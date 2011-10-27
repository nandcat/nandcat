package nandcat;

import java.lang.reflect.Field;


public abstract class ReflectionUtil {
    public static Object getPrivateField(Class<?> c, Object instance, String field) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Field classField = c.getDeclaredField(field);
        classField.setAccessible(true);
        return classField.get(instance);
    }
}
