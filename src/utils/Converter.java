package utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.request.BTOApplication;
import entity.request.BTOWithdrawal;
import entity.request.Enquiry;
import entity.request.OfficerRegistration;
import entity.request.Request;
import entity.request.RequestType;

/**
 * Utility class providing static methods for converting objects to and from string representations,
 * primarily designed for CSV serialization and deserialization.
 * Uses reflection to handle object fields, including common types like String, Integer, Boolean,
 * Enums, and complex types like {@code List<String>}, {@code Map<K, V>}, and {@link LocalDate}.
 * Defines specific separators for serializing collection types. Includes special handling
 * for determining subclasses of {@link Request}.
 *
 * Note: The generic type {@code <T>} on the class declaration itself appears unused as all methods are static.
 *
 * @param <T> Unused generic type parameter on class declaration.
 */
public class Converter<T> {

    // converter constant 
     /** Separator used within a CSV field to delimit elements of a List. */
    public static final String LIST_SEPARATOR = "::LIST::";
     /** Separator used within a CSV field to delimit parts of a LocalDate representation. */
    public static final String DATE_SEPARATOR = "::DATE::";
     /** Separator used within a Map entry string to separate the key from the value. */
    public static final String MAP_SEPARATOR = "::MAP::";

    /**
     * Deserializes a single line of comma-separated string data into an object of the specified class.
     * Uses reflection to instantiate the object and set its fields based on the string values.
     * Handles fields inherited from up to two levels of superclasses.
     * Supports conversion for String, Integer, Double, Boolean, Enum, LocalDate, List&lt;String&gt;, and Map&lt;?,?&gt; types.
     * Assumes custom formats for List, Map, and LocalDate using defined separators.
     * Treats the literal string "null" as a null value for fields.
     *
     * @param <T>   The type of the object to create.
     * @param line  The comma-separated string representing the object's data.
     * @param clazz The {@code Class} object of the type {@code T}.
     * @return An object of type {@code T} populated with data from the string, or {@code null} if an error occurs.
     */
    public static <T> T stringtoObj(String line, Class<T> clazz){

        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] f = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<>();
            // check for user super class
            Class<?> current = clazz;
            current = clazz.getSuperclass();
            if(current != null){
                Field[] ff = current.getDeclaredFields();
                current = current.getSuperclass();
                if(current != null){
                    Field[] fff = current.getDeclaredFields();
                    for(Field i: fff) fields.add(i);
                }
                for(Field i: ff) fields.add(i);
            }
            for(Field i:f) fields.add(i);
            String[] values = line.split(",");
            List<String> lineData = new ArrayList<>(Arrays.asList(values));
            int idx = 0; // values iterator 
            for(Field field: fields){
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if(List.class.isAssignableFrom(fieldType)){
                    List<String> ls = stringToList(lineData.get(idx));
                    field.set(obj,ls);
                }
                // handle map
                else if(Map.class.isAssignableFrom(fieldType)){
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType pt) {
                        Type keyType = pt.getActualTypeArguments()[0];
                        Type valueType = pt.getActualTypeArguments()[1];
                        Class<?> keyClass = (Class<?>) keyType;
                        Class<?> valueClass = (Class<?>) valueType;
                        Map<?,?> mp = stringToMap(lineData.get(idx),keyClass,valueClass);
                        field.set(obj,mp);
                    }
                }
                // handle date
                else if(fieldType.equals(LocalDate.class)){
                    LocalDate date = stringToDate(lineData.get(idx));
                    field.set(obj,date);
                }
                // handle other
                else {
                    if(lineData.get(idx).equals("null")){
                        field.set(obj,null);
                    }
                    else{
                        Object value = convert(lineData.get(idx), fieldType);
                        field.set(obj,value);
                    }
                }
                idx++;
            }
            return obj;
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
       
    }
    /**
     * Serializes an object into a single comma-separated string representation.
     * Uses reflection to access object fields (including inherited ones up to two levels).
     * Handles List&lt;String&gt;, Map&lt;?,?&gt;, LocalDate, primitives, enums, and Strings using helper methods
     * and defined separators. Null field values are represented by the literal string "null".
     *
     * @param <T> The type of the object to serialize.
     * @param obj The object to convert to a string.
     * @return A comma-separated string representing the object's data, or {@code null} if an error occurs.
     */
    public static <T> String objToString(T obj){
        String ret = "";
        try {
            Class<?> clazz = obj.getClass();
            Field[] f = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<>();

           // check for superClass
            clazz = clazz.getSuperclass();
            if(clazz != null){
                Field[] ff = clazz.getDeclaredFields();
                clazz = clazz.getSuperclass();
                if(clazz != null){
                    Field[] fff = clazz.getDeclaredFields();
                    for(Field i: fff) fields.add(i);
                }
                for(Field i: ff) fields.add(i);
            }
            for(Field i:f) fields.add(i);

            for(Field field: fields){
                field.setAccessible(true);
                Object val = field.get(obj);
                Class<?> fieldType = field.getType();
                if(List.class.isAssignableFrom(fieldType)){
                    @SuppressWarnings("unchecked")
                    List<String> lsval = (List<String>) val; 
                    String ls = listToString(lsval);
                    ret+=ls;
                }
                // handle map
                else if(Map.class.isAssignableFrom(fieldType)){
                    @SuppressWarnings("unchecked")
                    Map<Object, Object> mpval = (Map<Object, Object>) val;
                    String mp = mapToString(mpval); 
                    ret+=mp;
                }
                // handle date
                else if(fieldType.equals(LocalDate.class)){
                    LocalDate dateval = (LocalDate) val;
                    String date = dateToString(dateval);
                    ret+=date;
                }
                // handle other
                else {
                    if (val == null) {
                        ret += "null";
                    } else {
                        ret += val.toString();
                    }
                }
                ret+=",";
            }
            ret = ret.substring(0, ret.length()-1);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return ret;
    }

     /**
     * Converts a string representation (using {@code LIST_SEPARATOR}) back into a List of Strings.
     * If the input data is the literal string "null" or empty, returns an empty list.
     *
     * @param data The string data, potentially containing elements separated by {@code LIST_SEPARATOR}.
     * @return A {@link List} of strings. Returns an empty list if input is "null" or empty.
     */
    public static List<String> stringToList(String data){
        if(data.equals("null")) return new ArrayList<>();
        String[] values = data.split(LIST_SEPARATOR);
        List<String> ret = new ArrayList<>(Arrays.asList(values));
        return ret;
    }

    /**
     * Converts a List of Strings into a single string representation using {@code LIST_SEPARATOR}.
     * If the input list is null or empty, returns the literal string "null".
     *
     * @param data The {@link List} of strings to convert.
     * @return A single string with elements joined by {@code LIST_SEPARATOR}, or "null" if the list is empty/null.
     */
    public static String listToString(List<String> data){
        String ret = "";
        if(data.isEmpty()) return "null";
        for(int i = 0; i < data.size()-1; i++){
            ret+=data.get(i) + LIST_SEPARATOR;
        }
        ret+=data.get(data.size()-1);
        return ret;
    }

    /**
     * Parses a string representation (using {@code DATE_SEPARATOR}) into a {@link LocalDate} object.
     * Expects the format "M::DATE::dd::DATE::yyyy".
     *
     * @param data The string representation of the date (e.g., "4::DATE::22::DATE::2025").
     * @return The parsed {@link LocalDate} object, or null if parsing fails or input is null/empty.
     */
    public static LocalDate stringToDate(String data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M'::DATE::'dd'::DATE::'yyyy"); 
        LocalDate ret = LocalDate.parse(data, formatter);
        return ret;
    }

    /**
     * Formats a {@link LocalDate} object into a specific string representation using {@code DATE_SEPARATOR}.
     * The output format is "M::DATE::dd::DATE::yyyy".
     *
     * @param date The {@link LocalDate} object to format.
     * @return The formatted date string, or "null" if the input date is null.
     */
    public static String dateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M'::DATE::'dd'::DATE::'yyyy");
        return date.format(formatter);
    }

    /**
     * Converts a string representation (using {@code LIST_SEPARATOR} for entries and {@code MAP_SEPARATOR} within entries)
     * back into a {@code Map<A, B>}.
     * Uses the {@link #convert(String, Class)} helper method to convert keys and values to their target types.
     * If the input data is the literal string "null" or empty, returns an empty map.
     *
     * @param <A>       The type of the keys in the map.
     * @param <B>       The type of the values in the map.
     * @param data      The serialized string representation of the map.
     * @param keyType   The {@code Class} object for the key type {@code A}.
     * @param valueType The {@code Class} object for the value type {@code B}.
     * @return A {@link Map} populated from the string data. Returns an empty map if input is "null" or empty.
     */
    public static <A,B> Map<A,B> stringToMap(String data, Class<A> keyType, Class<B> valueType){
        Map<A,B> ret = new HashMap<>();
        if (data.equals("null")) return ret;
        String[] values = data.split(LIST_SEPARATOR);
        List<String> valuesList = new ArrayList<>(Arrays.asList(values));
        for(String value:valuesList){
            String[] mp = value.split(MAP_SEPARATOR);
            List<String> mpList = new ArrayList<>(Arrays.asList(mp));
            A key = convert(mpList.get(0), keyType);
            B val = convert(mpList.get(1), valueType);
            ret.put(key,val);
        }
        return ret;
    }
    
    /**
     * Converts a {@code Map<A, B>} into a single string representation.
     * Entries are separated by {@code LIST_SEPARATOR}. Keys and values within an entry
     * are separated by {@code MAP_SEPARATOR}. Keys and values are converted using their
     * {@code toString()} method.
     * If the input map is null or empty, returns the literal string "null".
     *
     * @param <A> The type of the keys in the map.
     * @param <B> The type of the values in the map.
     * @param mp  The {@link Map} to convert.
     * @return A single string representation of the map, or "null" if the map is empty/null.
     */
    public static <A,B> String mapToString(Map<A,B> mp){
        String ret = "";
        if(mp.isEmpty()) return "null";
        int cnt = 0;
        for(Map.Entry<A,B> entry: mp.entrySet()){
            if(cnt == mp.size()-1) ret += entry.getKey() + MAP_SEPARATOR + entry.getValue();
            else ret += entry.getKey() +  MAP_SEPARATOR + entry.getValue() + LIST_SEPARATOR;
            cnt++;
        }
        return ret;
    }

    /**
     * Converts a string value to a specified target type using basic conversion logic.
     * Handles Integer, Double, Boolean (case-insensitive "true"), and Enum types.
     * If the type is not explicitly handled, it returns the original string value.
     *
     * @param <T>   The target type.
     * @param value The string value to convert.
     * @param type  The {@code Class} object of the target type {@code T}.
     * @return The converted value as type {@code T}, or the original string if no specific conversion applies.
     * Returns null if the input value is null. May throw runtime exceptions on parsing errors (e.g., NumberFormatException).
     * @throws NumberFormatException If conversion to Integer or Double fails.
     * @throws IllegalArgumentException If conversion to Enum fails (value not found).
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T convert(String value, Class<T> type) {
        if (type == Integer.class || type == Integer.TYPE) return (T) Integer.valueOf(value);
        if (type == Double.class) return (T) Double.valueOf(value);
        if (type == Boolean.class || type == Boolean.TYPE) return (T) Boolean.valueOf(value);
        if (type.isEnum()) return (T) Enum.valueOf((Class<Enum>) type.asSubclass(Enum.class), value);
        return (T) value; // fallback to String
    }

    /**
     * Generates a comma-separated string of field names for a given object's class,
     * including fields inherited from superclasses (up to two levels).
     * This is typically used to create a header row for a CSV file.
     *
     * @param <T> The type of the object.
     * @param obj An instance of the object whose class's fields are to be retrieved.
     * (Can be null if only class structure is needed, but instance is safer for non-static fields).
     * @return A comma-separated string of field names, or {@code null} if an error occurs.
     */
    public static <T> String getField(T obj){
        String ret = "";
        try {
            Class<?> clazz = obj.getClass();
            Field[] f = clazz.getDeclaredFields();
            List<Field> fields = new ArrayList<>();
            // check for user super class
            clazz = clazz.getSuperclass();
            if(clazz != null){
                Field[] ff = clazz.getDeclaredFields();
                clazz = clazz.getSuperclass();
                if(clazz != null){
                    Field[] fff = clazz.getDeclaredFields();
                    for(Field i: fff) fields.add(i);
                }
                for(Field i: ff) fields.add(i);
            }
            for(Field i:f) fields.add(i);
            for(Field field: fields){
                field.setAccessible(true);
                ret+=field.getName() + ",";
            }
            ret = ret.substring(0, ret.length()-1);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return ret;
    }

    /**
     * Determines the specific subclass of {@link Request} based on a serialized string line.
     * It assumes the second comma-separated value in the line corresponds to a {@link RequestType} enum name.
     *
     * @param s The comma-separated string line representing a serialized Request object.
     * @return The specific {@code Class} object extending {@code Request} (e.g., {@code Enquiry.class}),
     * or the base {@code Request.class} if the type is NONE.
     * @throws IllegalArgumentException If the request type string derived from the input is unknown or invalid.
     */
    public static Class<? extends Request> getRequestClass(String s){
        String[] parts = s.split(",");
        List<String> ls = new ArrayList<>(Arrays.asList(parts));
        String type = ls.get(1);
        try {
            return switch (RequestType.valueOf(type)) {
                case ENQUIRY -> Enquiry.class;
                case BTO_APPLICATION -> BTOApplication.class;
                case BTO_WITHDRAWAL -> BTOWithdrawal.class;
                case REGISTRATION -> OfficerRegistration.class;
                case NONE -> Request.class;
            };
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown RequestType: " + type, e);
        }
    }
}
