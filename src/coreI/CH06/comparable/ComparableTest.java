package coreI.CH06.comparable;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ComparableTest {
    public static void main(String[] args) {
        Student[] students = new Student[3];
        students[0] = new Student("b",12);
        students[1] = new Student("a",11);
        students[2] = new Student("c",13);
        System.out.println("------------------排序前-------------------");

        for(Student s:students){
            System.out.println(s);
        }
        Arrays.sort(students);
        System.out.println("------------------排序后-------------------");
        for(Student s:students){
            System.out.println(s);
        }

    }
}

/**
 * comparable<T>接口，某对象实现comparable<T>接口，并重写其中的compareTo()方法。
 * 然后调用Arrays.sort()排序
 */
class Student implements Comparable<Student>{
    int age;
    String name;
    public Student(String name,int age){
        this.age = age;
        this.name = name;
    }

    @Override
    public int compareTo(Student anotherStudent) {
        return anotherStudent.age - this.age; //按年纪倒序
    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
