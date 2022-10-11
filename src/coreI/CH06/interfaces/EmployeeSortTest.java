package coreI.CH06.interfaces;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author QingHappy
 */
public class EmployeeSortTest {
    public static void main(String[] args) {
        var staff = new Employee[3];
        staff[0] = new Employee("jack", 1000);
        staff[1] = new Employee("python", 4500);
        staff[2] = new Employee("php", 3000);

        //调用静态方法Arrays.sort()，该方法需要提供一个比较方式，即compareTo方法！
        //为了确保传入数组存在该方法，则数组的对象类必须实现Comparable接口！因为一旦实现该接口，必然实现了compareTo方法！
        Arrays.sort(staff);
        for (Employee e : staff) {
            System.out.println(e.toString());

        }

    }
}
