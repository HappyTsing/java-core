# 工具类 Collections 和 Arrays

## Collections

### 一、排序

```java
//反转
void reverse(List list)

//随机排序
void shuffle(List list)

//按自然排序的升序排序
void sort(List list)

//定制排序，由Comparator控制排序逻辑
void sort(List list, Comparator c)

//交换两个索引位置的元素
void swap(List list, int i , int j)

//旋转: 当distance为正数时，将list的后distance个元素整体移到前面。
//       distance为负数时，将list的前distance个元素整体移到后面。
void rotate(List list, int distance)
```

### 二、查找、替换

```java
//对List进行二分查找，返回索引，注意List必须是有序的
int binarySearch(List list, Object key)

//根据元素的自然顺序，返回最大的元素。 类比int min(Collection coll)
int max(Collection coll)

//根据定制排序，返回最大元素，排序规则由Comparatator类控制。类比int min(Collection coll, Comparator c)
int max(Collection coll, Comparator c)

//用指定的元素代替指定list中的所有元素。
void fill(List list, Object obj)

//统计元素出现次数
int frequency(Collection c, Object o)

//统计target在list中第一次出现的索引，找不到则返回-1，类比int lastIndexOfSubList(List source, list target).
int indexOfSubList(List list, List target)

//用新元素替换旧元素
boolean replaceAll(List list, Object oldVal, Object newVal)
```

### 三、 同步控制

不推荐，需要线程安全的集合类型时请考虑使用 JUC 包下的并发集合

### 四、示例

一、`Collections.nCopies(n,var)`

```java
List<String> list3 = new ArrayList<String>(Collections.nCopies(2, "orange"));
```

是通过nCopies方法创建拥有2个“orange”值的List，提供给list3进行初始化。

## Arrays

排序 : sort()

查找 : binarySearch()

比较: equals()

填充 : fill()

转列表: asList()

转字符串 : toString()

复制: copyOf()

### 示例

**一、`Arrays.asList()`**

在LeetCode 15题解中出现的用法：`List<List<Integer>>`

首先`List<Integer>`指的是存int类型数据的列表，`List<List<Integer>>`指的是存【int类型数据的列表】类型数据的列表

如何为这种List添加元素？

```java
List<List<Integer>> outList = new ArrayList<>();

int a=1,b=2,c=3;
//用法一 先构造List，再res.add(List)
List<Integer> list = new ArrayList<>();
list.add(a);
list.add(b);
list.add(c);
outList.add(list)

//用法二 ans.add(Arrays.asList(元素1，元素2，...,元素n))
outList.add(Arrays.asList(a,b,c));
```

**二、`Arrays.fill(数组名,val)`**

使用val填充数组

# 重要概念

## equals 与 ==

```java
import java.util.*;
public class test1 {
    public static void main(String[] args) {
        String a = "a"; // 放在常量池中
        String b = "a"; // 从常量池中找
        String c = new String("a"); // c为一个引用
        System.out.println(a==b); //true
        System.out.println(a.equals(b)); //true
        System.out.println(c==a); //false
        System.out.println(c.equals(a)); //true
    }
}
```

String类重写了equals方法，比较的是值是否相同，如果没有重写，那equals方法与==相同！

```java
// Object类中的原始equals方法
public boolean equals(Object obj) {
        return (this == obj);
}
```

```java
// String类中重写的equals方法
public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String aString = (String)anObject;
            if (coder() == aString.coder()) {
                return isLatin1() ? StringLatin1.equals(value, aString.value)
                                  : StringUTF16.equals(value, aString.value);
            }
        }
        return false;
}
```

## hashCode 与 equals

> 以HashSet为例！

1. 为什么要有hashCode？

   当加入HashSet时，分为两步：

   - 计算当前对象的hashcode，如果在原set中不存在相同hashcode的数据，则将该数据加入set，否则：
   - 调用equals方法检查hashcode相等的对象是否真的相同。如果equals返回true，则不加入，否则说明这两个数据并不相同，那么加入set。

   由此可知，hashcode相同时，equals未必相同！而equals返回true时，hashcode一定相同！

   因此hashCode的作用主要是减少equals方法的调用次数，以此增加运行效率！

2. 重写equals必须重写hashCode，为什么？

   从上文可知，如果`a.equals(b) = true`，那么他们的hashCode也应该是相同的。如果不重写hashCode方法，那么即便equals返回true，那么hashcode也不相同，在加入HashSet时会重复加入！

3. 为什么两个对象有相同的hashcode值，但他们不一定相同？

   因为hashCode()使用的杂凑算法也许刚好会让多个对象返回相同的杂凑值。

```java
public class test1 {
    public static void main(String[] args) {
        Student s1 = new Student("lele");
        Student s2 = new Student("lele");
        System.out.println(s1 == s2); //false
        System.out.println(s1.equals(s2)); //重写equals前：false，重写后：true
        var set = new HashSet<Student>();
        set2.add(s1);
        set2.add(s2);
        System.out.println(set); 
        /*只重写equals：[Student{name=lele}, Student{name=lele}]
          只重写hashCode：[Student{name=lele}, Student{name=lele}]
		  同时重写equals、hashCode：[Student{name=lele}]
		*/
    }
}
class Student{
    String name;
    public Student(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name=" + name +
                '}';
    }
	
    // idea自动生成的重写的equals和hashCode方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
```

