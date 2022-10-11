package coreI.CH05.abstractClasses;

public class PersonTest {
    public static void main(String[] args) {
        var people = new Person[2];
        people[0] = new Employee("王乐卿", 50000, 2000, 03, 30);
        people[1] = new Student("宋雨童", "外国语学院");

        /**
         * 由于抽象类不能实例化，因此变量P永远不会引用Person对象！而是引用诸如Student、Employee具体子类的对象！
         */
        for (Person p : people) {
            System.out.println(p.getName() + "," + p.getDescription());
        }
    }
}
