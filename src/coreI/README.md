# Java 核心卷 Ⅰ 代码重现

近日重读 Java 核心卷 Ⅰ，打算手撸课本上的所有代码以加深对 Java 基础的理解，现在就开始！

## CH02 Java 程序设计环境

### Welcome.java

经典入门代码，打印 hello！

## CH03 Java 的基本程序设计结构

### FirstSample.java

java 中三种注释的使用

### InputTest.java

标准输入流 System.in 和 Scanner 对象实现读取输入

### Retirement.java

三种循环：while dowhile for 的简单使用
格式化输出 printf

## CH04 对象与类

### Calendar.java

实现了使用 LocalDate 类实现一个日历的输出！

知识点：两个用于显示时间的类：Date 和 LocalDate

- Date 类

  用于保存时间点，显示年月日的方法已被废弃！

- LocalDate 类
  一般用于日历表示法表示日期
  该对象构造时，不使用构造器，而是使用静态工厂方法
  - LocalDate.now()
  - LocalDate.of(year,month,day)

### EmployeeTest.java

面向对象初探，实现了一个 Employee 对象类，使用一个 Employee 数组，再使用 for each 循环对每个 Employee 对象操作，同时在代码中体现了隐式参数的使用！

### StaticTest.java

**静态字段：**

1. 一个类维护一个静态字段，每个类的实例共享同一个静态字段。而非静态字段，每个实例自己独有一个副本
2. 即便没有对象实例，静态字段也存在！

**静态常量：**

静态变量使用较少，静态常量比较常用，使用 public static final 定义一个静态常量。

如在 Math 类中定义一个静态常量 PI

```java
public class Math
{
    public static final double PI=3.14159265358;
}
```

于是我们可以直接通过 Math.PI 访问这个常量。

注：System.out 就是一个静态常量，它的声明如下：

```java
public class System
{
    public static final PrintStream out = ...
}
```

**静态方法：**

静态方法即为没有隐式参数的方法，它不是在对象上执行的方法，我们直接用类名.方法名调用！
如：`Math.pow()`

使用静态方法的两种情况：

1. 方法不需要访问对象状态，因为它所需要的所有参数都通过显式参数提供。
2. 方法只需要访问类的静态字段

main 方法是一个静态方法，它不对任何对象进行操作，事实上，在启动程序时还没有对象，静态的 main 方法将执行并构造程序所需要的方法！

### Param.java

java 中所有参数，包括对象参数，都是按值调用的。
但是在 C 语言中，可以使用&符号进行引用传递
当我们传一个对象时，其实传的是对象引用的副本

```java
harry = new Employee();
function(harry){};
//此时传入方法的其实是一个对象引用的副本，它和harry都指向同一个实例对象！
```

### Constructor.java

展示了为数据字段赋值的三种方法：

1.  在声明中赋值
2.  在构造器中设置值：可以在一个构造器中使用`this.()`来调用另一个构造器函数
3.  初始化块
    - 静态初始化块：只有在类加载的时候初始化一次。
    - 对象初始化块：每次调用构造函数都会运行。

默认数据初始化为：

- 数值：0
- 布尔值：false
- 对象引用为 null，String 也为 null

### 类设计技巧

1. 保证数据私有，不要破坏封装性，即类中的字段都应该为 private，然后通过 set/get 方法去访问
2. 一定要对数据进行初始化，不要依赖于默认的数据初始化！
3. 不要在类中使用过多的基本类型，即要学会使用对象来替换基本类型！
4. 不是所有的字段都需要 set/get 方法，比如有些字段，如出生日期，不需要 set 方法，因为一旦初始化，不会再改变！
5. 分解有过多职责的类，即如果一个复杂的类可以分解为两个更加简单的类，那么就应该那么做！
6. 类名和方法名要能够体现它们的职责
7. 优先使用不可变的类

## CH05 继承

### inheritance

继承初体验！

**多态**：一个**对象变量**可以指示多种实际类型的现象称为多态

【例】一个 Employee 类型的变量既可以引用一个 Employee 类型的对象，也可以引用 Employee 类的任何一个子类的对象。

```java
Manager boss = new Manager();
Employee[] staff = new Employee[2]
staff[0] = boss;
```

注：此时编译器只把 staff[0]看作是一个 Employee 对象，故 Manager 独有的方法无法使用！

**在程序运行前，也就是编译时期 JVM 就能够确定方法由谁调用，这种机制称为静态绑定，反之则为动态绑定！**

