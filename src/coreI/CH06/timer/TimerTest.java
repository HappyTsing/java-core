package coreI.CH06.timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

public class TimerTest {
    public static void main(String[] args) {
        var listener = new TimePrinter();

        var timer = new Timer(1000, listener);
        timer.start();

        JOptionPane.showMessageDialog(null, "退出？");
        System.exit(0);
    }

}

class TimePrinter implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("现在的时间是" +
                Instant.ofEpochMilli(e.getWhen()));
        Toolkit.getDefaultToolkit().beep();
    }
}