如上可以获得，只有同时重写equals方法和hashCode方法，才能将两个属性相同的对象正确加入HashSet，原因是：

- 加入HashSet先调用HashCode，如果HashCode相同：
- 再调用equals，如果equals相同，才会判定为重复数据，不加入HashSet！
- 一旦其中有一步（HashCode、equals）不同，就被认为是不同的数据，加入HashSet

如上，便是为什么修改了equals一定要hashCode的原因！

## 方法的签名：重载与重写

方法名 + 参数类型 = 方法的签名

注：返回类型不是方法的签名！

主要用于重载，重写和重载的区别：

- 重载：相同方法名的方法，根据输入不同的参数，做出不同的处理。发生在编译器！
  - 返回值类型可变，参数列表一定要变。异常和访问修饰符都可以变。
- 重写：重写发生在运行期，是子类对父类的允许访问的方法的实现过程进行重新编写：
  - 返回值类型、方法名、参数列表必须相同，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类
  - 如果父类方法访问修饰符为private/final/static。则子类不能重写该方法，但是被static修饰的方法能够再次被声明。
  - 构造方法无法被重写。

## 成员变量和局部变量

- 从语法形式上看，成员变量是属于类的，可以被`public/protected/private/static/final等`修饰符修饰；而局部变量是属于方法的，且只能被`final`修饰。
- 从变量在内存中的存储方式看：如果成员变量是使用static修饰的，那么这个成员变量是属于类的，如果没有使用static修饰，这个成员变量是属于实例的。
- 从变量在内存中的生存世家您看：成员变量是对象的一部分，随着对象的创建而存在，而局部变量随着方法的调用而存在，方法的结束而消失。
- 成员变量如果没有被赋初始值，则会以类型的默认值而赋值（final例外，final修饰的成员变量必须显式赋值），而局部变量则不会自动赋值。

## 接口和抽象类

### 接口

1. 接口中的所有`方法`都自动是`public`方法、所有`字段`都是`public static final`
2. 接口中绝对不会有实例字段，但可以提供多个**静态方法**（Java 8之后），还可以使用`default`修饰符提供**默认方法**！
3. 不能使用`new`运算符实例化接口，但是可以声明接口的变量，该变量必须引用实现了该接口的类对象
4. 每个类只能有一个超类，但却可以实现多个接口，这就是有了抽象类还引入接口的原因
5. **超类优先**：超类提供了一个具体方法，接口的具有相同签名的默认方法会被忽略
6. **覆盖解决冲突**：继承的两个接口提供了两个相同签名的默认方法，为了解决冲突，必须重写方法以覆盖解决冲突！

```java
public interface People {
    int age = 18; // 为什么必须要赋值，因为默认是public static final,而final修饰的成员变量必须显式赋值！
//    public static final int age = 18;
    
    // 默认方法
    default String getName(){
        return "HappyTsing";
    }
    // 静态方法
    static int getAgeStatic(){
        return age;
    }
}
```

### 抽象类

1. 有一个或多个抽象方法的类本身必然是抽象的
2. 不含抽象方法，也可以将类声明为抽象类
3. 抽象类不能实例化
4. 可以使用抽象类的变量去引用具体子类的变量

```java
public abstract class Person {
    public abstract String getDescription();
    private String name;
    public Person(String name) {
        this.name = name;
    }
    public String getName() {
        return name; 
    }
}
```

```java
public class Student extends Person {
    private String major;
    public Student(String name, String major) {
        super(name);
        this.major = major;
    }
    @Override
    public String getDescription() {
        return "a student major in " +
                major;
    }

}
```

```java
var people = new Person[2];
people[0] = new Employee("王乐卿", 50000, 2000, 03, 30);
people[1] = new Student("宋雨童", "外国语学院");

/**
 * 由于抽象类不能实例化，因此变量P永远不会引用Person对象！而是引用诸如Student、Employee具体子类的对象！
 */
for (Person p : people) {
    System.out.println(p.getName() + "," + p.getDescription());
}
```

# spi
Java 中的 SPI 机制就是在每次类加载的时候会先去找到 class 相对目录下的 META-INF 文件夹下的 services 文件夹下的文件，将这个文件夹下面的所有文件先加载到内存中，然后根据这些文件的文件名和里面的文件内容找到相应接口的具体实现类，找到实现类后就可以通过反射去生成对应的对象，保存在一个 list 列表里面，所以可以通过迭代或者遍历的方式拿到对应的实例对象，生成不同的实现。

所以会提出一些规范要求：文件名一定要是接口的全类名，然后里面的内容一定要是实现类的全类名，实现类可以有多个，直接换行就好了，多个实现类的时候，会一个一个的迭代加载。

接下来同样将 service-provider 项目打包成 jar 包，这个 jar 包就是服务提供方的实现。通常我们导入 maven 的 pom 依赖就有点类似这种，只不过我们现在没有将这个 jar 包发布到 maven 公共仓库中，所以在需要使用的地方只能手动的添加到项目中。
参考：https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/basis/spi.md