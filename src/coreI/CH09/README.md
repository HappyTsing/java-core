# 前言

有关 Java 集合的内容，于《Java 核心卷 Ⅰ》中有初步的学习，不过始终没有真正理解，以致于在学习数据结构和算法时，无法得心应手的使用这部分的内容，决定认真的学习一次 Java 的集合（容器）的相关知识。

> 2020-10-28 于电科沙河欣苑寝室。

# 集合框架接口

![Collections_interfate_framework](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Collections_interface_framework.png)

上图是由 IDEA 导出的 UML 图，是 Java 集合框架为不同类型的集合定义的接口。

## RandomAccess 接口

存在两种有序集合（List 接口）：

- 数组：快速的随机访问
- 链表：随机访问很慢，性能开销大，所以应该尽量不使用下标随机访问，而使用迭代器顺序访问

但是由于继承于同一个接口 List，而该接口又定义了多个用于随机访问的方法，因此链表理论上也是可以进行随机访问的，**为了避免对链表完成随机访问操作**，Java 1.4 引入了一个**标记接口**RandomAccess，该接口**不包含任何方法**，仅**用于测试一个特定的集合是否支持高效的随机访问**。

```java
if(c instanceof RandomAccess)
{
    //表示支持高效随机访问，即数组有序集合
}
else
{
    //不支持，即链表有序集合
}
```

![RandomAccess](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/RandomAccess.png)

如图所示，ArrayList 类实现了接口 RandomAccess 接口，`ArrayList instanceof RandomAccess`结果为 true，故支持高效随机访问，同理 LinkedList 类不支持高效随机访问。

## Iterator 接口

查看接口下方法：

![Iterator](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Iterator.png)

通过反复调用 next()方法，可以逐个访问集合中的每个元素。

如果到达集合末尾，next()方法将抛出一个 NoSuchElementException。

因此需要在调用 next()方法之前调用 hasNext()方法。

remove()方法将删除上次调用 next()方法时返回的元素。

forEachRemaining()方法用于接收一个 lambda 表达式，实现循环操作。

### ListIterator 接口

该接口 extends 了 Iterator 接口，是为了弥补 Iterator 接口无法添加元素的缺陷。

List 是一个有序集合，其中的链表(LinkedList)常常需要将对象添加到链表中任意位置：因为链表的插入时间复杂度是 O(1)。

而<u>LinkedList 自带的 add 方法只能将对象插入链表的尾部</u>，由于迭代器描述了集合中的位置，因此依赖于位置的 add 方法将有迭代器负责。

至于为什么不再 Iterator 中直接提供该方法，因为集(set)和映射(map)是无序的，显然只有对自然有序的集合(list)使用迭代器添加元素才有实际意义，因此 Iterator 接口未设置 add 方法。

> <u>LinkedList 自带的 add 方法只能将对象插入链表的尾部</u>：摘自《Java 核心卷 Ⅰ 第 11 版 P378》
>
> 这句话似乎是有问题的，List 接口定义有两个 add 方法
>
> - boolean add(E e)
>
> - void add(int index, E element)
>
> 显然第二个方法就是用于插入链表中某位置的，因此其实不需要使用迭代器来插入。
>
> **合理的解释**是，前文已经提到 RandomAccess 接口，该接口是为了防止对链表进行随机访问，因为开销大，链表查找的时间复杂度是 O(n)，但是查找和访问的时间复杂度是 O(1)，因此不提倡链表使用随机访问，而使用迭代器进行顺序访问！

```java
var staff = new LinkedList<String>();
staff.add("Amy");
staff.add("Boy");
staff.add("Cat");
ListIterator liter= staff.listIterator();
liter.next();
liter.add("test");
```

结果如图：

![ListIterator_test_result1](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/ListIterator_test_result1.png)

```java
//如果将liter.next();  liter.add("test");改为下述语句：
staff.add(2,"test2");
```

结果如图：

![ListIterator_test_result2](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/ListIterator_test_result2.png)

还定义两个方法：previous()和 hasPrevious()用于反向遍历链表

以下是 ListIterator 的方法：

![ListIterator](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/ListIterator.png)

## Iterable 接口

查看接口下方法：

![Iterable](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Iterable.png)

顾名思义：Iterable 是指可迭代的，迭代方法有两种

- iterator 迭代器
- forEach

此处的 Iterator()方法用于返回一个实现了 Iterator 接口的对象：

