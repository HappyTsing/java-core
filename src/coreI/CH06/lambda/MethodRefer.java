package coreI.CH06.lambda;

import javax.swing.*;
import java.util.Arrays;

/**
 * 方法引用，其本质是对lambda表达式的重写。
 * 注意：只用当lambda表达式的体只调用一个方法而不做其他操作时，才能把lambda表达式重写为方法引用。
 * @author happytsing
 */
public class MethodRefer {
    public static void main(String[] args) {
        testArrays();

    }

    public static void testTimer() {
        // 假设你希望只要出现一个定时器事件就打印这个事件对象，如此调用：
        Timer timer = new Timer(1000, event -> System.out.println(event));

        // 等价于如下方法引用：
        Timer timerRefer = new Timer(1000, System.out::println);
        timer.start();
        JOptionPane.showMessageDialog(null, "quie?");
        System.exit(0);
    }

    public static void testArrays() {
        String[] strings = {"alice","bob","fake","cat"};
        // 对字符串进行排序，而不考虑字母的大小
        Arrays.sort(strings,String::compareToIgnoreCase);
        System.out.println(Arrays.toString(strings));
    }

    public static void testMath() {

    }
}
