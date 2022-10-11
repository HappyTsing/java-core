package coreI.CH04;

/**
 * 本例的Manager类有一个静态字段staticId，初始值为1，以后每调用一个实例对象的setId方法，先将staticId的值赋值给对象的id字段，然后自增。
 * 可以得出结论：静态字段是属于类的，一个类共同享有一个静态字段，而非静态字段是属于实例对象的，每个对象独自占有一个副本！
 * @author QingHappy
 */
public class StaticTest {
    public static void main(String[] args) {

        Manager[] staff1 = new Manager[3];
        var staff = new Manager[3];
        staff[0] = new Manager("jack");
        staff[1] = new Manager("david");
        staff[2] = new Manager("tom");
        System.out.println("-----静态字段测式-------");
        for (Manager e : staff) {
            e.setId();
            System.out.println(e.toString());
        }
        System.out.println("-----静态方法测式-------");
        Manager.doubleNum(3);
        System.out.println("StaticId:"+Manager.getStaticId());
        //在调用静态方法时，建议直接使用类名来引用！
    }


}

class Manager {
    private String name;
    private int id;
    private static int staticId=1;


    public Manager(String n) {
        name = n;
        id = 0;
    }

    /**
     * 使用静态方法的两种情况：
     *          1. 方法不需要访问对象状态，所需要的参数全部由显示参数提供，如方法doubleNum
     *          2. 方法只需要访问类的静态字段，如getStaticId
     */
    public static void doubleNum(int i) {
        System.out.println("doubleNum:"+2*i);
    }

    public static int getStaticId() {
        return staticId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = staticId;
        staticId++;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
