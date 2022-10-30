# 静态代理VS动态代理

静态代理步骤：

1. 定义一个接口及其实现类；
2. 创建一个代理类同样实现这个接口
3. 将**目标对象注注入进代理类**，然后在代理类的对应方法调用目标类中的对应方法。这样的话，我们就可以通过代理类屏蔽对目标对象的访问，并且可以在目标方法执行前后做一些自己想做的事情。

假设有一个接口，其中有一个转账方法：

```java
public interface IAccountService{
    void transfer(String sourceName,String targetName,Float money);
}
```

接口实现类实现了该方法:

```java
public class AccountServiceImpl implements IAccountService {
    public void transfer(String sourceName, String targetName, Float money) {
        //根据名称查询两个账户信息
        Account source = accountDao.findByName(sourceName);
        Account target = accountDao.findByName(targetName);
        //转出账户减钱，转入账户加钱
        source.setMoney(source.getMoney()-money);
        target.setMoney(target.getMoney()+money);
        //更新两个账户
        accountDao.update(source);
        int i=1/0; //模拟转账异常
        accountDao.update(target);
	}
}
```

我们知道，转账操作这显然是一个完整的事务，要么全部执行，要么全部不执行，于是我们打算创建一个代理类，代理类中实现类，调用它的方法，并增强这个方法！

```java
public class AccountServiceImplProxy implements IAccountService {
    
    //被代理类
     AccountServiceImpl as = new AccountServiceImpl();
    
    public void transfer(String sourceName, String targetName, Float money) {
 
        TransactionManager.beginTransaction(); //开启事务
        try{
            as.transfer(sourceName,targetName,money); //调用我们需要增强的方法
            TransactionManager.commit();  //提交事务
        }
        catch (Exception e) {
            TransactionManager.rollback(); //出错则回滚
            e.printStackTrace();
        }
        finally {
            TransactionManager.release();
        }
	}
}
```

此时就完成了静态代理，但是有一个缺点，如果该接口中有10个方法，其中5个方法需要增强（也就是事务管理），那么我们就要为这五个方法进行相同的增强操作，这就造成了代码的冗余。

如何解决代码冗余问题？

**动态代理！**

构建一个动态代理类：

```java
//用于创建客户业务层对象工厂
public class BeanFactory {
    /**
     * 创建账户业务层实现类的代理对象
     */
    public static IAccountService getAccountService() {
        // 1.定义被代理对象
        final IAccountService accountService = new AccountServiceImpl();
        // 2.创建代理对象
        IAccountService proxyAccountService =(IAccountService)Proxy.newProxyInstance(
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
                            // 开启事务
                            TransactionManager.beginTransaction();
                            // 执行业务层方法
                            rtValue = method.invoke(accountService, args);
                            // 提交事务
                            TransactionManager.commit();
                        } catch (Exception e) {
                            // 回滚事务
                            TransactionManager.rollback();
                            e.printStackTrace();
                        } finally {
                            // 释放资源
                            TransactionManager.release();
                        }
                        return rtValue;
                    }
                });
        return proxyAccountService;
    }
}
```

通过该工厂获取代理对象

```java
public class AccountServiceImplProxyTest{
    public static void main(String[] args) {
        //得到动态代理对象，通过该对象调用的方法都会经过增强！
        IAccountService proxyAccountService = BeanFactory.getAccountService();

        //通过该动态代理对象调用方法！
        proxyAccountService.transfer(sourceName,targetName,money);
    }
}
```