```java
Collection<String> c = new ...;    //public interface Collection<E> extends Iterable<E>
Iterator<String> iter = c.iterator();
while(iter.hasnext())
{
    String element = iter.next();
    //do something with element;
}
```

上述语句也可以使用 for each 循环操作，表示更加简练：

```java
for(String element:c)
{
    //do something with element;
}
```

编译器会简单地将 for each 循环转换为带有迭代器的循环，for each 循环可以处理任何实现了 Iterable 接口的对象。

不写循环，调用 forEachRemaining 方法并提供一个 lambda 表达式。将对迭代器地每一个元素调用这个 lambda 表达式，直到没有元素为止。

```
iter.forEachRemaining(element -> do something with element);
```

### Collection 接口

在 java 类库中，集合类的基本接口是 Collection 接口和 Map 接口。

Collection 接口提供了许多使用方法：

![Collection_methods](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Collection_methods.png)

方法具体作用见：核心卷 P372

显然，直接实现 Collection 接口是一件十分复杂的事情，为了方便实现，Java 类库提供了一个类 AbstractCollection，它保持基础方法 size 和 iterator 仍为抽象方法，但是实现了其他所有方法。

#### List

List 是一个有序、可重复集合，元素会增加到容器的特定位置。

两种访问方式：

- 迭代器
- 整数索引，又称随机访问。因为这种访问方式可以按任意顺序访问元素，而迭代器访问时，必须顺序访问。

其定义了多个用于随机访问的方法：

```java
void add(int index, E element)
void remove(int index)
E get(int index)
E set(int index, E element)
```

#### Set

Set 是一个无序、不可重复集合，该接口等同于 Collection 接口，不过其方法有更严谨的定义。

集(set)的 add 方法不允许增加重复的元素。

**为什么方法签名相同，还要单独建立一个接口？**

> 因为不是所有的集合都是集(set)，因此建立一个 Set 接口可以允许程序员编写只接收集的方法。

查看 Set 接口的方法可以看到，除了继承于 Collection 接口的方法，仅有 of 方法。

![Set_method](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Set_method.png)

#### Queue

常用于实现队列，先进先出。

![Queue_method](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Queue_method.png)

除了基本的 Collection 操作外，队列还提供其他的插入、提取和检查操作。

每个方法都存在两种形式：

- 一种抛出异常（操作失败时）
- 一种返回一个特殊值（null 或 false，具体取决于操作）

| **操作**           | **抛出异常** | **返回特殊值** |
| ------------------ | ------------ | -------------- |
| 在队列尾部插入     | add(e)       | offer(e)       |
| 从队列头部移除     | remove()     | poll()         |
| 查看第一个元素内容 | element()    | peek()         |

##### Deque

队列（Queue）允许高效地在尾部添加元素，并在头部删除元素。双端队列（Deque）继承了 Queue，允许在头部和尾部都高效地添加或删除元素，不支持在队列中间添加元素。

该接口于 Java 6 中引入，ArrayDeque 和 LinkedList 实现了这个接口。

![Deque_method](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Deque_method.png)

该接口既可以实现队列：FIFO，又可以实现栈：LIFO

且提供了用于栈的方法：pop()、Push(E)

## Map 接口

使用键值对（kye-value）存储，Key 是无序的、不可重复的，value 是无序的、可重复的，每个键最多映射到一个值。

## SortedSet 和 SortedMap

这两个接口会提供用于排序的比较器对象，定义了可以得到集合子集视图的方法。

## NavigableSet 和 NavigableMap

Java 6 引入，其中包含一些用于搜索和遍历有序集和映射的方法，这些方法理应在 SortedSet 和 SortedMap 接口中就应该包含，可能是由于考虑不周，于是又创建了 NavigableSet 和 NavigableMap 接口 extends 它们来作为补充！

有且仅有 TreeSet 和 TreeMap 实现了这些接口。

![TreeSet_TreeMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/TreeSet_TreeMap.png)

# 集合框架类

![Collections_class_framework](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Collections_class_framework.png)

## AbstractCollection 抽象类

在`Collection接口`一章中已经说明，由于 Collection 接口中声明了许多方法，如果开发者直接实现 Collection 接口，那么会十分复杂，于是 Java 类库提供了一个类 AbstractCollection，实现了 Collection 接口中的绝大多数方法以减少实现难度。

查看其方法：除了 iterator()和 size()仍旧为抽象方法，其余方法都已经实现。

于是开发者无需实现 Collection 接口，直接继承 AbstractCollection 接口即可。

![AbstractCollection](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/AbstractCollection.png)

