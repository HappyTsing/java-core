package coreI.CH12.synchReentrantLock;


/**
 * @author QingHappy
 */
public class SynchBankTest {

    public static final int NACCOUNTS = 100;
    public static final double INITIAL_BALANCE = 1000;
    public static final double MAX_AMOUNT = 1000;
    public static final int DELAY = 10;

    public static void main(String[] args)
    {
        var bank = new Bank(NACCOUNTS, INITIAL_BALANCE);
        /**
         * 创建NACCOUNTS（100）个线程，并启动！
         * 出现问题：总金额数目会发生改变，这显然是不合理的，其原因是没有保证操作的原子性！
         */
        for (int i = 0; i < NACCOUNTS; i++)
        {
            int fromAccount = i;
            Runnable r = () -> {
                try
                {
                    while (true)
                    {
                        int toAccount = (int) (bank.size() * Math.random());
                        double amount = MAX_AMOUNT * Math.random();
                        bank.transfer(fromAccount, toAccount, amount);
                        Thread.sleep((int) (DELAY * Math.random()));
                    }
                }
                catch (InterruptedException e)
                {
                }
            };
            var t = new Thread(r);
            t.start();
        }
    }
}
