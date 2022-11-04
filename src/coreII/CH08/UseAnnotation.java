package coreII.CH08;


/**
 * @author happytsing
 */
public class UseAnnotation {

    @People("uestc")
    public void sayHello(String people){
        System.out.println(people + " say hello to you");
    }
}