## AbstractList、AbstractSet、AbstractQueue、AbstractMap 抽象类

同 AbstractCollection 抽象类，都是为了简化 List、Set、Queue、Map 接口的实现，为开发者实现了这些接口中的大部分方法，开发者只需要继承这些抽象类，然后实现其中未实现的抽象方法即可！

## 具体集合

![Collections_detail_framework](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Collections_detail_framework.png)

| 集合类型        | 描述                                                   |
| --------------- | ------------------------------------------------------ |
| ArrayList       | 可以动态增长和缩减的一个索引序列                       |
| LInkedList      | 可以在任何位置高效插入和删除的一个有序序列             |
| ArrayDeque      | 实现为循环数组的一个双端队列                           |
| PriorityQueue   | 允许高效删除最小元素的一个集合，插入顺序任意，检索有序 |
| EnumSet         | 一个包含枚举类型值的集                                 |
| TreeSet         | 没有重复元素的一个有序集                               |
| HashSet         | 没有重复元素的一个无序集                               |
| LinkedHashSet   | 可以记住元素插入次序的集                               |
| EnumMap         | 键属于枚举类型的一个映射                               |
| TreeMap         | 键有序的一个映射                                       |
| HashMap         | 存储键值对的一个映射                                   |
| LinkedHashMap   | 可以记住键值对添加次序的映射                           |
| WeakHashMap     | 值不会在别处使用时就可以被垃圾回收的一个映射           |
| IdentityHashMap | 用==而不是用 equals 比较键的一个映射                   |

**如何选用**：主要根据集合的特点来选用，比如我们需要根据键值获取到元素值时就选用 `Map` 接口下的集合，需要排序时选择 `TreeMap`,不需要排序时就选择 `HashMap`,需要保证线程安全就选用 `ConcurrentHashMap`。

当我们只需要存放元素值时，就选择实现`Collection` 接口的集合，需要保证元素唯一时选择实现 `Set` 接口的集合比如 `TreeSet` 或 `HashSet`，不需要就选择实现 `List` 接口的比如 `ArrayList` 或 `LinkedList`，然后再根据实现这些接口的集合的特点来选用。

### ArrayList

1. 实现了 RandomAccess 接口，表明可以通过 get 和 set 方法随机的访问每个元素。
2. 继承了 AbstractList 抽象类，即实现了 List 接口，表明是一个有序集合

扩容机制：

在 ArrayList 创建时，如果没有指定容量的话，会先初始化一个空数组的 Object 数组

若是在添加第一个元素时，会创建一个长度 10 的数组，若是长度大于 10 之后，每次会将当前长度右移一位，即当前长度/2，然后加上当前长度。

```java
// oldCapacity为旧容量，newCapacity为新容量
int oldCapacity = elementData.length;
//将oldCapacity 右移一位，其效果相当于oldCapacity /2，
//我们知道位运算的速度远远快于整除运算，整句运算式的结果就是将新容量更新为旧容量的1.5倍，
int newCapacity = oldCapacity + (oldCapacity >> 1);
```

![ArrayList](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/ArrayList.png)

### LinkedList

1. 继承了 AbstractList 抽象类，即实现了 List 接口，表明是一个有序集合
2. 未实现 RandomAccess 接口，表明不支持高效的随机访问，因此需要使用迭代器来访问元素
3. 实现了 Deque 接口，表明该类可以提供双端队列，其大小可以根据需要扩展。可用作栈和队列！

![LinkedList](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/LinkedList.png)

### ArrayDeque

1. 实现了 Deque 接口，表明该类可以提供双端队列，其大小可以根据需要扩展。可用作栈和队列！

![ArrayDeque](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/ArrayDeque.png)

### PriorityQueue

优先队列中的元素可以按照任意的顺序插入，但会按照有序的顺序进行检索。也就是说，无论何时调用 remove 方法，总会获得当前优先队列中最小的元素。

优先队列并未对所有元素进行排序，使用堆（heap）。堆是一个可以自组织的二叉树，其添加 add 和删除 remove 操作可以让最小的元素移动到根，而不必花时间排序！

![PriorityQueue](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/PriorityQueue.png)

### EnumSet

![EnumSet](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/EnumSet.png)

### HashSet

HashSet 是 Set 接口的主要实现类 ，是**基于散列集（hash table）的集**。

**底层原理**：HashSet 底层完全是基于 HashMap 实现的，由于 HashMap 存储 key：value 键值对，因此默认 value 为 Object 的静态常量，取值的时候也只返回 key。

