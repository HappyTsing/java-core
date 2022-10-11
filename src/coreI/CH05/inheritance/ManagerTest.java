package coreI.CH05.inheritance;

/**
 * 继承初体验！
 * 多态
 * 动态绑定
 */
public class ManagerTest
{
   public static void main(String[] args)
   {
      // 构建一个Manager对象
      var boss = new Manager("Carl Cracker", 80000, 1987, 12, 15);
      boss.setBonus(5000);

      var staff = new Employee[4];

      /**
       * 使用Manager和Employee对象来填充staff数组
       * 注意：此处的Staff[n]是Employee类型的对象应用变量，但是却存放了一个Manager对象！
        */
      staff[0] = boss;
      /*
      boss.setBonus(100);  OK；
      staff[0].setBonus(100);  ERROR,因为staff[0]是Employee类型的引用，编译器只讲它看作一个Employee对象，故不存在setBonus这个方法！
       */
      staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
      staff[2] = new Employee("Tommy Tester", 40000, 1990, 3, 15);

      /*
      可以直接接受一个Manager类型的对象，但是编译器只讲它看作一个Employee对象，因此不存在setBonus方法
       */
      staff[3] = new Manager("Carl Cracker", 80000, 1987, 12, 15);



      /**
       * 此处的变量e，指示了多种实际类型的对象，这种现象称为多态。
       * 在运行时能够自动地选择适当的方法，称之为动态绑定！
       * 即存在一个方法f(int)和方法f(String)，那么当调用x.f(”hello“)的时候，编译器会挑选f(String)方法，
       * 当调用x.f(1)时，编译器会挑选f(int)方法，这就是动态绑定！
       */
      for (Employee e : staff) {
         System.out.println("name=" + e.getName() + ",salary=" + e.getSalary());
      }
      System.out.println("----------------------------------------------------------------------------------");
      System.out.println("最终结论：若是父类对象变量引用子类对象，在使用该变量的方法时，会使用子类对象重写过的方法！即子类方法会覆盖超类中相同签名的方法");
      System.out.println("方法签名：方法的名+参数列表");

      System.out.println("-----------------------instanceof 测式-----------------------------------------------------------");
      System.out.print("boss instanceof Employee:");
      System.out.println(boss instanceof Employee);
      System.out.print("boss instanceof Manager:");
      System.out.println(boss instanceof Manager);


      System.out.print("staff[2] instanceof Employee:");
      System.out.println(staff[2] instanceof Employee);
      System.out.print("staff[2] instanceof Manager:");
      System.out.println(staff[2] instanceof Manager);

      System.out.println("instanceof用于测试左边的对象是否是右边类的实例！子类对象一定是父类的实例，而父类对象不是子类的实例");

   }
}
