package pl.olart.pmsuite.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: grp
 * Date: 2017-06-29
 * Time: 09:57
 */
public class CommonUtils {
    public static Object wywolajMetodeBezArgumentowa(Object obiekt, String nazwaMetody) {
        try {
            Method method = obiekt.getClass().getMethod(nazwaMetody);
            Object retobj
                    = method.invoke(obiekt);
            return retobj;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object wywolajMetodeJednoArgumentowa(Object obiekt, String nazwaMetody, Class<?> parametrMetody, Object wartoscParametruMetody) {
        try {
            Method method = obiekt.getClass().getMethod(nazwaMetody, parametrMetody);
            Object retobj
                    = method.invoke(obiekt, wartoscParametruMetody);
            return retobj;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