**动态绑定**：在运行时能自动地选择适当的方法，称为动态绑定

1.  存在一个方法 f(int)和方法 f(String)，那么当调用 x.f(”hello“)的时候，编译器会挑选 f(String)方法，当调用 x.f(1)时，编译器会挑选 f(int)方法
2.  如果在子类中定义了一个与超类签名相同的方法，那么子类中的这个方法就会**覆盖**超类中这个相同签名的方法。
3.  使用动态绑定的原因是调用的方法依赖于**隐式参数**的实际类型！假设存在调用 x.f()因为可调用方法的范围为：x 类的方法、x 类的超类的方法！本次调用的隐式参数的实际类型可能是 x 类，也可能是 x 类的超类

    由于第二点，即重写覆盖，因此查找方法的顺序为：先找 x 类有无所需的方法（根据第一点，即方法签名），若无，再查找超类的方法（可能存在超类的方法，但是子类并没有重写）。

**静态绑定**：方法为 private、static、final 或者构造器，则编译器可以准确的指导应该调用哪种个方法，此时为静态绑定！

- private 方法：只能被当前类使用，其子类也无法访问，因此当我们使用 x.f()，其中 f()为 private 方法时，它一定是当前类的方法！即 x 和 f()都能确定！
- static 方法属于类：它是没有隐式参数的方法（核心卷 ⅠP116）
- final 方法：该方法不能被子类覆盖重写，故可以确定 x 和 f()
- 构造器

**super 与 this**

迄今为止，super 和 this 都分别有两种作用：

- super 一是调用超类的方法，二是调用超类的构造器
- this 一是指示隐式参数的引用，二是调用该类的其他构造器

当然，也可以使用 this 来调用当前类的方法，只不过可以省略，因此没必要使用 this！

**instanceof**

①obj instanceof Class

用于检测左边的对象是否是右边的类的实例:

1. 子类对象一定是父类的实例
2. 父类对象不是子类的实例

②obj instanceof Comparable

用于检测左边的对象是否是实现了右边的接口的类的实例

### abstractClasses

抽象类的简单体验：

1. 有一个或多个抽象方法的类本身必然是抽象的
2. 不含抽象方法，也可以将类声明为抽象类
3. 抽象类不能实例化

可以使用抽象类的变量去引用具体子类的变量

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

### equals

Obejcts：所有类的超类

- 可以使用 Object 类型的变量引用任何类型的对象
- Object 类中常用于覆盖的方法
  - equals()
  - toString()
  - hashCode() 根据对象的**存储地址**来生成对应的散列码
- equals 与==的探究

==的作用根据数据类型而有所变化

1. 若比较双方为基本数据类型，比如 int，则直接比较值
2. 若比较双方是对象，则比较它们的在内存中的存储地址是否相同！

equals 只用于对象，用于比较内存中的存储地址是否相同！

```java
//Object类中实现的equals，本质上就是==
public boolean equals(Object obj) {
    return (this == obj);
}
```

若是我们自己的类不对 equals()方法进行重写，那么当我们调用诸如 e1.equals(e2)时，实际上就是在进行 e1==e2！

当然，有些官方实现的类已经对 equals 进行了重写，此处以 String 类为例：

```java
public boolean equals(Object anObject) {
    //同Object类，若是两个对象引用相同，即它们在内存中的存储地址相同，则返回true
    if (this == anObject) {
        return true;
    }
    //若是两个String对象的值相同，也返回true！
    //注意：两个分别new出来的对象的存储地址一定是不同的！可以通过hashCode函数查看！
    if (anObject instanceof String) {
        String aString = (String)anObject;
        if (!COMPACT_STRINGS || this.coder == aString.coder) {
            return StringLatin1.equals(value, aString.value);
        }
    }
    return false;
}

```

注意：当我们使用 System.out.println(e)

假设 e 是一个对象的引用

那么实际上会自动调用 System.out.println(e.toString)

如果显示诸如：CH05.arrayList.Employee@4e50df2e

其原因是因为 e 对象的类的实现中，没有覆盖 toString 方法，而使用了默认的 Object 类的 toString 方法！

### arrayList

C 语言中，必须在编译时确定数组的大小

即只能够：int a[2]

不允许： int n; int a[n];

而 java 允许在声明数组时使用变量：int n = 3; var staff = new Employee[n];

从此实现了运行时确定数组的大小！

然而存在一个问题：一旦确定了数组的大小，就不能再改变了！

