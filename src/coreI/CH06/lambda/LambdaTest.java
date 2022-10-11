package coreI.CH06.lambda;

import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.function.Predicate;


/**
 * @author QingHappy
 */
public class LambdaTest {
    public static void main(String[] args) {
        var planets = new String[]{"Mercury", "Ven", "Earth"};
        System.out.println(Arrays.toString(planets));


        //第二个参数本来应该是实现了Comparator接口的实例，该接口只有一个抽象方法compare
        Arrays.sort(planets, (first, second) -> first.length() - second.length());
        System.out.println(Arrays.toString(planets));

        //第二个参数本来应该是实现了ActionListener接口的实例，该接口只有一个抽象方法，即函数式接口，可用lambda表达式代替
        var timer = new Timer(1000, event -> System.out.println("时间是：" + new Date()));
        timer.start();
        JOptionPane.showMessageDialog(null, "quie?");
        System.exit(0);



    }
}
