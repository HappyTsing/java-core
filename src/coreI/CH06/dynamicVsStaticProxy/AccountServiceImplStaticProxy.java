package coreI.CH06.dynamicVsStaticProxy;

/**
 * 静态代理，其实就是包了一层。
 * 非常的麻烦，因为：
 * 1. 每一个接口（类）都需要自己的代理类，且代理类和被代理类有着大量的重复代码！
 * 2. 对目标对象的每个方法的增强都是手动完成的
 * 不灵活: 比如接口一旦新增加方法，目标对象和代理对象都要进行修改
 * 麻烦: 需要对每个目标类都单独写一个代理类
 */
public class AccountServiceImplStaticProxy implements IAccountService {

    // 被代理类
    AccountServiceImpl as = new AccountServiceImpl();

    // Spring事务
    public void transfer(String sourceName, String targetName, Integer money) {

        // 开启事务 TransactionManager.beginTransaction();
        try {
            as.transfer(sourceName, targetName, money); // 调用我们需要增强的方法
            // 提交事务 TransactionManager.commit();
        } catch (Exception e) {
            // 出错则回滚 TransactionManager.rollback();
            System.out.println("事务失败，回滚");
            e.printStackTrace();
        } finally {
            // 释放资源 TransactionManager.release();
        }
    }

    public static void main(String[] args) {
        IAccountService asimpl = new AccountServiceImplStaticProxy();
        asimpl.transfer("source", "target", 2);

    }
}