**解决运行时动态更改数组：使用 ArrayList 类**

泛型数组列表的声明：

```java
//以下三种写法等价！
ArrayList<Employee> staff = new ArrayList<Employee>();
//使用var关键字，避免重复写类名
var staff1 = new ArrayList<Employee>();
//菱形语法，可省略右侧类型参数
ArrayList<Employee> staff2 = new ArrayList<>();
```

泛型数组列表的常用方法：add、set、get、remove、size

若是已经能够估计数组的大小，可以使用 ensureCapacity 方法

例如：staff.ensureCapacity(100)，当然只是表示可能存 100 个，你可以少于，也可以超过，都没问题！

### wrapper

对象包装器与自动装箱

包装器共有八类：Integer、Long、Short、Float、Double、Byte、Character、Boolean

前六类派生于公共的超类 Number

作用一：用于一些基本类型无法使用，必须传入对象的情况：如泛型数组类型

```java
var list = new ArrayList<Integer>();
```

作用二：作为存放一些基本方法的好地方

```java
String s = "100";
int x = Integer.parseInt(s);
//此处的parseInt()方法是静态方法，与Integer对象没有任何关系，Integer类是放置这个方法的好地方。
```

基本方法是**静态方法**

### varargs

参数数量可变的方法

方法的最后一个参数设置为：类型... 数组名

例如：

**double... values**

相当于接受一个 double[]数组：var values = double[]

**Object... args**

相当于接收一个 Object[]数组，表示可以接收任意数量的对象，如果**输入是整形或者其他基本类型**，那么会**自动装箱**为对象！

传入该参数的效果和传入`Object args[]`效果相同

### Enum

可以为枚举类型增加构造器、方法和字段

```java
enum Size
{
    //SMALL的缩写（abbreviation）是S
    SMALL("S"), MEDIUM("M"), LARGE("L"), EXTRA_LARGE("XL");

    //构造器
    private Size(String abbreviation) { this.abbreviation = abbreviation; }

    //方法：得到缩写
    public String getAbbreviation() { return abbreviation; }

    //字段：用于保存缩写
    private String abbreviation;
}

```

枚举类的三个常用方法：

- toString：返回枚举常量名

```java
//以下两句等价
System.out.println("size.toString="+size.toString());
System.out.println("size=" + size);
```

- valueOf：toString 方法的逆方法,将一个枚举变量设置为枚举常量元素

```java
Size s=Enum.valueOf(Size.class ,"SMALL");
```

- values：静态方法，返回一个包含全部枚举值的数组

```java
var values = Size.values();
for (int i = 0; i < 4; i++) {
    System.out.println(values[i].getAbbreviation());
}
```

### 反射

反射的内容十分之多，暂且也用不到，决定先跳过！

2020.10.13 更新

能够分析类能力的程序称为反射，主要用于：

- 在运行时分析类的能力
- 在运行时检查对象，例如，编写一个适用于所有类的 toString 方法
- 实现泛型数组操作代码
- 利用 Method 对象，得到目标类的私有方法等

**Class 类**

Java 运行时系统始终为所有对象维护一个运行时类型标记，这个信息会跟踪每个对象所属的类。可以用一个特殊的 Java 类访问这些信息，保存这些信息的类名为 Class。

**获取 Class 类的三种方法**

Ⅰ. 已知具体类，直接通过 TargetClass.class 获得 Class 对象

```java
Class cl = Random.class;
Class cl = Employee.class;
```

Ⅱ. Object 类中的 getClass()方法，该方法返回一个 Class 类型的实例。instance.getClass()

```java
Employee e = new Employee();
Class cl = e.getClass();
```

Ⅲ. Class 类中的静态方法 forName()，获得类名对应的 Class 对象

```java
String className = "java.util.Random";
Class cl = Class.forName(className);
```

**利用反射分析类**

`java.lang.reflect`包中由三个类：Field、Method、Constructor，分别用于描述类的字段、方法、构造器。

**Ⅰ. Field 字段**

- getType() 返回字段类型，Class 类型接收

- Modifier.isPublic/isPrivate/isFinal/isStatic() 静态方法，判断字段是否是 Public 等，其参数是 f.getModifier()

**Ⅱ. Method 方法**

- getReturnType() 返回方法返回值类型，Class 类型接收

- getParameterType() 返回方法的参数类型，是一个数组，使用 Class[]接收

**Ⅲ. Constructor 构造器**

