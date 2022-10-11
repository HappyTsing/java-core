package coreI.CH05.inheritance;

/**
 * 继承的具体实现
 * super关键字的两种用法
 */
public class Manager extends Employee
{
   private double bonus;

   /**
    * @param name the employee's name
    * @param salary the salary
    * @param year the hire year
    * @param month the hire month
    * @param day the hire day
    */
   public Manager(String name, double salary, int year, int month, int day)
   {
      //super含义一：调用超类中带有对应参数的构造器
      super(name, salary, year, month, day);
      bonus = 0;
   }

   @Override
   public double getSalary()
   {
      //super含义二：调用中带有对应参数的方法
      System.out.print("这里是父类的getSalary：");
      double baseSalary = super.getSalary();
      return baseSalary + bonus;
   }

   public void setBonus(double b)
   {
      bonus = b;
   }
}
