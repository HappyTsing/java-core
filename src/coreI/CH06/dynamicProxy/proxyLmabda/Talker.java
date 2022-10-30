package coreI.CH06.dynamicProxy.proxyLmabda;

public class Talker implements Talk {

    @Override
    public void talk(String word) {
        System.out.println("Talker 类在说：" + word);
    }
}
