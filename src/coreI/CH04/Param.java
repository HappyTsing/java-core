package coreI.CH04;

/**
 * 参数传递：java都是按值调用！
 * @author QingHappy
 */
public class Param {
    public static void main(String[] args) {
        /**
         * Test1 参数按值调用，方法得到的是所有参数值的一个副本
         */
        System.out.println("------参数数值按值调用---------");
        int num = 10;
        System.out.println("num 初始值:" + num);
        doubleValue(num);
        System.out.println("num 经过doubleValue后的值：" + num);

        /**
         * Test2 参数若为对象，也是按值调用，方法得到的是一个对象的引用的副本！
         * 虽然在swap函数中，我们将两个对象变量引用的实例进行了转换，但是原本的a和b所引用的对象实例是没有改变的
         * 结论：传入的只是对象的引用变量的副本！
         */
        System.out.println("------参数对象按值调用---------");
        var a = new Employee("Alice", 70000, 1800, 2, 5);
        var b = new Employee("Bob", 65000, 2000, 3, 16);
        System.out.println("Before a is:" +
                a.getName());
        System.out.println("Before b is:" +
                b.getName());
        swap(a, b);
        System.out.println("After a is:" +
                a.getName());
        System.out.println("After b is:" +
                b.getName());


        System.out.println("------改变引用的对象的属性---------");
        /**
         * Test3 虽然对象的引用变量无法改变，但是由于副本引用变量和原本的引用变量是指向同一个对象的
         * 因此副本对对象的操作，是可以保存的！
         */
        var c = new Employee("Cat", 65000, 2000, 3, 16);
        System.out.println("Before salary:" + c.getSalary());
        doubleValue(c);
        System.out.println("After salary:" + c.getSalary());

    }

    public static void doubleValue(double x) {
        x = 2 * x;
        System.out.println("In doubleValue x now is：" + x);
    }

    public static void doubleValue(Employee x) {
        x.raiseSalary(100);
        System.out.println("In doubleValue salary now is：" + x.getSalary());
    }

    public static void swap(Employee x, Employee y) {
        Employee temp = x;
        x = y;
        y = temp;
        System.out.println("In swap x name=" + x.getName());
        System.out.println("In swap y name=" + y.getName());
    }

}

