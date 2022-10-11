package coreI.CH05.Enum;

import java.util.*;

/**
 * 枚举类
 */
public class EnumTest
{
    public static void main(String[] args)
    {
        var in = new Scanner(System.in);
        System.out.print("Enter a size: (SMALL, MEDIUM, LARGE, EXTRA_LARGE) ");
        String input = in.next().toUpperCase();

        //valueOf把size变量设置为Size.input的内容，如Size.SMALL
        Size size = Enum.valueOf(Size.class, input);

        //以下两句等价
        System.out.println("size.toString="+size.toString());
        System.out.println("size=" + size);

        //得到size变量的内容的缩写！
        System.out.println("abbreviation=" + size.getAbbreviation());
        if (size == Size.EXTRA_LARGE)
            System.out.println("Good job--you paid attention to the yourself.");

        //静态方法values
        var values = Size.values();
        for (int i = 0; i < 4; i++) {
            System.out.println(values[i].getAbbreviation());
        }
    }
}

enum Size
{
    //SMALL的缩写（abbreviation）是S
    SMALL("S"), MEDIUM("M"), LARGE("L"), EXTRA_LARGE("XL");

    private Size(String abbreviation) { this.abbreviation = abbreviation; }
    public String getAbbreviation() { return abbreviation; }

    private String abbreviation;
}
