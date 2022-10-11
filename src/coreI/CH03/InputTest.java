package coreI.CH03;

import java.util.Scanner;

/**
 * 使用标准输出流System.in读取输入
 * @author QingHappy
 */
public class InputTest {
    public static void main(String[] args) {
        //构造一个与标准输入流System.in关联的Scanner对象
        Scanner in = new Scanner(System.in);
        //读取输入
        System.out.println("please input your name");
        /**
         * 当你输入in. 会跳出很多很多的方法，其中有：
         *              in.nextLine    读取一行
         *              in.next        读取一个单词（空格为分隔符）
         *              in.nextInt     读取一个整数
         *              in.nextDouble  读取一个浮点数或整数
         * 还有：
         *              in.hasNext       检测输入中是否还有其他单词
         *              in.hasNextInt    检测输入中是否还有整数
         *              in.hasNextDouble 检测输入中是否还有整数或者浮点数
         *
         */
        String name = in.nextLine();
        System.out.println(name);

    }
}