- getParameterType() 返回构造器的参数类型，是一个数组，使用 Class[]接收

**Ⅳ. 通用方法**

- getName() 获得字段、方法、构造器的名字

- getModifier() 返回一个整数，用不同的 0/1 位描述所使用的修饰符，通过 Modifier.toString 静态方法将修饰符转化为字符串

**Ⅴ. 有何区别**

- getFields、getMethods、getConstructors：返回对应对象的数组，这些对象对应于这个类或其超类的公共字段、方法、构造器，即只包括 public，但是包括父类！

- getDeclaredFields、getDeclaredMethods、getDeclaredConstructors ：返回对应对象的数组，这些对象仅对应这个类的所有字段、方法、构造器。即既包括 public，也包括 private

**通过 Class 类实例化对象**

```java
Employee e1 = (Employee) cl.getConstructor().newInstance(); //构造无参对象
Employee e2 = (Employee)cl.getConstructor(int.class, String.class).newInstance(200, "职员"); //构造有参对象
```

**通过 Class 类使用方法**

```java
//Employee对象的方法
public final String publicMethodEmployee(String s) {
    System.out.println("This is Employee publicMethod:"+s);
    return s;
}

//main函数中使用反射
Class cl = Employee.class;
Employee e = (Employee) cl.getConstructor().newInstance();
Method m = cl.getDeclaredMethod("publicMethodEmployee", String.class);
String s = (String)m1.invoke(e,"This is a String");
```

1. 通过 getDeclaredMethod()或 getMethod()方法，得到 Method 方法 m

   参数一：方法名

   参数二：方法的参数的类，有几个参数，就写几个

2. 使用 invoke()调用该方法

   参数一：隐式参数，一般来说，我们调用 Employee 对象 e 的方法 getName(String s1,String s2)是这样的，e.getName(s)，此时 e 就是隐式参数，s1、s2 是显式参数

   参数二：显示参数

### 继承的设计技巧

1. **将公共操作和字段放在超类中**：如将 name 字段放在超类 Person 中，而不是子类 Employee 和 Student 类中
2. **不要使用受保护的字段**：① 子类集合是无限制的，任何人都能通过派生子类来访问 protected 实例字段，从而破坏了封装性；② 同一个包中的类都可访问 protected 字段，不管它们是否是这个类的子类
3. **使用继承实现”is-a“关系**：即 extend
4. **在覆盖方法时，不要改变预期的行为**
5. **使用多态，而不要使用类型信息**
6. **不要滥用反射**

## CH06 接口、lambda 表达式与内部类

### 一、接口

**comparator、interfaces、timer**

**一些知识点**

1. 接口中的所有方法都自动是 public 方法、所有字段都是`public static final`
2. 接口中绝对不会有实例字段，但可以提供多个**静态方法**（Java 8 之后），还可以使用`default`修饰符提供一个**默认方法**！
3. 不能使用`new`运算符实例化接口，但是可以声明接口的变量，该变量必须引用实现了该接口的类对象
4. 每个类只能有一个超类，但却可以实现多个接口，这就是有了抽象类还引入接口的原因
5. **超类优先**：超类提供了一个具体方法，接口的具有相同签名的默认方法会被忽略
6. **覆盖解决冲突**：继承的两个接口提供了两个相同签名的默认方法，为了解决冲突，必须重写方法以覆盖解决冲突！

**代码知识概括**

排序的两种方式：

1. **`Comparable<T>接口`**

   类可以实现`Comparable<T>`接口，实现接口中的`CompareTo()`方法，若有一个存储该类的数组，则调用`coreI.ToolsClass.Arrays.sort(数组名)`实现排序！

   **缺陷**：有时候我们需要对同一个类进行多种不同的排序，而每一个类只能实现一次`compareTo()`方法！

   况且，如 String 类，它自身实现了`comparable<String>`接口中的`compareTo()`方法，即根据字典顺序比较字符串，且我们是无法对该类进行修改的。

   那么如果我们想根据字符串长度进行比较该如何实现呢？这就需要引入`Comparator<T>`接口

2. **`Comparator<T>接口`**

   首先需要一个**比较器类**，该类实现`Comparator<T>`接口，实现接口中的`compare()`方法

   其次一个普通的对象类，假设该类为 String 类，现在有一个 String 类组成的数组

   调用`coreI.ToolsClass.Arrays(数组名,比较器)`实现排序

   其中比较器是比较器类的实例，可以通过`coreI.ToolsClass.Arrays(数组名,new 比较器类名())`实现排序

