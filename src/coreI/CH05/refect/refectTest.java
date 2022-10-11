package coreI.CH05.refect;

import java.lang.reflect.*;

public class refectTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Employee e = new Employee(100,"主管");

        /**
         *    获取Class类的三种方式
         *          1.Class类中的静态方法forName()，获得类名对应的Class对象
         *          2.已知具体类，直接通过TargetClass.class获得Class对象
         *          3.Object类中的getClass()方法，该方法返回一个Class类型的实例。instance.getClass()
         */

        Class cl = Class.forName("coreI.CH05.refect.Employee");
        Class cl2 = Employee.class;
        Class cl3 = e.getClass();

        //通过Class类实例化对象
        Employee e1 = (Employee) cl.getConstructor().newInstance(); //构造无参对象
        Employee e2 = (Employee) cl.getConstructor(int.class, String.class).newInstance(200, "职员"); //构造有参对象

        /**
         * 通过Class类调用任意方法
         *   通过getDeclaredMethod()或getMethod()方法，得到Method方法m
         *                  参数一：方法名
         *                  参数二：方法的参数的类，有几个参数，就写几个
         *   使用invoke()调用该方法
         *                  参数一：隐式参数，一般来说，我们调用Employee对象e的方法getName(String s1,String s2)是这样的，e.getName(s)，此时e就是隐式参数，s1、s2是显式参数
         *                  参数二：显示参数
         */
        Method m1 = cl.getDeclaredMethod("publicMethodEmployee", String.class);
        String s = (String)m1.invoke(e,"sss");


        /**
         * 利用反射分析类
         *     一、 Field       字段
         *              1.getType() 返回字段类型，Class类型接收
         *              2.Modifier.isPublic/isPrivate/isFinal/isStatic() 静态方法，判断字段是否是Public等，其参数是f.getModifier()
         *
         *     二、 Method      方法
         *              1.getReturnType()     返回方法返回值类型，Class类型接收
         *              2.getParameterType()  返回方法的参数类型，是一个数组，使用Class[]接收
         *
         *     三、 Constructor 构造器
         *              1.getParameterType()   返回构造器的参数类型，是一个数组，使用Class[]接收
         *
         *     通用方法
         *              1.getName()         获得字段、方法、构造器的名字
         *              2.getModifier()     返回一个整数，用不同的0/1位描述所使用的修饰符，通过Modifier.toString静态方法将修饰符转化为字符串
         *
         *     有何区别：
         *      getFields、getMethods、getConstructors：返回对应对象的数组，这些对象对应于这个类或其超累的公共字段、方法、构造器，即只包括public，但是包括父类！
         *      getDeclaredFields、getDeclaredMethods、getDeclaredConstructors  ：返回对应对象的数组，这些对象仅对应这个类的所有字段、方法、构造器。即既包括public，也包括private
         *
         *
         */


        //1. Field
        System.out.println("****************Field测式**********************");
        Field[] fields1 = cl.getFields();
        Field[] fields2 = cl.getDeclaredFields();
        for (Field f : fields2) {

            Class type = f.getType();
            String name = f.getName();
            String modifiers = Modifier.toString(f.getModifiers());
            Boolean bool = Modifier.isPublic(f.getModifiers());

            System.out.println("Modifier.isPublic:"+bool);
            System.out.println(modifiers);
            System.out.println("f.getName:"+name);
            System.out.println("f.getType:"+type);
            System.out.println("-----------------------------------");
        }

        //2. Method
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
        System.out.println("****************Method测式**********************");

        Method[] methods1 = cl.getMethods();
        Method[] methods2 = cl.getDeclaredMethods();
        for (Method m : methods2) {
            Class retType = m.getReturnType();
            Class[] paramType = m.getParameterTypes();
            String name = m.getName();
            String modifiers = Modifier.toString(m.getModifiers());


            System.out.println("modifiers："+modifiers);
            System.out.println("m.getName:"+name);
            System.out.println("m.getType:"+retType);
            System.out.print("m.getParameterTypes:");
            for (int i = 0; i < paramType.length; i++) {
                System.out.print(paramType[i]+"" );
                if (i < paramType.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
            System.out.println("-----------------------------------");
        }

        //3. Constructor
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
        System.out.println("****************Constructor测式**********************");
        Constructor[] constructors1 = cl.getConstructors();
        Constructor[] constructors2 = cl.getDeclaredConstructors();

        for (Constructor c : constructors2) {
            String name = c.getName();
            String modifiers = Modifier.toString(c.getModifiers());
            Class[] paramType = c.getParameterTypes();

            System.out.println("modifiers："+modifiers);
            System.out.println("m.getName:"+name);
            System.out.print("m.getParameterTypes:");
            for (int i = 0; i < paramType.length; i++) {
                System.out.print(paramType[i]+"" );
                if (i < paramType.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.println();
        }


    }
}
