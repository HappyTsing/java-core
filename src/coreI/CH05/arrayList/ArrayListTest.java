package coreI.CH05.arrayList;

import java.util.ArrayList;
import java.util.List;

public class ArrayListTest {
    public static void main(String[] args) {
        //以下三种写法等价！
        ArrayList<Employee> staff = new ArrayList<Employee>();
        //使用var关键字，避免重复写类名
        var staff1 = new ArrayList<Employee>();
        //菱形语法，可省略右侧类型参数
        ArrayList<Employee> staff2 = new ArrayList<>();

        System.out.println("---add--添加内容---------------------------------------------------");

        staff.add(new Employee("java", 99, 1999, 2, 15));
        staff.add(new Employee("python", 66, 2006, 6, 18));
        staff.add(new Employee("php", 33, 2014, 12, 26));


        for (Employee e :
                staff) {
            System.out.println(e);
        }

        System.out.println("---get--得到对应位置的内容--------------------------------------------");

        Employee emp = staff.get(1);
        System.out.println(emp);

        System.out.println("---set--会把对应位置覆盖---------------------------------------------");
        var temp = new Employee("basic", 11, 2019, 3, 30);
        staff.set(1,temp);
        for (Employee e :
                staff) {
            //相当于System.out.println(e.toString())
            System.out.println(e);
        }

        System.out.println("---remove--删除 可以用一个变量来接收-----------------------------------");
        Employee rme = staff.remove(1);
        System.out.println("删除的内容为: "+rme.toString());

        System.out.println("目前的数组内容：");
        for (Employee e :
                staff) {
            System.out.println(e);
        }
    }
}