**构造方法**：直接初始化一个 HashMap，初始化容量（数组容量，也即链表数）和装填因子。

```java
//构造一个空散列集
public HashSet() {
    map = new HashMap<>();
}
//构造一个散列集，并将集合中的所有元素添加到这个散列集中
public HashSet(Collection<? extends E> c) {
    map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
    addAll(c);
}
//构造一个空的具有指定容量(initialCapacity)的散列集
public HashSet(int initialCapacity) {
    map = new HashMap<>(initialCapacity);
}
/*构造一个有指定容量(initialCapacity)和装填因子(loadFactor)的空散列集
  装填因子是0.0~1.0的一个数，最好取值为0.6~0.9，当大于这个百分比时，散列表进行再散列 */
public HashSet(int initialCapacity, float loadFactor) {
    map = new HashMap<>(initialCapacity, loadFactor);
}
```

**具体方法：**直接调用 HashMap 的相关方法

```java
// Dummy value to associate with an Object in the backing Map 译：伪值，以便与后备映射中的对象相关联
private static final Object PRESENT = new Object();

//调用map的put方法，不过value只是一个Object对象。
public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}
public boolean contains(Object o) {
    return map.containsKey(o);
}
public boolean remove(Object o) {
    return map.remove(o)==PRESENT;
}
public int size() {
    return map.size();
}
public boolean isEmpty() {
    return map.isEmpty();
}
```

![HashSet](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/HashSet.png)

### TreeSet

相比于 HashSet，多实现了 SortedSet 和 NavigableSet 接口，能够按照添加元素的顺序进行遍历，**排序是使用红黑树实现**，迭代器总是以有序的顺序访问每个元素。

![TreeSet](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/TreeSet.png)

### LinkedHashSet

1. 继承了 HashSet，LinkedHashSet 是 HashSet 的子类，能够按照添加的顺序遍历；

![LinkedHashSet](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/LinkedHashSet.png)

### EnumMap

![EnumMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/EnumMap.png)

### HashMap

```java
/*方法一 先new一个HashMap对象，再put键值对*/
var map = new HashMap<String, String>();
map.put("name", "jack");
map.put("age", "19");

/*方法二 使用双括号 {{}}来初始化，使代码简洁易读。
		第一层括弧实际是定义了一个匿名内部类 （Anonymous Inner Class）
		第二层括弧实际上是一个实例初始化块 （Instance Initializer Block），这个块在内部匿名类构造时被执行。
		这种写法的好处很明显，就是一目了然。但是这种写法可能导致这个对象串行化失败的问题。
			其一，因为这种方式是匿名内部类的声明方式，所以引用中持有着外部类的引用。所以当串行化这个集合时，外部类也会被不知不觉				   的串行化，而当外部类没有实现Serialize接口时，就会报错。
			其二，在上面的例子中，其实是声明了一个继承自HashMap的子类，然而有些串行化方法，例如要通过Gson串行化为json，或者要				 串行化为xml时，类库中提供的方式，是无法串行化Hashset或者HashMap的子类的，也就导致了串行化失败。解决办法是				   重新初始化为一个HashMap对象new HashMap(map);，这样就可以正常进行初始化了。
		另外要注意的是，这种使用双括号进行初始化的语法在执行效率上要比普通的初始化写法要稍低。
		最后，这个使用双括号进行初始化的语法同样适用于ArrayList和Set等集合。*/
var map2 = new HashMap<String, String>(){
    {
        put("name", "jack");
        put("age", "19");
    }
};
```

| 常用操作                                   | 方法签名                               |
| ------------------------------------------ | -------------------------------------- |
| 插入键值对数据                             | public V put(K key, V value)           |
| 根据键值获取键值对值数据                   | public V get(Object key)               |
| 获取 Map 中键值对的个数                    | public int size()                      |
| 判断 Map 集合中是否包含键为 key 的键值对   | public boolean containsKey(Object key) |
| 判断 Map 集合中是否包含值为 value 的键值对 | boolean containsValue(Object value)    |
| 判断 Map 集合中是否没有任何键值对          | public boolean isEmpty()               |
| 清空 Map 集合中所有的键值对                | public void clear()                    |
| 根据键值删除 Map 中键值对                  | public V remove(Object key)            |

![HashMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/HashMap.png)

**原理解释**