**接口与回调**：指定某个特定事件发生时应该采取的动作！详见 timer 文件夹下的代码！

**对象克隆**：Cloneable 接口，这个接口指示一个类提供了一个安全的 clone()方法

Object 提供了一个 clone()方法，是浅拷贝！

我们需要做的是实现深拷贝！

待续！

### 二、lambda 表达式

**lambda**

**简介**：lambda 表达式就是一段代码块，形式诸如`（参数类型1 参数名1，参数类型2 参数名2，...) -> { 表达式 }` 使用 lambda 表达式的主要目的是**延迟执行**。

**使用场景**：将一个<u>代码块传递到某个对象</u>，该代码块会在将来某个时间调用。但是java中想要传递代码段并不容易，必须要构造一个对象，<u>该对象的类需要有一个方法包含所需的代码</u>。因此引入了lambda！

延迟执行的原因有很多，例如：

- 在一个单独的线程中运行代码
- 多次运行代码
- 在算法的适当位置运行代码（例如，排序中的比较操作）
- 发生某种情况时执行代码（例如，点击了一个按钮，数据到达，等等）
- 只在必要时才运行代码（例如，对象为空时才创建对象）

**规则**：

1. 没有参数，仍需要提供()，即`() -> { 表达式 }`
2. 如果根据上下文可推导出参数类型，则可以省略参数类型，即`（参数名1，参数名2，...) -> { 表达式 }`
3. 如果只有一个参数，且参数类型可推导，则可以省略(),即`参数名 -> { 表达式 }`
4. 若是表达式只有一句，则可以省略{}

无需指定返回类型，lambda 的返回类型总是会由上下文推到得出！

**函数式接口**：**只有一个抽象方法的接口**，可以有其他非抽象方法，只要保证抽象方法只有一个即可！当需要这种接口的对象时，可以提供一个lambda表达式。例如：Comparator接口。

**lambda 表达式所能做的只是转化为函数式接口**，`java.util.function`中定义了许多**通用**的函数式接口，例如`BiFunction<T, U, R>`，其描述了参数类型为T和U而且返回类型为R的函数，我们可以把一些 lambda 表达式保存在这些接口类型的变量中：

```java
BiFunction<String,String,Integer> comp = (first,second) -> first.length() - second.length();
```

不过这并没有什么意义，真正有意义的是类似Comparator这种具有特定用途的接口：

- Comparator: 排序

- Predicate: 该接口用于判断某个东西是否满足某种条件。ArrayList 类中的 removeIf 方法，其参数是`java.util.function`中的接口 Predicate，该接口专门用来传递lambda表达式，例如，下面的语句将从一个数组列表中删除所有null值：

  ```java
  //如果数组列表中存在null值，则删除！
  list.removeIf(e - > e==null);
  ```

- Supplier: 供应者（supplier）没有参数，调用时会生成一个T类型的值。供应者用于实现**懒计算**。例如，考虑如下调用：

  ```java
  // LocalDate d = LocalDate.of(2022,11,1);
  LocalDate d = null;
  
  // 方法一: 当预计d很少为null时，该方法并非最优解，我们希望只有在必要时才构造默认的LocalDate。
  LocalDate day = Objects.requireNonNullElse(d,LocalDate.of(2022,11,2));
  
  // 方法二：使用Supplier实现懒计算，只有d为null时，才会调用供应者。
  // public static <T> T requireNonNullElseGet(T obj, Supplier<? extends T> supplier)
  LocalDate day = Objects.requireNonNullElseGet(d,()->LocalDate.of(2022,11,2));
  ```

**方法引用**

使用`::`运算符分隔方法名与对象或类名，主要有三种情况：

- `object::instanceMethod`：方法引用等价于向方法传递参数的lambda表达式，下述二者等价：

  ```java
  System.out::println
  x -> System.out.println(x)
  ```

- `Class::instanceMethod`：第1个参数会成为方法的隐式参数，下述二者等价：

  ```java
  String::compareToIgnoreCase
  (x,y) -> x.compareToIgnoreCase(y)
  ```

- `Class::staticMethod`：所有参数传递到静态方法，下述二者等价：

  ```java
  Math::pow
  (x,y) -> Math.pow(x,y)
  ```

注意：只用当lambda表达式的体只调用一个方法而不做其他操作时，才能把lambda表达式重写为方法引用。

例如：`s -> s.length() == 0`，这里存在方法调用，但还有一个比较，因此此处不能使用方法调用。

