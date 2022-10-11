package coreI.CH06.proxyInnerClass;


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
 *
 *     使用匿名内部类实现函数式接口InvocationHandler接口
 *
 * @author QingHappy
 */
public class ProxyTest {
    public static void main(String[] args) {
        var elements = new Object[1000];

        /**
         * 该for循环用于在数组中填充elements数组，每一个元素都是Object类的代理
         */
        for (int i = 0; i < elements.length; i++) {
            Integer value = i + 1;

            /**
             * 创建代理对象，需要使用Proxy类的newProxyInstance方法
             * @param 参数1 类加载器 用于加载代理对象的字节码的用于指被代理对象的类加载器
             * @param 参数2 一个Class对象数组 用于让代理对象和被代理对象有相同的接口
             *            new Class[]{ 接口一.class，接口二.class}
             * @param 参数3 一个调用处理器 如何代理，即代理需要实现哪些功能，一般时写一个接口的实现类！可以用匿名内部类实现
             *
             */
            Object proxy = Proxy.newProxyInstance(
                    ClassLoader.getSystemClassLoader(), //使用系统类加载器，此处代理的是Integer对象
                    new Class[]{Comparable.class},      //代理对象和Integer对象都实现了Comparable接口
                    new InvocationHandler() {
                        /**
                         * 作用：执行被代理对象的任何接口方法都会经过该方法
                         * 方法参数的含义
                         * @param proxy  代理对象的引用
                         * @param method 当前执行的方法
                         * @param args   当前执行方法所需的参数
                         * @return       和被代理对象方法有相同的返回值
                         * @throws Throwable
                         */
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                            System.out.print(value+"." + method.getName() + "(");

                            /**
                             * 该if函数用于输出参数，在二分查找过程中，只用到了两个方法：
                             *   1。 compareTo，有一个参数
                             *   2。 toString，无参数
                             */
                            if (args != null) {
                                for (int i = 0; i < args.length; i++) {
                                    System.out.print(args[i]);
                                    if (i < args.length - 1) {
                                        System.out.print(", ");
                                    }
                                }
                            }
                            System.out.println(")");
                            return method.invoke(value, args);

                        }
                    }

            );
            elements[i] = proxy;
        }

        Integer key = new Random().nextInt(elements.length) + 1;
        /**
         * binarySearch(object[ ], object key); 如果key在数组中,则返回搜索值的索引;否则返回-1
         */
        int result = Arrays.binarySearch(elements, key);
        if (result >= 0) {
            System.out.println(elements[result]);
        }


    }

}