初始容量和扩容机制：HashMap 默认初始化 16，每次扩容变为原来的两倍，HashTable 默认初始化 11，每次扩容为 2n+1。且若指定容量值，HashMap 会自动扩充为 2 的幂次方大小。

为什么是 2 的幂次方？

HashMap 如何定位数组索引位置源码：

```java
方法一：
static final int hash(Object key) {   //jdk1.8 & jdk1.7
     int h;
     // h = key.hashCode() 为第一步 取hashCode值
     // h ^ (h >>> 16)  为第二步 高位参与运算
     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
方法二：
static int indexFor(int h, int length) {  //jdk1.7的源码，jdk1.8没有这个方法，但是实现原理一样的
     return h & (length-1);  //第三步 取模运算
}
```

![HashMap_theory](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/HashMap_theory.jpg)

方法一用于得到 hash 值，方法二使用该 hash 值得到数组索引位置，一般来说我们取模得到数组索引位置：`h%length`

但是&比%具有更高的效率，因此我们想办法使用&代替%运算。

我们知道 HashMap 底层数组的长度总是 2 的 n 次方，此时：`h& (length-1)`等同于`h%length`

这也解释了 HashMap 的底层数组长度是 2 的 n 次方

**HashMap 底层**

HashMap 底层使用的是数组+链表，当发生哈希碰撞时，首先比较两个碰撞元素的 HashCode()，若相同，再比较 equals()，若相同，则说明两个元素相同，反之则将元素加入链表中，使用拉链法解决冲突。

当链表长度大于 8 时，链表转换为红黑树结构。

当红黑树长度小于 6 时，红黑树转化为链表结构。

