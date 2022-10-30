package coreI.CH06.dynamicProxy.proxyLmabda2;

import java.lang.reflect.Proxy;

/**
 * 使用lambda表达式，实现两个接口的类的代理！
 */
public class ProxyTest {
    public static void main(String[] args) {
        // 1. 获得需要构建的对象的实例
        var talker = new Talker();
        // 2. 创建代理对象.强转时括号内应该为接口类型,这种代理方式代理对象时,该对象类必须至少实现了一个接口
        // 由于此处实现了两个接口，因此无法直接强转，先将他保存在Object母类中！具体使用时再强转！
        Object proxy = Proxy.newProxyInstance(
                talker.getClass().getClassLoader(),
                talker.getClass().getInterfaces(),
                // new Class[]{Talk.class,Eat.class}, //作用同talker.getClass().getImterfaces()相同!!
                (proxy1, method, args1) -> {
                    System.out.println("method Name is：" + method.getName());
                    System.out.print("method args is：");
                    // 用于输出所有参数
                    for (int i = 0; i < args1.length; i++) {
                        System.out.print(args1[i]);
                        if (i < args1.length - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                    return method.invoke(talker, args1);
                });
        // 3. 调用代理对象
        ((Talk) proxy).talk("你好！");
        ((Eat) proxy).eat("汉堡", 200);

    }
}
