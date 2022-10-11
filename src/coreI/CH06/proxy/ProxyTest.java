package coreI.CH06.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

/**
 * 使用代理机制主要用于解决以下三个问题
 *     1. 将方法调用路由到远程服务器
 *     2. 在运行的程序中将用户界面事件与动作关联起来
 *     3. 为了调试，跟踪方法调用：本次案例解决该问题⭐
 */
public class ProxyTest {
    public static void main(String[] args) {
        var elements = new Object[1000];
        for (int i = 0; i < elements.length; i++) {
            Integer value = i + 1;
            var handler = new TraceHandler(value);

            /**
             * 创建代理对象，需要使用Proxy类的newProxyInstance方法
             * @param 参数1 类加载器
             * @param 参数2 一个Class对象数组
             * @param 参数3 一个调用处理器
             */
            Object proxy = Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(),
                    new Class[]{Comparable.class},
                    handler
            );
            elements[i] = proxy;
        }

        Integer key = new Random().nextInt(elements.length) + 1;
        int result = Arrays.binarySearch(elements, key);
        if (result >= 0) {
            System.out.println(elements[result]);
        }

    }

}

/**
 * 调用处理器：实现了InvocationHandler接口的类的对象
 * 该接口只有一个方法：invoke
 * 注意：由于该接口仅有一个抽象方法，即为一个函数式接口，因此我们更好的实现是使用lambda表达式。
 * 当然：也可以使用匿名内部类实现
 */
class TraceHandler implements InvocationHandler {
    public Object target;

    public TraceHandler(Object target) {
        this.target = target;
    }


    @Override
    /**
     * 本次实现的invoke方法会打印所调用的方法的名字和参数
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.print(target+"." + method.getName() + "(");
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                System.out.print(args[i]);
                if (i < args.length - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println(")");
        return method.invoke(target, args);

    }
}
