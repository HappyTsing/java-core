package coreI.CH05.arrayList;

import java.time.LocalDate;

public class Employee
{
   private String name;
   private double salary;
   private LocalDate hireDay;

   public Employee(String name, double salary, int year, int month, int day)
   {
      this.name = name;
      this.salary = salary;
      hireDay = LocalDate.of(year, month, day);
   }

   public String getName()
   {
      return name;
   }

   public double getSalary()
   {
      System.out.print("这里是子类的getSalary：");
      return salary;
   }

   public LocalDate getHireDay()
   {
      return hireDay;
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
