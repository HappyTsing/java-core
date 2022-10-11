package coreI.CH04;

import java.util.Random;

/**
 * 重载构造器
 * 用this()调用另一个构造器
 * 无参数构造器
 * 对象初始化块
 * 静态初始化块
 * 实例字段初始化
 * @author QingHappy
 */
public class Constructor {
    public static void main(String[] args) {
        var school = new Student[3];
        school[0] = new Student("Tsing", 100);
        school[1] = new Student(95);
        school[2] = new Student();

        for (Student s : school) {
            System.out.println(s.toString());
        }

    }

}

class Student {
    private static int nextId;
    private int id;
    private int grage;
    /**
     * 初始化数据字段方法一
     * @param name 显示字段初始化为小王
     */
    private String name = "小王";


    /**
     * 初始化字段方法二
     * 使用初始化块机制，当构造这个类的对象时，这些初始化块就会被执行！
     *   1. 对象初始化块：每次调用构造函数时都会运行
     *   2. 静态初始化块：只有在类第一次加载的时候运行
     */
    static
    {
//        System.out.println("静态初始化块运行");
        var generator = new Random();
        nextId = generator.nextInt(10000);
    }

    {
//        System.out.println("对象初始化块运行");
        id = nextId;
        nextId++;
    }

    /**
     * 初始化数据字段方法三
     * 使用有参构造器，在构造器中赋值
     */
    public Student(String n, int g) {
        name = n;
        grage = g;
    }

    /**
     *
     * 使用this()另一个构造器
     */
    public Student(int g) {
        this("Student#" + nextId, g);
    }

    /**
     * 初始化字段默认方法
     * 无参构造，此时默认：
     *      1.数值为0
     *      2.布尔值为false
     *      3.对象引用为null，String也为null
     */
    public Student() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGrage() {
        return grage;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", grage=" + grage +
                '}';
    }
}
