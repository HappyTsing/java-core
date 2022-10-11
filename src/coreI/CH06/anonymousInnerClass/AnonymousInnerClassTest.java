package coreI.CH06.anonymousInnerClass;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

/**
 * 匿名内部类
 * @author QingHappy
 */
public class AnonymousInnerClassTest {

    public static void main(String[] args) {
//        var clock = new TalkingClock(1000, true);
        var clock = new TalkingClock();
        clock.start(1000, true);

        JOptionPane.showMessageDialog(null, "quit?");
        System.exit(0);
    }
}

class TalkingClock {
    private int interval;
    private boolean beep;
/*
    public TalkingClock(int interval, boolean beep) {
        this.beep = beep;
        this.interval = interval;
    }
*/

    public void start(int interval,boolean beep) {

        // 局部内部类的升级版：匿名内部类（即不为类指定名字）
        // ActionListener是一个接口，这一整句话的意思是：创建一个类的新对象，这个类实现了该接口！
        var listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("时间：" + Instant.ofEpochMilli(e.getWhen()));
                if (beep) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        };
        var timer = new Timer(interval, listener);
        timer.start();
    }
/*
    public class TimePrinter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("时间：" + Instant.ofEpochMilli(e.getWhen()));

            //此处的beep是外围类的private字段，内部类可以访问自身的数据字段和创建它的外围类对象的数组字段。
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

 */

}

