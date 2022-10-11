package coreI.CH06.staticInnerClass;

/**
 * 静态内部类的使用
 * 此处使用静态内部类的原因是为了隐藏Pair类
 * 为什么要隐藏Pair类？
 *          因为Pair是一个十分大众化的名字，别的程序员也使用，可能会产生冲突，于是我们在外卖呢嵌套一个ArrayAlg类
 *          ArrayAlg类内部没有任何东西，仅有一个静态内部类Pair，我们可以通过ArrayAlg.Pair来进行访问！
 *
 * 注：当且仅当内部类不需要访问外围类对象时，才使用静态内部类！
 *    静态内部类可以有静态字段和方法，而非static的内部类，在外部类加载的时候，并不会加载它，所以它里面不能有静态变量或者静态方法。
 *
 * @author QingHappy
 */
public class StaticInnerClass {
    public static void main(String[] args) {

        //构造一个double数组，随机填充20个数字
        var values = new double[20];
        for (int i = 0; i < values.length; i++) {
            values[i] = 100 * Math.random();
        }

        ArrayAlg.Pair p = ArrayAlg.Pair.minmax(values);
        System.out.println("min = " +
                p.getFirst());
        System.out.println("max = " +
                p.getSecond());
    }
}
class ArrayAlg{
    public static class Pair{
        private double first;
        private double second;

        public Pair(double f, double s) {
            first = f;
            second = s;
        }

        public double getFirst() {
            return first;
        }

        public double getSecond() {
            return second;
        }

        public static Pair minmax(double[] values) {
            /**
             * @param min 设置为正无穷，那么第一次判断min>v时一定会成立！
             * @param max 负无穷
             */
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (double v : values) {
                if (min > v) {
                    min = v;
                }
                if (max < v) {
                    max = v;
                }
            }
            return new Pair(min, max);
        }
    }

}