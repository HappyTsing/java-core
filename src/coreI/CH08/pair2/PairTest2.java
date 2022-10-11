package coreI.CH08.pair2;

import java.time.LocalDate;


/**
 * @version 1.02 2015-06-21
 * @author Cay Horstmann
 */
public class PairTest2
{
    public static void main(String[] args)
    {
        LocalDate[] birthdays =
                {
                        LocalDate.of(1906, 12, 9), // G. Hopper
                        LocalDate.of(1815, 12, 10), // A. Lovelace
                        LocalDate.of(1903, 12, 3), // J. von Neumann
                        LocalDate.of(1910, 6, 22), // K. Zuse
                };

        Pair<LocalDate> mm = ArrayAlg.minmax(birthdays);
        System.out.println("min = " + mm.getFirst());
        System.out.println("max = " + mm.getSecond());
    }
}

class ArrayAlg
{
    /**
     *
     * 限定T只能是实现了Comparable接口的类
     *     此处只能使用 extend，无论是接口还是类，都是用它！
     *     多个限定：T extend Comparable&Serializable
     *     多个类型变量：T extend Comparable,E extend Serializable
     */
    public static <T extends Comparable> Pair<T> minmax(T[] a)
    {
        if (a == null || a.length == 0) {
            return null;
        }
        T min = a[0];
        T max = a[0];
        for (int i = 1; i < a.length; i++)
        {
            if (min.compareTo(a[i]) > 0) {
                min = a[i];
            }
            if (max.compareTo(a[i]) < 0) {
                max = a[i];
            }
        }
        return new Pair<>(min, max);
    }
}
