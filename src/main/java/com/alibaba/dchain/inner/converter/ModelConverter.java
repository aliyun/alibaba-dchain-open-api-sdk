package com.alibaba.dchain.inner.converter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.dchain.inner.annotation.NameMapping;
import com.alibaba.dchain.inner.exception.ErrorEnum;
import com.alibaba.dchain.inner.exception.OpenApiException;
import com.alibaba.dchain.inner.model.Model;
import com.alibaba.dchain.inner.utils.Assert;
import com.alibaba.dchain.inner.utils.ReflectUtil;
import com.alibaba.dchain.inner.utils.StringUtil;

import com.google.gson.Gson;

import static com.alibaba.dchain.inner.constants.CommonConstants.STRING_CLASS_NAME;

/**
 * parse object, change object to map, and mapping fieldName to the realName in NameInMap annotation, refer to TeaModel
 *
 * @author 开帆
 * @date 2022/02/17
 */
public final class ModelConverter {

    private static final String TRUE = "1";

    private static final String FALSE = "0";

    public ModelConverter() {
    }

    public static Object parseObject(Object object) throws OpenApiException {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass();
        if (Map.class.isAssignableFrom(clazz)) {
            Map<String, Object> map = (Map<String, Object>)object;
            Map<String, Object> result = new HashMap<>(16);
            for (Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                result.put(entry.getKey(), parseObject(entry.getValue()));
            }
            return result;
        }
        if (List.class.isAssignableFrom(clazz)) {
            List list = (List)object;
            List<Object> result = new ArrayList<>();
            for (Object obj : list) {
                result.add(parseObject(obj));
            }
            return result;
        }
        if (Model.class.isAssignableFrom(clazz)) {
            return changeToMap((Model)object);
        }
        return object;
    }

    public static Map<String, String> parseMapToMapString(Map<String, Object> map) throws OpenApiException {
        Map<String, Object> parsedMap = new HashMap<>(16);
        for (Entry<String, Object> entry : map.entrySet()) {
            parsedMap.put(entry.getKey(), parseObject(entry.getValue()));
        }
        return objectToStringInMap(parsedMap);
    }

    public static <T extends Model> T toModel(Map<String, ?> map, T model) throws OpenApiException {
        for (Field field : ReflectUtil.getAllField(model.getClass())) {
            String fieldName = getMappingFieldName(field);
            Object value = map.get(fieldName);
            if (value == null) {
                continue;
            }
            setField(model, field, value);
        }
        return model;
    }

    private static <T> void setField(T model, Field field, Object value) throws OpenApiException {
        try {
            Class<?> fieldCls = field.getType();
            Class<?> valueCls = value.getClass();
            if (Model.class.isAssignableFrom(fieldCls)) {
                if (Map.class.isAssignableFrom(valueCls)) {
                    Model fieldModel = (Model)fieldCls.getDeclaredConstructor().newInstance();
                    setFieldValue(field, model, toModel(cast(value), fieldModel));
                } else {
                    setFieldValue(field, model, value);
                }
            } else if (isCollectionOrMapType(fieldCls)) {
                setFieldValue(field, model,
                    convertToCollectionOrMapType(value, ((ParameterizedType)field.getGenericType())));
            } else if (isPrimitive(fieldCls)) {
                setFieldValue(field, model, processPrimitiveType(fieldCls, value));
            }
            // 其他类型field对象不支持转换
        } catch (Exception e) {
            throw new OpenApiException(ErrorEnum.SET_FIELD_ERROR, e);
        }
    }

    private static Object convertClass(Object value, Class clazz) throws OpenApiException {
        Class<?> valueCls = value.getClass();
        // 对象转换，仅支持Model类型
        if (Model.class.isAssignableFrom(clazz) && Map.class.isAssignableFrom(valueCls)) {
            try {
                return toModel(cast(value), (Model)clazz.newInstance());
            } catch (Exception e) {
                throw new OpenApiException(ErrorEnum.INIT_MODEL_ERROR, e);
            }
        } else {
            return value;
        }
    }

    private static Object convertParameterizedType(Object value, ParameterizedType type) throws OpenApiException {
        Class rawType = (Class)type.getRawType();
        if (isCollectionOrMapType(rawType)) {
            return convertToCollectionOrMapType(value, type);
        }
        // 非集合类，不支持泛型, 直接做类转换
        return convertClass(value, rawType);
    }

    private static Object convertToCollectionOrMapType(Object value, ParameterizedType type) throws OpenApiException {
        Class rawType = (Class)type.getRawType();
        if (Map.class.isAssignableFrom(rawType)) {
            return convertToMapType(value, type);
        }
        if (List.class.isAssignableFrom(rawType)) {
            return convertToListType(value, type);
        }
        throw new OpenApiException(ErrorEnum.FIELD_TYPE_NOT_SUPPORT);
    }

    private static Map<String, Object> convertToMapType(Object value, ParameterizedType type)
        throws OpenApiException {
        Type[] actualTypeArguments = type.getActualTypeArguments();
        // Map的key必须是String
        Assert.assertEquals(actualTypeArguments[0].getTypeName(), STRING_CLASS_NAME, ErrorEnum.FIELD_TYPE_NOT_SUPPORT);

        Type mapValueType = actualTypeArguments[1];
        Map<String, Object> valueMap = cast(value);
        Map<String, Object> result = new HashMap<>(valueMap.size());
        for (Entry<String, Object> entry : valueMap.entrySet()) {
            if (mapValueType == null || mapValueType instanceof WildcardType) {
                // 如果map值的泛型类型为空，或者是通配符，则不做值对象类型转换
                result.put(entry.getKey(), entry.getValue());
            } else if (mapValueType instanceof Class) {
                result.put(entry.getKey(), convertClass(entry.getValue(), (Class)mapValueType));
            } else if (mapValueType instanceof ParameterizedType) {
                result.put(entry.getKey(), convertParameterizedType(entry.getValue(), (ParameterizedType)mapValueType));
            } else {
                throw new OpenApiException(ErrorEnum.FIELD_TYPE_NOT_SUPPORT);
            }
        }
        return result;
    }