[6-8 的原因](https://www.javazhiyin.com/34651.html)

每个树节点所需要的内存空间大概是链表节点的两倍！

且当 hashCode 离散型很好的时候，数据会比较均匀的分布，因此出现链表长度为 8 的情况几乎是不可能的！

参见：[美团技术团队: Java 8系列之重新认识HashMap](https://zhuanlan.zhihu.com/p/21673805)

### TreeMap

相比于 HashMap，TreeMap 多实现了 NavigableMap 和 SortedMap，有了对集合中的元素根据键排序的能力。默认是按 key 的升序排序，不过我们也可以指定排序的比较器。

示例代码如下：

```java
public class Person {
    private Integer age;

    public Person(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }


    public static void main(String[] args) {
        TreeMap<Person, String> treeMap = new TreeMap<>(new Comparator<Person>() {
            @Override
            public int compare(Person person1, Person person2) {
                int num = person1.getAge() - person2.getAge();
                return Integer.compare(num, 0);
            }
        });
        treeMap.put(new Person(3), "person1");
        treeMap.put(new Person(18), "person2");
        treeMap.put(new Person(35), "person3");
        treeMap.put(new Person(16), "person4");
        treeMap.entrySet().stream().forEach(personStringEntry -> {
            System.out.println(personStringEntry.getValue());
        });
    }
}
```

输出：

```java
person1
person4
person2
person3
```

可以看出，`TreeMap` 中的元素已经是按照 `Person` 的 age 字段的升序来排列了。

上面，我们是通过传入匿名内部类的方式实现的，你可以将代码替换成 Lambda 表达式实现的方式：

```java
TreeMap<Person, String> treeMap = new TreeMap<>((person1, person2) -> {
  int num = person1.getAge() - person2.getAge();
  return Integer.compare(num, 0);
});
```

![TreeMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/TreeMap.png)

### LinkedHashMap

![LinkedHashMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/LinkedHashMap.png)

### WeakHashMap

![WeakHashMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/WeakHashMap.png)

### IdentityHashMap

![IdentityHashMap](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/IdentityHashMap.png)

# 具体应用

## 栈

Stack、ArrayDeque、LinkedList 都可以用于栈

![stack](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/stack.png)

### Stack 类

该类中有熟悉的 push 和 pop 方法，但是 Stack 扩展了 Vector 类，底层实际上还是数组（初始化长度为 10 的数组），所以还是存在**需要扩容**。

Stack 之所以继承 Vector，是为了复用它的方法来实现栈，然而这就是设计不严谨的地方，Stack 和 Vector 本是毫无关系的，仅仅为了复用方法而继承 Vector，而且还造成了很多不符合栈定义的错误：由于 Vector 是由数组实现的集合类，它包含了大量集合处理的方法，比如你**可以使用并非栈操作的 insert 和 remove 方法在任何地方插入和删除值**，而不是栈顶！

**不建议使用！！**

![Stack_method](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Stack_method.png)

### Deque 接口

- **ArrayDeque**

  > ArrayDeque 是 Deque 接口的一种具体实现，是依赖于**可变数组**（初始化长度为 16 的数组）来实现的。ArrayDeque 没有容量限制，可根据需求**自动进行扩容**。ArrayDeque 可以作为栈来使用，效率要高于 Stack。ArrayDeque 也可以作为队列来使用，效率相较于基于双向链表的 LinkedList 也要更好一些。注意，ArrayDeque 不支持为 null 的元素。

- **LInkedLIst**

  > LinkedList 是 Deque 接口的另一种具体实现，是依赖于**链表**实现的，同样可以作为栈和队列使用！

- **如何选择**

  > 1. 需要线程同步：使用 Collections 工具类中 synchronizedXxx()将线程不同步的 ArrayDeque 以及 LinkedList 转换成线程同步。
  >
  > 2. 频繁的插入、删除操作：LinkedList
  >
  > 3. 未知的初始数据量：LinkedList
  >
  > 4. 频繁的随机访问操作：ArrayDeque

若只是想把 LInkedList 或 ArrayDeque 作为栈或者队列使用，可以使用接口 Deque 变量来引用实例对象：

```java
Deque stack = new ArrayDeque<String>();
Deque stack = new LinkedList<String>();
```

| Queue Method | Equivalent Deque Method（等效的 Deque 方法） | 说明                                   |
| ------------ | -------------------------------------------- | -------------------------------------- |
| `add(e)`     | `addLast(e)`                                 | 向队尾插入元素，失败则抛出异常         |
| `offer(e)`   | `offerLast(e)`                               | 向队尾插入元素，失败则返回`false`      |
| `remove()`   | `removeFirst()`                              | 获取并删除队首元素，失败则抛出异常     |
| `poll()`     | `pollFirst()`                                | 获取并删除队首元素，失败则返回`null`   |
| `element()`  | `getFirst()`                                 | 获取但不删除队首元素，失败则抛出异常   |
| `peek()`     | `peekFirst()`                                | 获取但不删除队首元素，失败则返回`null` |

| Stack Method | Equivalent Deque Method (等效的 Deque 方法) | 说明                                   |
| ------------ | ------------------------------------------- | -------------------------------------- |
| `push`       | `addFirst(e)`                               | 向栈顶插入元素，失败则抛出异常         |
| 无           | `offerFirst(e)`                             | 向栈顶插入元素，失败则返回`false`      |
| `pop()`      | `removeFirst()`                             | 获取并删除栈顶元素，失败则抛出异常     |
| 无           | `pollFirst()`                               | 获取并删除栈顶元素，失败则返回`null`   |
| `peek()`     | `getFirst()`                                | 获取但不删除栈顶元素，失败则抛出异常   |
| 无           | `peekFirst()`                               | 获取但不删除栈顶元素，失败则返回`null` |

![Queue_description](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/Queue_description.png)

Note that the **peek method works equally well when a deque is used as a queue or a stack**; in either case, elements are drawn from the beginning of the deque.

**peek 方法在 stack 和 queue 中都可以使用！**

![peek](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/peek.png)

## 队列

使用 Queue 来接收对象

```java
Queue queue1 = new LinkedList<String>();
Quque queue2 = new ArrayDeque<String>();
```

| Queue Method | Equivalent Deque Method（等效的 Deque 方法） | 说明                                   |
| ------------ | -------------------------------------------- | -------------------------------------- |
| `add(e)`     | `addLast(e)`                                 | 向队尾插入元素，失败则抛出异常         |
| `offer(e)`   | `offerLast(e)`                               | 向队尾插入元素，失败则返回`false`      |
| `remove()`   | `removeFirst()`                              | 获取并删除队首元素，失败则抛出异常     |
| `poll()`     | `pollFirst()`                                | 获取并删除队首元素，失败则返回`null`   |
| `element()`  | `getFirst()`                                 | 获取但不删除队首元素，失败则抛出异常   |
| `peek()`     | `peekFirst()`                                | 获取但不删除队首元素，失败则返回`null` |

## 优先队列

```java
PriorityQueue<Integer> priorityQueue=new PriorityQueue<>();
```

java 自带的优先队列属于小顶堆，若是需要大顶堆等，需要提供一个 Comparator

# size()、length()、length

Collection接口和Map接口，都有size()方法，因此凡是**集合**，如set、map、list，都拥有方法size()

**String类**有length()方法

length是**数组**的属性
