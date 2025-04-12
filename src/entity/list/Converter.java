package entity.list;

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

public class Converter<T> {

    // converter constant 
    public static final String LIST_SEPARATOR = "::LIST::";
    public static final String DATE_SEPARATOR = "::DATE::";
    public static final String MAP_SEPARATOR = "::MAP::";

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
            List<String> lineData = Arrays.asList(values);
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
                    Object value = convert(lineData.get(idx), fieldType);
                    field.set(obj,value);
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
    public static List<String> stringToList(String data){
        if(data == "") return new ArrayList<>();
        String[] values = data.split(LIST_SEPARATOR);
        List<String> ret = Arrays.asList(values);
        return ret;
    }

    public static String listToString(List<String> data){
        String ret = "";
        if(data.isEmpty()) return "null";
        for(int i = 0; i < data.size()-1; i++){
            ret+=data.get(i) + LIST_SEPARATOR;
        }
        ret+=data.get(data.size()-1);
        return ret;
    }

    public static LocalDate stringToDate(String data){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M'::DATE::'dd'::DATE::'yyyy"); 
        LocalDate ret = LocalDate.parse(data, formatter);
        return ret;
    }

    public static String dateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M'::DATE::'dd'::DATE::'yyyy");
        return date.format(formatter);
    }

    public static <A,B> Map<A,B> stringToMap(String data, Class<A> keyType, Class<B> valueType){
        Map<A,B> ret = new HashMap<>();
        if ("null".equals(data)) return ret;
        String[] values = data.split(LIST_SEPARATOR);
        List<String> valuesList = Arrays.asList(values);
        for(String value:valuesList){
            String[] mp = value.split(MAP_SEPARATOR);
            List<String> mpList = Arrays.asList(mp);
            A key = convert(mpList.get(0), keyType);
            B val = convert(mpList.get(1), valueType);
            ret.put(key,val);
        }
        return ret;
    }
      
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T convert(String value, Class<T> type) {
        if (type == Integer.class || type == Integer.TYPE) return (T) Integer.valueOf(value);
        if (type == Double.class) return (T) Double.valueOf(value);
        if (type == Boolean.class || type == Boolean.TYPE) return (T) Boolean.valueOf(value);
        if (type.isEnum()) return (T) Enum.valueOf((Class<Enum>) type.asSubclass(Enum.class), value);
        return (T) value; // fallback to String
    }

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

    public static Class<? extends Request> getRequestClass(String s){
        String[] parts = s.split(",");
        List<String> ls = Arrays.asList(parts);
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