    private static List<Object> convertToListType(Object value, ParameterizedType type)
        throws OpenApiException {
        Type[] actualTypeArguments = type.getActualTypeArguments();
        Type listValueType = actualTypeArguments[0];
        List<Object> valueMap = cast(value);
        List<Object> result = new ArrayList<>(valueMap.size());
        for (Object element : valueMap) {
            if (listValueType == null || listValueType instanceof WildcardType) {
                // 如果list值的泛型类型为空，或者是通配符，则不做值对象类型转换
                result.add(element);
            } else if (listValueType instanceof Class) {
                result.add(convertClass(element, (Class)listValueType));
            } else if (listValueType instanceof ParameterizedType) {
                result.add(convertParameterizedType(element, (ParameterizedType)listValueType));
            } else {
                throw new OpenApiException(ErrorEnum.FIELD_TYPE_NOT_SUPPORT);
            }
        }
        return result;
    }

    private static Object processPrimitiveType(Class expect, Object object) throws Exception {
        if (String.class.isAssignableFrom(expect)) {
            if (object instanceof Number || object instanceof Boolean) {
                return object.toString();
            }
        } else if (Boolean.class.isAssignableFrom(expect)) {
            if (object instanceof String) {
                return Boolean.parseBoolean(String.valueOf(object));
            } else if (object instanceof Integer) {
                if (object.toString().equals(TRUE)) {
                    return true;
                } else if (object.toString().equals(FALSE)) {
                    return false;
                }
            }
        } else if (Integer.class.isAssignableFrom(expect)) {
            if (object instanceof String) {
                return Integer.parseInt(object.toString());
            }
            // 判断数值大小是否超过期望数据类型的上限，如果不超过则强制转换，如果超过则报错
            if (object instanceof Long && (Long)object <= Integer.MAX_VALUE) {
                return Integer.parseInt(object.toString());
            }
            if (object instanceof Double && (Double)object <= Integer.MAX_VALUE) {
                return new BigDecimal(object.toString()).intValue();
            }
        } else if (Long.class.isAssignableFrom(expect)) {
            if (object instanceof String || object instanceof Integer) {
                return Long.parseLong(object.toString());
            }
            if (object instanceof Double) {
                return new BigDecimal(object.toString()).longValue();
            }
        } else if (Float.class.isAssignableFrom(expect)) {
            if (object instanceof String || object instanceof Integer || object instanceof Long) {
                return Float.parseFloat(object.toString());
            }
            // 判断数值大小是否超过期望数据类型的上限，如果不超过则强制转换，如果超过则报错
            if (object instanceof Double && (Double)object <= Float.MAX_VALUE) {
                return new BigDecimal(object.toString()).floatValue();
            }
        } else if (Double.class.isAssignableFrom(expect)) {
            if (object instanceof String || object instanceof Integer || object instanceof Long
                || object instanceof Float) {
                return Double.parseDouble(object.toString());
            }
        }
        return object;
    }

    private static boolean isCollectionOrMapType(Class clazz) {
        return Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz);
    }

    private static void setFieldValue(Field field, Object object, Object value) throws IllegalAccessException {
        if (field.isAccessible()) {
            field.set(object, value);
            return;
        }
        field.setAccessible(true);
        try {
            field.set(object, value);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Map<String, String> objectToStringInMap(Map<String, Object> map) {
        Map<String, String> result = new HashMap<>(16);
        for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), (String)entry.getValue());
            } else {
                result.put(entry.getKey(), new Gson().toJson(entry.getValue()));
            }
        }
        return result;
    }

    private static Map<String, Object> changeToMap(Model model) throws OpenApiException {
        Class<? extends Model> clazz = model.getClass();
        List<Field> fields = ReflectUtil.getAllField(clazz);
        Map<String, Object> result = new HashMap<>(16);
        try {
            for (Field field : fields) {
                String fieldName = getMappingFieldName(field);
                Object fieldObj = getFieldValue(field, model);
                if (fieldObj == null) {
                    continue;
                }
                result.put(fieldName, parseObject(fieldObj));
            }
        } catch (Exception e) {
            throw new OpenApiException(ErrorEnum.PARSE_OBJECT_TO_MAP_ERROR, e);
        }
        return result;
    }

    private static Object getFieldValue(Field field, Object object) throws IllegalAccessException {
        if (field.isAccessible()) {
            return field.get(object);
        }
        field.setAccessible(true);
        try {
            return field.get(object);
        } finally {
            field.setAccessible(false);
        }
    }

    private static String getMappingFieldName(Field field) {
        NameMapping annotation = field.getAnnotation(NameMapping.class);
        if (annotation == null || StringUtil.isEmpty(annotation.value())) {
            return field.getName();
        }
        return annotation.value();
    }

    private static boolean isInteger(Class cls) {
        return cls == Integer.class || cls == int.class
            || cls == Byte.class || cls == byte.class
            || cls == Short.class || cls == short.class
            || cls == Long.class || cls == long.class;
    }

    private static boolean isFloat(Class cls) {
        return cls == Float.class || cls == float.class
            || cls == Double.class || cls == double.class;
    }

    private static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive()
            || cls == String.class
            || cls == Boolean.class
            || cls == Character.class
            || Number.class.isAssignableFrom(cls);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T)obj;
    }

}
