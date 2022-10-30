package coreI.CH06.dynamicVsStaticProxy;

/**
 * 此处的转账存在问题，并不是一个事务。即可能存在source更新成功，但是target更新失败
 * 因此需要使用静态代理来对他进行增强
 */
public class AccountServiceImpl implements IAccountService {

    @Override
    public void transfer(String sourceName, String targetName, Integer money) {
        AccountDao accountDao = new AccountDao();
        // 根据名称查询两个账户信息
        Account source = accountDao.findByName(sourceName);
        Account target = accountDao.findByName(targetName);
        // 转出账户减钱，转入账户加钱
        source.setMoney(source.getMoney() - money);
        target.setMoney(target.getMoney() + money);
        // 更新两个账户
        accountDao.update(source);
        int i = 1 / 0; // 模拟转账异常
        accountDao.update(target);
    }

    public static void main(String[] args) {
        IAccountService asimpl = new AccountServiceImpl();
        asimpl.transfer("source", "target", 2);

    }
}
