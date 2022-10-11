package coreI.CH05.varargs;

/**
 * 参数数量可变的方法
 * 方法的最后一个参数设置为：类型... 数组名
 * 例如：double... values
 *      Object... args
 */
public class Varargs {
    public static void main(String[] args) {
        System.out.println(sum(2.5,3.6,3.45,25));
    }

    public static double sum(double... values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum;
    }
}
