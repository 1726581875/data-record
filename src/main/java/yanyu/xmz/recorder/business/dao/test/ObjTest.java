package yanyu.xmz.recorder.business.dao.test;

import yanyu.xmz.recorder.business.dao.TestObj;

import java.lang.reflect.InvocationTargetException;

/**
 * @author xiaomingzhang
 * @date 2022/10/11
 */
public class ObjTest {


    public static void main(String[] args) throws Exception {

        getInstance(TestObj.class);

    }

    private static  <T> T getInstance(Class<T> returnType) throws Exception {

        T t = returnType.newInstance();
        return t;

/*        T resultInstance = returnType.getConstructor().newInstance();
        return resultInstance;*/
    }

}
