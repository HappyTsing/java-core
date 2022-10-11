package coreI.CH05.equals;

/**
 * Objects类的常用于覆盖的三个方法
 *             1. equals()
 *             2. toString()
 *             3. hashcode()
 */
public class EqualsTest
{
   public static void main(String[] args)
   {
      var alice1 = new Employee("Alice Adams", 75000, 1987, 12, 15);
      var alice2 = alice1;
      var alice3 = new Employee("Alice Adams", 75000, 1987, 12, 15);
      var bob = new Employee("Bob Brandson", 50000, 1989, 10, 1);
      System.out.println("-----equal--------------------------------------------------------------------------------------");
      System.out.println("alice1 == alice2: " + (alice1 == alice2));
      System.out.println("alice1 == alice3: " + (alice1 == alice3));

      System.out.println("alice1.equals(alice2): " + alice1.equals(alice2));
      System.out.println("alice1.equals(alice3): " + alice1.equals(alice3));

      System.out.println("alice1.equals(bob): " + alice1.equals(bob));

      System.out.println("-----toString--------------------------------------------------------------------------------------");
      System.out.println("bob.toString(): " + bob);

      var carl = new Manager("Carl Cracker", 80000, 1987, 12, 15);
      var boss = new Manager("Carl Cracker", 80000, 1987, 12, 15);

      boss.setBonus(5000);

      System.out.println("boss.toString(): " + boss);
      System.out.println("carl.toString(): " + carl);
      System.out.println("------equal-------------------------------------------------------------------------------------");

      System.out.println("carl.equals(boss): " + carl.equals(boss));

      System.out.println("-----hashCode--------------------------------------------------------------------------------------");

      System.out.println("alice1.hashCode(): " + alice1.hashCode());
      System.out.println("alice3.hashCode(): " + alice3.hashCode());
      System.out.println("bob.hashCode(): " + bob.hashCode());
      System.out.println("carl.hashCode(): " + carl.hashCode());
   }
}
