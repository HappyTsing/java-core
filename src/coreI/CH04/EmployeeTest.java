package coreI.CH04;

import java.time.LocalDate;

/**
 * 面向对象初体验！
 * 隐式参数和显式参数的概念
 * @author QingHappy
 */
public class EmployeeTest {
    public static void main(String[] args) {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("Carl Cracker", 75000, 1987, 12, 15);
        staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
        staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);
        for (Employee employee : staff) {
            //薪水提高5%
            employee.raiseSalary(5);
            //此处employee是隐式参数，5是显示参数，我们可以在raiseSalary中使用this来代指隐式参数！
        }
        for (Employee employee : staff) {
            System.out.println(employee.toString());
        }

    }


}
class Employee {
    private String name;
    private double salary;
    private LocalDate hireDay;

    public Employee(String n, double s, int year, int month, int day) {
        name = n;
        salary = s;
        hireDay = LocalDate.of(year, month, day);
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    public void raiseSalary(double byPercent) {
        //此处的this代指隐式参数，我们使用employee.raiseSalary(5)调用时，this代指employee，当然，此处也可以将this.省略！
        double raise=this.salary*byPercent/100;
        salary+=raise;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + salary +
                ", hireDay=" + hireDay +
                '}';
    }
}