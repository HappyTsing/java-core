package coreI.CH06.lambda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 函数式接口
 * @author happytsing
 */
public class FunctionalInterface {
    public static void main(String[] args) {
        testPredicate();
        testSupplier();
    }

    /**
     * 函数式接口Predicate
     */
    public static void testPredicate(){
        List<String> ls = new ArrayList<String>(){{
            add(new String("test1"));
            add(new String("test2"));
            add(null);
        }};
        System.out.println(ls);

        ls.removeIf(e -> e == null);
        // 方法引用，效果相同。
        ls.removeIf(Objects::isNull);
        System.out.println(ls);
    }

    /**
     * 函数式接口Supplier<T>被调用时，会生成一个T类型的值，此处生成的是LocalDate类型的对象。Supplier用于懒计算。
     */
    public static void testSupplier(){
//        LocalDate d = LocalDate.of(2022,11,1);
        LocalDate d = null;

        // 方法一: 当预计d很少为null时，该方法并非最优解，我们希望只有在必要时才构造默认的LocalDate。
//        LocalDate day = Objects.requireNonNullElse(d,LocalDate.of(2022,11,2));

        // 方法二：使用Supplier实现懒计算，只有d为null时，才会调用供应者。
        // public static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier)
        LocalDate day = Objects.requireNonNullElseGet(d,()->LocalDate.of(2022,11,2));
        System.out.println(day);
    }
}
