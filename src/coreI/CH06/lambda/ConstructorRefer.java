package coreI.CH06.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 构造器引用
 */
public class ConstructorRefer {
    public static void main(String[] args) {
        ArrayList<String> names  = new ArrayList<String>(){{
            add("wlq");
            add("syt");
        }};
        Stream<Person> stream = names.stream().map(Person::new);
        List<Person> people = stream.collect(Collectors.toList());
        System.out.println(people);

    }
    public static class Person{
        String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
