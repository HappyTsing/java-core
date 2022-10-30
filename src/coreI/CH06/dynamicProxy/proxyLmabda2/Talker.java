package coreI.CH06.dynamicProxy.proxyLmabda2;

public class Talker implements Talk, Eat {

    @Override
    public void talk(String word) {
        System.out.println("Talker 类在说：" + word);
    }

    @Override
    public void eat(String foodName, int amount) {
        System.out.println("Talker 吃了" + amount + "个" + foodName);
    }
}
