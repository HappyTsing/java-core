package coreI.CH09.map;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author QingHappy
 */
public class MapTest {
    public static void main(String[] args) {
        var nums = new HashMap<Integer, String>();
        nums.put(1, "one");
        nums.put(2, "two");
        nums.put(3, "three");
        nums.put(4, "four");



        System.out.println(nums);

        nums.remove(2);
        Logger.getGlobal().info(nums.toString());

        nums.put(5, "five");
        Logger.getGlobal().info(nums.toString());

        System.out.println(nums.get(3));

        nums.forEach((k,v)-> System.out.println("key="+k+", value="+v));


    }
}
