package coreII.CH01;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author happytsing
 */
public class ConvertStream {
    public static void main(String[] args) {
        System.out.println("------testFilter------");
        testFilter();
        System.out.println("------testMap------");
        testMap();
        System.out.println("------testTakeWhile------");
        testTakeWhile();
    }
    public static void testFilter(){
        Stream<String> words = Stream.of("just","a","test","happytsing","songyutong");
        words.filter(w -> w.length() > 3).forEach(System.out::println);
    }
    public static void testMap(){
        Stream<String> words = Stream.of("just","a","test","happytsing","songyutong");
        words.map(w -> w+"test_map").forEach(System.out::println);

    }

    /**
     * 当遇到第一个不满足条件的元素时，就会结束。而filter会对流中的所有元素进行比较和过滤。
     */
    public static void testTakeWhile(){
        Stream<String> words = Stream.of("just","a","test","happytsing","songyutong");
        words.takeWhile(w -> w.length() > 3).forEach(System.out::println);
    }
}
