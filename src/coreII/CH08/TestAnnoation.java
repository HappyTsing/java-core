package coreII.CH08;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author happytsing
 * TODO 一般来说，会专门写一个类，来实现注解的功能。
 */
public class TestAnnoation {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Class clazz  = UseAnnotation.class;
        Method[] methods = clazz.getMethods();
        UseAnnotation obj = (UseAnnotation) clazz.getConstructor().newInstance();

        // 使用流，获取所有被注解People标注的方法
        List<Method> methodsAnnotation = Arrays.stream(methods).filter(m -> m.isAnnotationPresent(People.class)).collect(Collectors.toList());
        for (Method method : methodsAnnotation) {
            // 由于该方法用注解People标注了，因此做一些处理：
            // 首先拿到注解的属性，其中Name使用的是默认属性
            String name = method.getAnnotation(People.class).Name();
            String school = method.getAnnotation(People.class).value();

            // 调用原方法sayHello()，输入通过注解获取的属性值
            method.invoke(obj,name + " in " + school);
        }

    }
}
