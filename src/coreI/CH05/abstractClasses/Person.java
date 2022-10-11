package coreI.CH05.abstractClasses;

/**
 * 抽象类：
 *    1. 有一个或多个抽象方法的类本身必然是抽象的
 *    2. 不含抽象方法，也可以将类声明为抽象类
 *    3. 抽象类不能实例化
 */
public abstract class Person {
    public abstract String getDescription();


    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