如果有多个同名的重载方法，编译器就会尝试从上下文中找出你指的是哪一个方法。

有时API包含一些专门用作方法引用的方法，例如Objects类有一个方法isNull，用于测试对象引用是否为null。乍看之下没啥用，因为测试`obj == null`的可读性远大于`Objects.isNull(obj)`。

不过可以把方法引用传递到任何有Predicate参数的方法，下述二者等价：

```java
list.removeIf(Objects::isNull)
list.removeIf(o -> o == null);
```

可以在方法中使用`this`参数，下述二者等价：

```
this::equals
x -> this.equals(x)
```

同理，使用`super`也是合理的：`super::instanceMethod`

**构造器引用**

构造器引用与方法引用类似，不过方法名为new，例如：`Person::new`

参考代码：`CH06/lambda/ConstructorRefer`

**处理lambda表达式**

常用函数式接口：P253

### 三、内部类

**InnerClass、anonymousInnerClass、staticInnerClass**

**简介**：内部类是定义在另一个类中的类，可以对同一个包中的其它类隐藏、且可以访问其外围类的数据，包括私有的数据。

**内部类的特殊语法规则**：

1. 在外围类的作用域之外，可以通过`OuterClass.InnerClass`来引用内部类。
2. 内部类不仅能访问外围类的字段，还可以访问局部变量。当前仅当该局部变量为**最终变量**，其原理是内部类会在局部变量消失之前，复制一份局部变量，该变量不会再改变，保存下来，因此需要局部变量是最终变量。

**局部内部类**：当一个内部类仅仅在某一个方法内使用，那么，可以在一个方法内局部的定义这个类。

**局部内部类的规则**

1. 声明局部类时不能有访问说明符，即 public 或 private
2. 局部内对外部世界完全隐藏，除了其所在的方法，其余任何地方都不知道它的存在，哪怕是其方法所在的类也不知道局部内部类的存在。

**匿名内部类**：局部内部类的进一步简化，当你使用内部类只是为了创建这个类的一个对象时，**不需要为类指定名字**！

```java
//匿名内部类语法
new SuperType（construction parameters)
{
    inner class methods and data
}
```

`SuperType`可以是接口，那么内部类就要实现这个接口

```java
// ActionListener是一个接口，这一整句话的意思是：创建一个类的新对象，这个类实现了该接口！
var listener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("时间：" + Instant.ofEpochMilli(e.getWhen()));
        if (beep) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
};
```

`SuperType`也可以是一个类，那么内部类就要扩展这个类

**匿名内部类的规则**

1. 匿名内部类没有构造器，因为构造器与类名相同， 匿名内部类无类名
2. 匿名内部类的习惯用法是实现**事件监听器和其他回调**，不过如今**lambda 表达式**有更好的表现！

**匿名内部类和构造类对象的比较**

如果构造参数列表的结束小括号后面跟一个开始大括号，就是在定义匿名内部类。

```java
//构造Person类的新对象
var queen = new Person("Marry");

//构造扩展了Person类的匿名内部类的对象
var prince = new Person("Jack"){
    inner class methods and data
}
```

**匿名内部类和 lambda 表达式的比较**

前文已经提及，如今实现事件监听器和其他回调时，lambda 会更简洁！

```java
//使用匿名内部类实现start方法
public void start(int interval,boolean beep) {

    var listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("时间：" + Instant.ofEpochMilli(e.getWhen()));
            if (beep) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    };
    var timer = new Timer(interval, listener);
    timer.start();
}
```

```java
//使用lambda实现start方法
public void start(int interval,boolean beep) {

    var timer = new Timer(interval,e -> {
        System.out.println("时间：" + Instant.ofEpochMilli(e.getWhen()));
        if (beep) {
            Toolkit.getDefaultToolkit().beep();
        }
    });
    timer.start();
}
```

**静态内部类**：有时候为了把一个类隐藏在另外一个类的内部，并不需要内部类有外围类对象的一个引用，为此可以将**内部类声明为 static**，这样就不会生成那个引用。

**静态内部类的规则**

1. 当且仅当内部类不需要访问外围类对象时，才使用静态内部类！
2. 静态内部类可以有静态字段和方法，而非 static 的内部类，在外部类加载的时候，并不会加载它，所以它里面不能有静态变量或者静态方法。

详细使用参见代码：`StaticInnerClass.java`

### 代理

