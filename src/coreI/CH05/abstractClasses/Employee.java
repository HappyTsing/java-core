package coreI.CH05.abstractClasses;

import java.time.LocalDate;

/**
 * 继承了抽象类的类如果不是抽象类，那么它必须实现抽象父类的抽象方法！
 */
public class Employee extends Person{
    private double salary;
    private LocalDate hireDay;

    public Employee(String n, double s, int year, int month, int day) {
        super(n);
        salary = s;
        hireDay = LocalDate.of(year, month, day);
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDay() {
        return hireDay;
    }

    @Override
    public String getDescription() {
        return String.format("an employee with a salary of $%.2f", salary);
    }

    public void raiseSalary(double byPercent) {
        double rasie = salary * byPercent / 100;
        salary = rasie + salary;
    }
}
