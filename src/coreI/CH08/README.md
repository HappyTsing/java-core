# 泛型

## 为什么要使用泛型？

泛型程序设计意味着编写的**代码可以对多种不同类型的对象重用**。

## 从ArrayList说起

ArrayList是一个有**类型参数**的**泛型类**。为了指定数组列表保存的元素对象的类型，需要使用一对尖括号将类名括起来追加到ArrayList后面，例如：

```java
ArrayList<Student>
```

### 为什么要使用ArrayList

在传统c/c++语言中，必须在**编译**时就确定整个数组的大小，例如：

```c
int buf[100];
```

这必然会导致存储空间的浪费，而这一问题在java中得到了一定的解决，即java允许在**运行时**确定数组的大小，例如：

```java
int[] buf;  //声明数组
int[] buf = new int[size];  //初始化数组，size是一个int类型的值  
```

然而，这段代码并没有解决**运行时动态更改数组**的问题，因为一旦创建了数组，就不能再改变它的长度！

解决该问题的方法便是使用ArrayList：

> ArrayList类类似于数组，但在添加或删除原宿时，它能够自动的调整数组容量，而不需要为此编写任何代码。

### 如何使用ArrayList

声明和构造一个保存Student对象的数组列表：

```java
ArrayList<Student> sds = new ArrayList<Student>();
```

在Java 10中，最好使用var关键字以避免重复写类名：

```java
var sds =new ArrayList<Student>();
```

如果没有使用var关键字，可以根据菱形语法，省去右边的类型参数：

```java
ArrayList<Student> sds = new ArrayList<>();
```

> **菱形语法** ： 即<>，可结合new操作符使用，
>
> 如果：
>
> - 赋值给一个变量
> - 传递给一个方法
> - 从某个方法返回
>
> 此时编译器会检查这个变量、参数或方法的泛型类型，然后将这个类型放在<>中。
>

上例中，new ArrayList<>()将赋值给一个泛型类型为Student的变量sds，故将泛型类型Student填入<>中。

#### **一、增**

使用add方法可以将元素添加到数组列表中，例：

```jAVA
sds.add(new Student("student1",...));
sds.add(new Student("student2",...));
...
```

如果调用add而内部数组已经满了，那么数组列表就会自动创建一个更大的数组，并将所有的对象从较小的数组中拷贝到较大的数组中。

如果已经可以估计所需数组大小，可以使用ensureCapacity方法手动设置数组大小：

```java
sdsl.ensureCapacity(100); //该方法将分配一个包含100个对象的内部数组

//也可以在构造ArrayList时传入
ArrayList<Student> sds = new ArrayList<>(100);
```

当然上述定义的大小，仅仅只是表示**可能**存放100个对象，实际也可以更多，只不过要重新分配空间。

使用size方法可以返回数组列表中包含的实际元素个数：

```java
sds.size();
```

#### **二、读与改**

数组列表无法使用[]访问或改变数组列表的元素，而需要使用get和set方法：

```java
//设置第i个元素
Student student3 =new Student("student3",...);
sds.set(i,student3);

//得到第i个元素
Student sd =sds.get(i)
```

### ArrayList与泛型

上述代码中，我们得到数组列表的第i个元素使用了如下代码：

```java
//得到第i个元素
Student sd =sds.get(i)
```

然而在没有类型参数的时候，这样写是错误，因为最终得到的是Object类型对象，而想得到真正的对象，必须进行强制转换：

```java
Student sd =(Student)sds.get(i)
```

同时由于接受的是Object类型的对象，在add时也无法进行对象类型的检测。

## 定义简单的泛型类

**什么是泛型类？**

泛型类就是有一个或多个类型变量的类。

**什么是类型变量？**

类型变量在整个类定义中用于指定方法的返回类型以及局部变量的类型。

**类型变量的常用描述**

- E：表示集合的元素类型
- K、V：分别表示表的键和值的类型
- T（U、S)：表示任意类型

一个简单的泛型类示例：

```java
public class Pair<T> {
    private T first;
    private T second;

    public Pair() {
        first = null;
        second = null;
    }

    public Pair(T first, T second) {
        this.first=first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}

```

## 类型擦除

Java的泛型是伪泛型，这是因为Java在编译期间，所有的泛型信息都会被擦掉，这也就是通常所说的类型擦除。

[《Java泛型类型擦除以及类型擦除带来的问题》](https://www.cnblogs.com/wuqinglong/p/9456193.html)

```java
public class Test {

    public static void main(String[] args) throws Exception {

        ArrayList<Integer> list = new ArrayList<Integer>();

        list.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer

        list.getClass().getMethod("add", Object.class).invoke(list, "asd");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

}
```

在程序中定义了一个`ArrayList`泛型类型实例化为`Integer`对象，如果直接调用`add()`方法，那么只能存储整数数据，不过当我们利用反射调用`add()`方法的时候，却可以存储字符串，这说明了`Integer`泛型实例在编译之后被擦除掉了，只保留了原始类型。

## 通配符：T E K V ?

- ？ 表示不确定的 java 类型
- T (type) 表示具体的一个java类型
- K V (key value) 分别代表java键值中的Key Value
- E (element) 代表Element

**上界通配符： < ? extends E>**

在类型参数中使用 extends 表示这个泛型中的参数必须是 E 或者 E 的子类，这样有两个好处：

- 如果传入的类型不是 E 或者 E 的子类，编译不成功
- 泛型中可以使用 E 的方法，要不然还得强转成 E 才能使用

```java
static int countLegs (List<? extends Animal > animals ) {
    int retVal = 0;
    for ( Animal animal : animals )
    {
        retVal += animal.countLegs();
    }
    return retVal;
}

static int countLegs1 (List< Animal > animals ){
    int retVal = 0;
    for ( Animal animal : animals )
    {
        retVal += animal.countLegs();
    }
    return retVal;
}

public static void main(String[] args) {
    List<Dog> dogs = new ArrayList<>();
 	// 不会报错
    countLegs( dogs );
	// 报错
    countLegs1(dogs);
}
```

**下界通配符 < ? super E>**

传入的类型必须是E或者E的父类，最高时Objects

**？和 T 的区别**

```java
// 指定集合元素只能是 T 类型
List<T> list = new ArrayList<T>();
// 集合元素可以是任意类型，这种没有意义，一般是方法中，只是为了说明用法
List<?> list = new ArrayList<?>();
```

`?`和` T`都表示不确定的类型，区别在于我们可以对` T `进行操作，但是对` ?` 不行，比如如下这种 ：

```Java
// 可以
T t = operate();

// 不可以
? car = operate();
```

T 是一个 确定的 类型，通常用于泛型类和泛型方法的定义，？是一个 不确定 的类型，通常用于泛型方法的调用代码和形参，不能用于定义类和泛型方法。

类型参数 T 只具有 一种 类型限定方式：

```java
T extends A
```

但是通配符 ? 可以进行 两种限定：

```java
? extends A
? super A
```

# Reference

- [《Java泛型类型擦除以及类型擦除带来的问题》](https://www.cnblogs.com/wuqinglong/p/9456193.html)

- [《聊一聊-JAVA 泛型中的通配符 T，E，K，V，？》](https://juejin.cn/post/6844903917835419661#heading-6)