**proxy、proxyInnerClass、proxyLambda、proxylambda2**

分别为：代理的普通实现、内部类实现、lambda 表达式实现及代理对象类实现多个接口的情况！

代理分为两种：静态代理和动态代理

**静态代理**是我们手动创建一个类，该类是对你所代理类的扩展，它除了有所代理类的功能，还会有一些额外功能。

比如房屋中介，它代理了卖方，除了卖方的卖房子的功能，还有自己的扩展功能：收取中介费用！从此我们不再需要联系卖方，直接和代理中介交互即可！

**静态代理的实现步骤**

1. 定义一个接口及其实现类；
2. 创建一个代理类同样实现这个接口
3. 将**目标对象注注入进代理类**，然后在代理类的对应方法调用目标类中的对应方法。这样的话，我们就可以通过代理类屏蔽对目标对象的访问，并且可以在目标方法执行前后做一些自己想做的事情。

**静态代理的缺陷**：

1. 每一个接口（类）都需要自己的代理类，且代理类和被代理类有着大量的重复代码！
2. 我们对目标对象的每个方法的增强都是手动完成的，非常不灵活（比如接口一旦新增加方法，目标对象和代理对象都要进行修改）且麻烦(需要对每个目标类都单独写一个代理类)。
3. 上面我们是从实现和应用角度来说的静态代理，从 JVM 层面来说， 静态代理在编译时就将接口、实现类、代理类这些都变成了一个个实际的 class 文件。

**动态代理**，基于反射原理实现使用一个代理类完成全部的代理功能！

- `CH06.dynamicProxy`：动态代理的各种实现方法，如基础方法、内部类、lambda表达式
- `CH06.dynamicVsStaticProxy`：静态代理的实现及其缺陷，以及从静态代理升级到动态代理。

详见：`CH06.dynamicProxy.proxy.TraceHandler.java`

1. 被代理对象作为参数`target`传入
2. 通过`employee.getClass().getClassLoader()`获取`ClassLoader`对象
3. 通过`employee.getClass().getInterfaces()`获取它实现的所有接口
4. 然后将`target`包装到实现了`InvocationHandler`接口的**调用处理器**`TraceHandler`对象中
5. 通过`newProxyInstance`函数我们就获得了参数对象`target`的动态代理对象`proxy`
6. 使用`((接口类型 proxy)).function()`，调用被代理类实现的多个接口中的指定接口的指定方法！同时，在调用该方法的同时，还会实现`invoke`中的代理增强方法，实现代理！

显然，**调用处理器 TraceHandler 是可以复用的**，当我们传入不同类型的被代理对象类，这个类实现了一个或者多个接口，由于不清楚传入的类的类型是什么，因此也不知道它实现了什么接口！

我们需要做的，仅仅是将被代理类作为参数`target`传入调用处理器`TraceHandler`即可！于是每次使用该对象时，都会调用代理的特有功能，即`InvocationHandler`接口的方法`invoke()`

注：代理类实在程序运行过程中动态创建的，然而，一旦被创建，就变成常规类，与其它类无任何区别！

## CH07 异常、断言、日志

### 一、异常

```shell
Throwable
    --Error

    --Exception
         --IOException
         --RuntimeException
```

Error：Java 运行时系统的内部错误和资源耗尽错误

RuntimeException：

- 错误的强制类型转换
- 数组访问越界
- 访问 null 指针

**派生于 Error 类或 RuntimeException 类的所有异常称为`非检查型异常`，所有其它的异常称为`检查型异常`**

**处理异常的两种方式**：抛出和捕获

**1. 抛出异常**

关键字：throws、throw

原则：一个方法必须声明所有可能抛出的`检查型异常`，`非检查型异常`要么在你的控制之外（Error），要么是一开始就应该避免（RuntimeException）

注意：子类抛出的异常必须比父类抛出的异常更特殊，父类不抛出异常，则子类也不能抛出异常

例如：父类抛出 Exception，那么子类只能抛出诸如 IOException，而不能抛出 Throwable 类型的异常

**2. 捕获异常**

关键字：try、catch、finally

注意：捕获异常会花费大量的时间，因此使用异常的基本规则是，只在异常情况下使用异常。

### 二、断言

已知异常的捕获检查会降低程序运行速度，且大量的检查代码会最终保留在程序中，造成混乱。

断言机制允许在测式期间向代码中插入一些检查，而在生产代码中会**自动删除**这些检查。

关键字：assert

语法：

