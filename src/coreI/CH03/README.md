# String详解

## 获取方法

```java
//1. 获取字符串长度
int length()  
    
//2. 获取特定位置的字符char，把String想象成数组，index从0计数
char charAt(int index) 
    
//3. 获取从 fromIndex 位置开始查找指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
int indexOf(int ch)
int indexOf(int ch, int fromIndex)
int indexOf(String str)
int indexOf(String str, int fromIndex)

//4. 同上，不过从后往前找
int lastindexOf()
```

## 转换方法

```java
//5. 将字符串转换为字符串数组
char[] toCharArray() 
    
//6. 将字符串转为字节数组
byte[] getBytes()

/*英文举例*/   
String str ="abcd";
System.out.println(Arrays.toString(str.toCharArray()));
输出：[a, b, c, d]

System.out.println(Arrays.toString(str.getBytes()));
输出：[97, 98, 99, 100]

/*中文举例*/
String str ="国庆快乐"
System.out.println(Arrays.toString(str.toCharArray()));
输出：[国, 庆, 快, 乐]
    
System.out.println(Arrays.toString(str.getBytes()));
输出：[-27, -101, -67, -27, -70, -122, -27, -65, -85, -28, -71, -112]
```

## 判断方法

```JAVA
//7. 判断是否为空字符串
public boolean isEmpty() {

//8. 判断是否以字符串suffix结尾、以prefix开始  
public boolean endsWith(String suffix) 
public boolean startsWith(String prefix) 

//9. 判断是否存在指定字符
boolean contains(CharSequence s) 

//10. 判断是否相同，str1.equals(str2)
boolean equals(Object anObject)
 
//11. 判断忽略大小写后是否相同
boolean equalsIgnoreCase(String anotherString) 忽略大小写是否相等
```

## 其他方法

```java
//12. 字母小写、大写
String toLowerCase()
String toUpperCase() 
    
//13. 去前后空格
String trim()
    
//14. 重复某个字符串count次    
String repeat(int count)

//15. 取出从beginIndex到endIndex-1范围内的所有字符    
String substring(int beginIndex)
String substring(int beginIndex, int endIndex)
    
//16. 代替
String replace(char oldChar, char newChar)
```

## 底层知识

```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {

    @Stable
    private final byte[] value;
}
```

如图，String类是用一个`final`修饰的一个`byte[]`，因此`String`是一个常量，是不可变的！

![String](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/String.png)

如图：每次对String的操作都会生成新的String对象，这样不仅效率低下，而且大量浪费有限的内存空间。

初始String值为“hello”，然后在这个字符串后面加上新的字符串“world”，这个过程是需要重新在栈堆内存中开辟内存空间的，最终得到了“hello world”字符串也相应的需要开辟内存空间。

这样短短的两个字符串，却需要开辟三次内存空间，不得不说这是对内存空间的极大浪费。

为了应对经常性的字符串相关的操作，就需要使用Java提供的其他两个操作字符串的类

- StringBuffer类：线程安全，效率低
- StringBuild类：非线程安全，效率高

上述两个类都是继承了AbstractStringBuilder：

![AbstractStringBuilder继承](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/AbstractStringBuilder.png)

```java
abstract class AbstractStringBuilder implements Appendable, CharSequence {
    /**
     * The value is used for character storage.
     */
    byte[] value;
    
    /**
     * Creates an AbstractStringBuilder of the specified capacity.
     */
    AbstractStringBuilder(int capacity) {
        if (COMPACT_STRINGS) {
            value = new byte[capacity];
            coder = LATIN1;
        } else {
            value = StringUTF16.newBytesFor(capacity);
            coder = UTF16;
        }
    }
}
```

```java
public final class StringBuilder extends AbstractStringBuilder implements ...{
    /**
     * Constructs a string builder with no characters in it and an
     * initial capacity of 16 characters.
     */
    @HotSpotIntrinsicCandidate
    public StringBuilder() {
        super(16);
    }
}
```

```java
 public final class StringBuffer extends AbstractStringBuilder implements ...{
    /**
     * Constructs a string buffer with no characters in it and an
     * initial capacity of 16 characters.
     */
    @HotSpotIntrinsicCandidate
    public StringBuffer() {
        super(16);
    }
}
```

可以看到，AbstractStringBuilder中是用`byte[] value;`存储数据，因此是可变的！

而StringBuilder和StringBuffer继承了AbstractStringBuilder，也都没有使用final修饰符，所以这两种对象是可变的！且二者的构造方法也是直接调用父类AbstractStringBuilder的构造方法的。

**小结**：

- 如果要操作少量的数据用 String；
- 多线程操作字符串缓冲区下操作大量数据 StringBuffer；
- 单线程操作字符串缓冲区下操作大量数据 StringBuilder（推荐使用）。

```java
public class test2 {
    public static void main(String[] args) {

        StringBuilder sbd = new StringBuilder();
        sbd.append("wangleqing");
        sbd.append(2000);
        sbd.append(false);
        System.out.println(sbd);

        StringBuffer sbf = new StringBuffer();
        sbf.append("songyutong");
        sbf.append(2000);
        sbf.append(true);
        System.out.println(sbf);
    }
}

/*
wangleqing2000false
songyutong2000true
*/
```

# 精度丢失

Double类型的数据在做加减乘除时可能存在精度问题，使用BigDecimal类解决问题：

```java
public class Test {
    public static void main(String[] args)  {
        BigDecimal bigDecimal = new BigDecimal("0.1");
        BigDecimal bigDecimal2 = new BigDecimal("0.1");
        
        bigDecimal.add(bigDecimal2);       //加
        bigDecimal.subtract(bigDecimal2);  //减       
        bigDecimal.multiply(bigDecimal2);  //乘
        bigDecimal.divide(bigDecimal2);	   //除
    }
}
```

注意使用BigDecimal类的**BigDecimal(double val)构造函数时仍会存在精度丢失问题，建议使用BigDecimal(String val)**

例如： `BigDecimal bigDecimal = new BigDecimal(Double.toString(0.1));`

# 类型互转

## String转八大基础类型

八大基础类型：byte、short、int、long、float、double、char、boolean

包装类：Byte、Short、Interge、Long、Float、Double、Character、Boolean

方法一：调用对应包装类的静态方法parsexxx()：

```java
String s = "123";

byte b = Byte.parseByte(s);
short t = Short.parseShort(s);
int i = Integer.parseInt(s);
long l = Long.parseLong(s);
Float f = Float.parseFloat(s);
Double d = Double.parseDouble(s);
boolean bo = Boolean.parseBoolean(s);
char c = Character.parseCharacter(s);
```

方法二：通过调用包装类的静态方法valueOf：

```java
int i =Integer.valueOf(str).intValue();
```

由于该方法是返回一个Integer对象，因此我们需要将他拆箱成基本类型int

.intValue()；是手动拆箱，可以不加，因为会自动拆箱。

## 八大基本类型转换为String：

方法一：String.valueOf()

![valueOf](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/valueOf.png)

```java
//源码如下：先装箱成对象，然后在toString()
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}
public static String valueOf(int i) {
    return Integer.toString(i);
}
```



方法二：Object.toString()