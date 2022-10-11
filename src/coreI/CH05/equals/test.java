package coreI.CH05.equals;

import coreI.CH05.arrayList.Employee;

public class test {
    public static void main(String[] args) {
        /**
         * ==进行比较时，分数据类型而定
         *       1. 若比较双方为基本数据类型，比如int，则直接比较值
         *       2. 若比较双方是对象，则比较它们的在内存中的存储地址是否相同！
         */
        int a = 1;
        int b = 1;
        System.out.println(a==b);

        /**
         * String类中的equals方法已经经过重写：
         *       1. 如果是引用同一个对象，即存储地址相同，则返回true
         *       2. 如果两个对象虽然存储地址不同，但是它们的值相同，也返回true
         *       3. 否则返回false
         */
        String s1 = "hello";
        String s2 = "hello";
        String s3 = s2;
        System.out.println(s1 == s2);
        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
        System.out.println(s1.equals(s2));
        System.out.println(s2.equals(s3));

        /**
         * 一般自己的定义的类，如果没有人为重写equals，那么equals和==的作用是一样的！
         * ==是比较两个对象在内存中存放的地址，而hashCode方法计算值正是通过对象的存储地址得出的！
         */


        Employee e1=new Employee("wlq",655, 1969, 3, 6);
        Employee e2=new Employee("wlq",655, 1969, 3, 6);
        Employee e3 = e2;
        System.out.println(e1.hashCode());
        System.out.println(e2.hashCode());
        System.out.println(e3.hashCode());
        System.out.println(e1.equals(e2));
        System.out.println(e1==e2);
        System.out.println(e2==e3);

    }


}
