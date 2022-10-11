package coreI.CH05.refect;

public class People {
    private String name;
    public String gender;
    private int age;

    public People() {
        name = "王乐卿";
        gender = "男";
        age = 11;
    }

    public void publicMethodPeople() {
        System.out.println("This is People publicMethod");
    }

    private void privateMethodPeople() {
        System.out.println("This is People privateMethod");
    }
}
