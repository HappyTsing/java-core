package coreI.CH06.dynamicProxy.proxyLmabda;

import java.lang.reflect.Proxy;

/**
 * 使用lambda表达式
 */
public class ProxyTest {
    public static void main(String[] args) throws ClassNotFoundException {
        // 1. 获得需要构建的对象的实例
        var talker = new Talker();
        // 2. 创建代理对象.强转时括号内应该为接口类型,这种代理方式代理对象时,该对象类必须至少实现了一个接口
        var proxy = (Talk) Proxy.newProxyInstance(
                talker.getClass().getClassLoader(),
                // Class.forName("coreI.CH06.proxyLmabda.Talker").getClassLoader(),
                // //作用同talker.getClass().getClassLoader()，只是获得class的方法不同！
                // Talker.class.getClassLoader(),
                // //作用同talker.getClass().getClassLoader()，只是获得class的方法不同！
                talker.getClass().getInterfaces(),
                // new Class[]{Talk.class}, //作用同talker.getClass().getImterfaces()相同!!
                (proxy1, method, args1) -> {
                    System.out.println("method Name is：" + method.getName());
                    System.out.println("method args is：" + args1[0]);
                    return method.invoke(talker, args1);
                });
        // 3. 调用代理对象
        proxy.talk("你好");

    }
}
