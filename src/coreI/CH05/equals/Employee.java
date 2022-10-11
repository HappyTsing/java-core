package coreI.CH05.equals;

import java.time.*;
import java.util.Objects;

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
      return salary;
   }

   public LocalDate getHireDay()
   {
      return hireDay;
   }

   public void raiseSalary(double byPercent)
   {
      double raise = salary * byPercent / 100;
      salary += raise;
   }

   @Override
   public boolean equals(Object otherObject)
   {
      // a quick test to see if the objects are identical
      // 返回true的情况一：如果两个对象的引用相同，那么一定相同。（即Object类中的equals的实现
      if (this == otherObject) {
         return true;
      }

      // must return false if the explicit parameter is null
      if (otherObject == null) {
         return false;
      }

      // if the classes don't match, they can't be equal
      if (getClass() != otherObject.getClass()) {
         return false;
      }

      // now we know otherObject is a non-null Employee
      var other = (Employee) otherObject;

      // test whether the fields have identical values
      //返回true的情况二：虽然引用了不同的对象，但是这两个不同的对象的值相同！
      return Objects.equals(name, other.name) 
         && salary == other.salary && Objects.equals(hireDay, other.hireDay);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(name, salary, hireDay); 
   }

   @Override
   public String toString()
   {
      return getClass().getName() + "[name=" + name + ",salary=" + salary + ",hireDay=" 
         + hireDay + "]";
   }
}
