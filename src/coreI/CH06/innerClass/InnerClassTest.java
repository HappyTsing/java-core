package coreI.CH06.innerClass;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import javax.swing.*;

public class InnerClassTest {

    public static void main(String[] args) {
        var clock = new TalkingClock(1000, true);
//        TalkingClock.TimePrinter li = new clock.TimePrinter(); 在外围类的作用域之外，可以这也引用内部类
        clock.start();


        JOptionPane.showMessageDialog(null, "quit?");
        System.exit(0);

    }
}

class TalkingClock {
    private int interval;
    private boolean beep;


    public TalkingClock(int interval, boolean beep) {
        this.beep = beep;
        this.interval = interval;
    }

    public void start() {
        var listener = new TimePrinter();
        var timer = new Timer(interval, listener);
        timer.start();
    }

    public class TimePrinter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("时间：" +
                    Instant.ofEpochMilli(e.getWhen()));

            //此处的beep是外围类的private字段，内部类可以访问自身的数据字段和创建它的外围类对象的数组字段。
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

}