package coreI.CH07.loggerTest;


/**
 * @author QingHappy
 */
public class DebugTest {
    public static void main(String[] args) {
        int[] demo = new int[10];

        System.out.println("fuck you");
        for (int i = 0; i < demo.length-1; i++) {
            demo[i] = getDoubleInt(i);
            demo[i + 1] = getInt(100);
        }
        System.out.println("love you");

    }
    public static int getDoubleInt(int num) {
        int temp = num * 2;
        return temp;
    }
    public static int getInt(int num) {
        int temp = num * 2;
        return temp;
    }

}
