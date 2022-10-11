package coreI.CH06.anonymousInnerClass;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

/**
 * 匿名内部类与lambda表达式的对比
 * @author QingHappy
 */
public class AnonymousInnerClassTestLambda {

    public static void main(String[] args) {

        var clock = new TalkingClockLambda();
        clock.start(1000, true);
        JOptionPane.showMessageDialog(null, "quit?");
        System.exit(0);
    }
}

class TalkingClockLambda {
    private int interval;
    private boolean beep;


    public void start(int interval,boolean beep) {

        var timer = new Timer(interval,e -> {
            System.out.println("时间：" + Instant.ofEpochMilli(e.getWhen()));
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        });
        timer.start();

/*
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
 */


    }


}

