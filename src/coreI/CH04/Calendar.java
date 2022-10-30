package coreI.CH04;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

/**
 * {@code Calendar}中主要介绍:
 * Date类和LocalDate类的简要使用
 * 使用LocalDate类实现当月日历的输出
 * 
 * @author QingHappy
 */
public class Calendar {
    public static void main(String[] args) {
        // 不要使用构造器来构造LocalDate类的对象，应该使用静态工厂方法，它会代替你使用构造器：
        // LocalDate.now()语句会构造一个新对象，表示构造这个对象时的日期
        // 可以使用LocalDate.of(1999,2,31)构造特定日期的对象！
        LocalDate localDate = LocalDate.now();
        Date date = new Date();
        // 注：Date类的该方法已被废弃，尽量避免使用！
        System.out.println(date.getDate());
        System.out.println(localDate.getDayOfMonth());
        // Date类现在一般用于保存时间点，而LocalDate类用日历表示法表示日期！
        System.out.println(date.getTime());

        /**
         * 使用localdate类实现日历
         */
        int month = localDate.getMonthValue();
        int today = localDate.getDayOfMonth();
        // 获取本月的第一天的日期
        // 当前日期 - 已过去的天数（today - 1）
        localDate = localDate.minusDays(today - 1);
        System.out.println("本月第一天：" + localDate.toString());

        // 得到本月第一天是星期几，并将其转化为数字，比如周二=2
        DayOfWeek weekday = localDate.getDayOfWeek();
        int value = weekday.getValue();
        System.out.println("本月第一天是：" + weekday);

        // 打印日历首行
        System.out.println(" Mon Tue Wed Thu Fri Sat Sun");

        // 根据本月第几天进行缩进
        for (int i = 0; i < value; i++) {
            System.out.print("  ");
        }

        while (localDate.getMonthValue() == month) {
            System.out.printf("%3d", localDate.getDayOfMonth());

            // 当天则标*，否则不标
            if (localDate.getDayOfMonth() == today) {
                System.out.print("*");
            } else {
                System.out.print(" ");
            }
            // 天数+1
            localDate = localDate.plusDays(1);
            // 周一换行
            if (localDate.getDayOfWeek().getValue() == 1) {
                System.out.println();
            }
        }

        // 下一个月，换行
        if (localDate.getDayOfWeek().getValue() != 1) {
            System.out.println();
        }

    }
}
