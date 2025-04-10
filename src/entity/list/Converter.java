package entity.list;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converter<T> {

    // converter constant 
    public static final String LIST_SEPARATOR = "::LIST::";
    public static final String DATE_SEPARATOR = "::DATE::";
    public static final String MAP_SEPARATOR = "::MAP::";

    public static <T> T StringtoObj(String line, Class<T> clazz){

        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
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
                // handle int 
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

    // public String objToString(T obj){

    // }
    public static List<String> stringToList(String data){
        String[] values = data.split(LIST_SEPARATOR);
        List<String> ret = Arrays.asList(values);
        return ret;
    }

    public static String listToString(List<String> data){
        String ret = "";
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
      
    public static String mapToString(Map<String,String> mp){
        Map.Entry<String, String> pair = mp.entrySet().iterator().next();
        return pair.getKey() + MAP_SEPARATOR + pair.getValue();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> T convert(String value, Class<T> type) {
        if (type == Integer.class || type == Integer.TYPE) return (T) Integer.valueOf(value);
        if (type == Double.class) return (T) Double.valueOf(value);
        if (type == Boolean.class || type == Boolean.TYPE) return (T) Boolean.valueOf(value);
        if (type.isEnum()) return (T) Enum.valueOf((Class<Enum>) type.asSubclass(Enum.class), value);
        return (T) value; // fallback to String
    }
    

     
}