- `assert condition;`
- `assert condition : expression;`

上述两个语句都会计算 condition 条件，若结果为 false，则抛出一个 AssertionError

第二个语句的 expression 是一个字符串，当 condition 为 false 时，会抛出 AssertionError：expression 的内容

**如何在 idea 中启动和禁用断言**

默认情况下断言是**禁用**的！

1. Edit configurations，进入 debug configuration
2. 在 VM options 中输入-ea 或者-enableassertions 启用，输入-da 或者-disableassertions

### 三、日志

书上的代码过于复杂，未完待续！

日志内容输出在`System.err`上，对比`System.err.println`和`System.out.println`可以发现，err 是红色字，而 out 是白色字。

日志的主要功能是帮助观察程序行为，一般我们会在程序中使用`System.out.println`来帮助我们直观的显示，然而弊端是后期需要删除所有的 print 语句，这无疑是麻烦的，于是引入日志功能来解决该问题！

一般直接使用这句即可：`Logger.getGlobal().info();`

也可以创建自己的日志再输出：

`Logger myLogger = Logger.getLogger("myLogger");`

`myLogger.info();`

## CH08 泛型程序设计

## CH09 集合

集合分为两类：继承了 Collection 接口和 Map 接口，其中继承了 Collection 接口的分为 List 和 Set

**List、Set、Map 的区别**

List(对付顺序的好帮手)： 存储的元素是有序的、可重复的。

Set(注重独一无二的性质): 存储的元素是无序的、不可重复的。

Map(用 Key 来搜索的专家): 使用键值对（kye-value）存储，Key 是无序的、不可重复的，value 是无序的、可重复的，每个键最多映射到一个值。

**集合的选用**

- 键值对，则使用 Map
  - 若需要排序，则 treeMap
  - 不需要排序，则 HashMap
  - 保证线程安全，则 ConcurrentHashMap
- 只存放元素值，则使用 Collection
  - 保证元素唯一，则使用实现 Set 接口的集合，如 TreeSet、HashSet
  - 元素可重复，则选择实现 List 接口，如 ArrayList、LinkList

**Iterator 迭代器**
用于遍历集合

## CH12 并发

## coreI.ToolsClass 工具类 Collections 和 Arrays

### Collections

#### 一、排序

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

#### 二、查找、替换

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

#### 三、 同步控制

不推荐，需要线程安全的集合类型时请考虑使用 JUC 包下的并发集合

#### 四、示例

一、`Collections.nCopies(n,var)`

```java
List<String> list3 = new ArrayList<String>(Collections.nCopies(2, "orange"));
```

是通过nCopies方法创建拥有2个“orange”值的List，提供给list3进行初始化。

### Arrays

排序 : sort()

查找 : binarySearch()

比较: equals()

填充 : fill()

转列表: asList()

转字符串 : toString()

复制: copyOf()

#### 示例

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

## 重要概念

### equals 与 ==

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

### hashCode 与 equals

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

### 方法的签名：重载与重写

方法名 + 参数类型 = 方法的签名

注：返回类型不是方法的签名！

主要用于重载，重写和重载的区别：

- 重载：相同方法名的方法，根据输入不同的参数，做出不同的处理。发生在编译器！
  - 返回值类型可变，参数列表一定要变。异常和访问修饰符都可以变。
- 重写：重写发生在运行期，是子类对父类的允许访问的方法的实现过程进行重新编写：
  - 返回值类型、方法名、参数列表必须相同，抛出的异常范围小于等于父类，访问修饰符范围大于等于父类
  - 如果父类方法访问修饰符为private/final/static。则子类不能重写该方法，但是被static修饰的方法能够再次被声明。
  - 构造方法无法被重写。

### 成员变量和局部变量

- 从语法形式上看，成员变量是属于类的，可以被`public/protected/private/static/final等`修饰符修饰；而局部变量是属于方法的，且只能被`final`修饰。
- 从变量在内存中的存储方式看：如果成员变量是使用static修饰的，那么这个成员变量是属于类的，如果没有使用static修饰，这个成员变量是属于实例的。
- 从变量在内存中的生存世家您看：成员变量是对象的一部分，随着对象的创建而存在，而局部变量随着方法的调用而存在，方法的结束而消失。
- 成员变量如果没有被赋初始值，则会以类型的默认值而赋值（final例外，final修饰的成员变量必须显式赋值），而局部变量则不会自动赋值。

### 接口和抽象类

#### 接口

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

#### 抽象类

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
