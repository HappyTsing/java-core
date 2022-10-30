package coreI.CH06.dynamicVsStaticProxy;

/**
 * 模拟从数据库中获取账户数据，以及更新数据库数据
 * 
 * @author HappyTsing
 */
public class AccountDao {

    public Account findByName(String name) {
        Account account = new Account(10, name);
        return account;
    }

    public void update(Account account) {
        System.out.println("将最新的数据写入数据库中, money = " + account.getMoney() + " name=" + account.getName());
    }

}
