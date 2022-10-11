package coreI.CH05.refect;

/**
 * @author QingHappy
 */
public class Employee extends People{
    private int money;
    public String position;

    public Employee(int m,String p) {
        super();
        money = m;
        position = p;
    }

    public Employee() {
        super();
        money = 300;
        position = "老板";
    }

    public final String publicMethodEmployee(String s) {
        System.out.println("This is Employee publicMethod:"+s);
        return s;
    }

    private void privateMethodEmployee() {
        System.out.println("This is Employee privateMethod");
    }
}
