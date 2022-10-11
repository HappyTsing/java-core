package coreI.CH05.wrapper;

import java.util.ArrayList;

/**
 * 对象包装器与自动装箱
 *  包装器共有七类：
 *      前六类派生于公共的超类Number：Integer、Long、Float、Double、Short、Byte、Character
 *      第七类：Boolean
 */
public class Wrapper {
    public static void main(String[] args) {

        //泛型数组列表的<>之中必须填入对象类型，不可以是基本类型，于是我们使用int的包装器：Integer类
        var list = new ArrayList<Integer>();

        //自动装箱，list.add(2);会自动装箱为list.add(Integer.valueOf(3));
        list.add(2);
        list.add(Integer.valueOf(3));

        //自动拆箱，list.get(0);会自动拆箱为list.get(1).intValue();
        int n = list.get(0);
        int m = list.get(1).intValue();

        //包装类的另一个作用就是存放一些基本方法：如parseInt将字符串转化为数字，这是一个静态方法！Integer类是放置这个方法的好地方！
        String num = "100";
        int x = Integer.parseInt(num);
        int sum = n + m + x;
        System.out.println(sum);

    }
}
