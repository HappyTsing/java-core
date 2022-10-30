package coreI.CH06.dynamicVsStaticProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AccountServiceImplDynamicProxy {
    /**
     * 创建账户业务层实现类的代理对象
     */
    public static IAccountService getAccountService() {
        // 1.定义被代理对象
        final IAccountService accountService = new AccountServiceImpl();
        // 2.创建代理对象
        IAccountService proxyAccountService = (IAccountService) Proxy.newProxyInstance(
                accountService.getClass().getClassLoader(),
                accountService.getClass().getInterfaces(),
                new InvocationHandler() {
                    /**
                     * 执行被代理对象的任何方法，都会经过该方法。 此处添加事务控制
                     */
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object rtValue = null;
                        try {
                            // 开启事务TransactionManager.beginTransaction();
                            // 执行业务层方法
                            rtValue = method.invoke(accountService, args);

                            // 提交事务 TransactionManager.commit();
                        } catch (Exception e) {
                            System.out.println("事务失败，回滚");
                            // 回滚事务 TransactionManager.rollback();
                            e.printStackTrace();
                        } finally {
                            // 释放资源 TransactionManager.release();
                        }
                        return rtValue;
                    }
                });
        return proxyAccountService;
    }

    public static void main(String[] args) {
        IAccountService proxyAccountService = AccountServiceImplDynamicProxy.getAccountService();
        proxyAccountService.transfer("source", "target", 2);

    }
}
