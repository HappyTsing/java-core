package coreI.CH06.comparator;

import java.util.Arrays;
import java.util.Comparator;

public class ComparatorTest {
    public static void main(String[] args) {
        var str = new String[3];
        str[0] = new String("aaaaa");
        str[1] = new String("abe");
        str[2] = new String("abcd");

        //使用comparable<T>接口，实现排序
        Arrays.sort(str);
        for (String s : str) {
            System.out.println(s);
        }

        //使用comparator<T>接口，实现排序，第二个参数是实现了comparator<T>接口的类的实例，我们称之为比较器！
        Arrays.sort(str, new LengthComparator());
        for (String s : str) {
            System.out.println(s);
        }

    }

}

/**
 * comparable<T>接口的缺陷：有时候我们需要对同一个类进行多种不同的排序，而每一个类只能实现一次compareTo方法！
 * 况且，如String类，它自身实现了comparable<String>接口中的compareTo方法，即根据字典顺序比较字符串，且我们是无法对该类进行修改的。
 * 那么如果我们想根据字符串长度进行比较该如何实现呢？
 * 于是引入Comparator<T>接口
 */
class LengthComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}
