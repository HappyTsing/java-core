# 从迭代到流

与集合相比，流提供了一种可以让我们在更高的概念级别上指定计算任务的数据视图。

通过使用流，可以说明想要完成什么任务，而不是说明去实现它，将操作的调度留给具体实现去解决。

> 例如，假设我们想要计算某个属性的平均值，那么我们就可以指定数据源和该属性，然后，流库就可以对计算进行优化，例如，使用多线程来计算总和与个数，并将结果合并。

在处理集合时，通常会迭代遍历它的元素，并在每个元素上执行某项操作。例如对所有列表中长度大于4的单词计数：

```java
List words; //["hello","ada"...]
int count = 0;
for(String w : words){
  if(w.length() > 12) count++
}
```

上述操作可以使用流解决，如下：

```java
long count = words.stream()
  .filter(w -> w.length() > 12)
  .count();
```

现在，我们不必扫描整个代码去查找过滤和技术操作，方法名就可以直接告诉我们代码在做什么。而且，循环需要非常详细地指定操作的顺序，而流却能够以想要的任何方式来调度这些操作，只要结果是正确的即可。

例如：仅将`stream()`修改为`parallelStream()`就可以让流库以并行的方式来执行过滤和计数。

流遵循了”**做什么而非怎么做**“的原则。

上述例子中，描述了需要做什么：获取长单词，并对它们计数。但是并没有指定操作该以什么顺序或者在哪个线程中执行。

相比之下，迭代循环要确切地指定计算应该如何工作，因此也丧失了优化的机会。

**流 VS 迭代**

流和迭代表面上看起来很相似，都可以转换和获取数据，但是存在着显著的差异：

- 流并不存储其元素。这些元素可能存储在底层的集合中（如上例），或者是按需生成的。
- 流的操作不会修改其数据源。例如，filter 方法不会从流中移除元素，而是会生成一个新的流，其中不包含被过滤掉的元素。
- 流的操作是尽可能惰性执行。这意味着直至需要结果时，操作才会执行。例如，我们只想查找前 5 个长单词而不是所有长单词，那么 filter 方法就会在匹配到第 5 个长单词后停止过滤。

**三阶段操作流程**

一般来说，流的操作共分为三个阶段：

- 创建一个流
- 指定将初始流转换为其他流的中间操作，可能包含多个步骤
- 应用终止操作，从而产生结果。该操作会强制执行之前的惰性操作，此后该流再也不能使用。

后续将依次讲解流的创建、转换和终止。

# 流的创建

- Collection 接口
  - stream()
  - parallelStream()
- 对于数组，使用静态方法：Stream.of(T ... value)
- 静态方法：Array.stream(array,from,to)
- 不包含任何元素的流，静态方法：Stream.empt()
- 无限流：
  - Stream.generate(Supplier\<T\> s)
  - Stream.iterate(T seed, Predicate\<? super T\>)
- 按照正则表达式分割对象：Pattern.compiler("正则").splitAsStream(contents)
- 产生扫描器的符号流：Scanner.tokens()
- 按行分割文件：Files.lines(path)
- 如果持有的Iterable对象不是集合，通过该方法转换为流：StreamSupport.stream(iterable.spliterator(),false)
- 如果持有的是Iterator对象，并希望得到由它的结果构成的流：StreamSupp

**☆不干涉性：执行流操作时，并没有修改流背后的集合。由于流的操作是惰性的，所以在终止操作得以执行时，集合可能已经发生了变化，此时流操作的结果就会变成未定义的。**

# 流的转换

## 过滤、处理和摊平

- filter(Predicate predicate)：产生一个流，它包含当前流中所有满足谓词条件的元素。其引元是 Predicate\<T\>
- map(Function mapper)：产生一个流，它包含将传入的函数 mapper 应用于所有元素所产生的结果
- flatMap(Function mapper)：产生一个流，它是通过将传入的函数 mapper 应用于所有元素所产生的结果连接到一起而获得的。例如，[...[1,2],[3,4]...] 将其摊平为 [...1,2,3,4...]。需要每一个结果都是一个流。

注意：flatMap方法在流之外的类中也会使用，因为它是计算机科学中的一种通用概念：

- 假设有一个泛型G（例如Stream），以及函数f，用于将类型T转换为G\<U\>，以及函数g，用于将类型U转换为G\<V\>。
- 此时可以通过使用flatMap来组和他们，即首先应用f，然后应用g。
- 上述是**单子论**的关键概念，无需了解就可以使用flatMap。

## 抽取子流和组合流

- limit(long maxSize)：产生一个流，其中包含了流中最初的 maxSize 个元素
- skip(long n)：产生一个流，它的元素是当前流中除了前 n 个元素之外的所有元素
- takeWhile(Predicate predicate)：产生一个流，它的元素是当前流中，直至第一个不满足的元素，所有满足谓词条件的元素。也就是说当遇到第一个不满足的元素时就会结束。**区别于filter的全部过滤**。
- dropWhile(Predicate predicate)：产生一个流，它的元素是当前流中，直至第一个满足的元素，排除不满足谓词条件的元素之外的所有元素。
- concat(Stream a,Stream b)：产生一个流，它的元素是a的元素后面跟着b的元素，也就是连接两个流。

## 去重和排序

- distinct()：产生一个流，包含当前流中所有不同的元素

- sorted()：排序，有多种变体，比如可以接受一个Comparator。**Todo 2022-11-02**

- peek(Consumer action)：产生一个流，其元素与原来流中元素相同，但是在每次获取一个元素时，都会调用一个函数。

  Consumer接受单个输入参数T，但不返回操作的结果。

  **区别于map(Function mapper)**，Function接受一个参数，并返回操作的结果。

  也就是说，map会修改返回的流，其返回的流中的元素是函数处理的返回值。

  而peek不会修改返回的流，action只是执行而已，并不会影响返回的流中的内容。这对调试来说很方便。

# Optional类型

Optional\<T\>对象是一种包装器对象，要么包装了类型T的对象，此时称这种值是存在的，要么没有包装任何对象。

Optional\<T\>类型被当做是一种更安全的方式，用来替代类型T的引用，这种引用要么引用某个对象，要么为null。

但是只有在正确使用的情况下才会更安全，做简要介绍。

**使用 Optional 的原因？**

常常需要对对象进行判断是否为null，这使得代码异常冗余。例如：`School.getStudentById(id).getName()`，在该代码前，必须先判断`School`对象不为空，再判断`School.getStudentById(id)`获取的`Student`对象不为空，才能执行`getName()`方法，因此需要多次对null进行判断！

**不正确的使用 Optional 值的方式**

以一个示例讲解，不使用 Optional 时：

```java
public String getName(User user){
	if(user == null){
		return "Unknown";
	}else return user.name();
}
```

改造上述代码，以错误用法为例，其并不比传统的写法安全，反而使得代码更加复杂：

```java
public String getName(User user){
	Optional<User> u = Optional.ofNullable(user);
	if(!u.isPresent()){
		return "Unknown";
	}else return u.get().name();  // get在不存在时抛出 NoSuchElementException 异常
}
```

Optional 类可以将其与流或其它返回 Optional 的方法结合，以构建流畅的API，正确示例：

```java
public String getName(User user){
	return Optional.ofNullable(user)
							.map(u -> u.name)
							.orElse("Unknown");
}
```

**正确使用OPtional提示**

- Optional 类型的变量永远都不应该为 null。
- 不要使用 Optional 类型的域。因为其代价是额外多出来一个对象。在类的内部，使用 null 表示缺失的域更易于操作。
- 不要在集合中放置 Optional 对象，并且不要将它们用作 map 的键。应该直接收集 Optional 中的值。

**创建 Optional 的值**

- Static Optional of(T value)：产生一个具有给定值的 Optional。如果value为null，抛出NullPointerException异常
- Static Optional ofNullable(T value)：产生一个具有给定值的 Optional。如果value为null，产生一个空Optional
- Static Optional empty()：产生一个空Optional

此外，还可以使用 flatMap 构建 Optional 值的函数。参见P16

**获取、消费、管道化 Optional的值**

- 策略一，获取 Optional 的值：值不存在时，使用某种默认值

  - orElse(T other)：使用写死的默认值
  - orElseGet(Supplier other)：通过调用代码来计算默认值
  - orElseThrow(Supplier execptionSupplier)：抛出异常

  ```java
  String result = optionalString.orElse("default");
  String result = optionalString.orElseGet(()->System.getProperty("myapp.default"));
  String result = optionalString.orelseThrow(IllegalStateException::new); // 构造器引用
  ```

- 策略二，消费 Optional 的值：只有在值存在的时候，才消费该值

  - ifPresent(Consumer action)：值存在的时候，执行操作action
  - ifPresentOrElse(Consumer action, Runnable emptyAction)：值存在时，执行action，不存在时执行 emptyAction

  ```java
  // 例如，值存在时，加入集合。下述二者等价：
  optionalValue.ifPresent(v -> results.add(v));
  optionalValue.ifPresent(results::add);
  
  // 例如，值存在时，打印找到，否则日志报错
  optionalValue.ifPresentOrElse(
  	v -> logger.info("found" + v),
    () -> logger.error("not found"));
  ```

- 策略三，管道化 OPtional 的值：**保持当前 optional 的完整**，使用 map、filite、or等方法来转换其内部的值。

  - Optional map(Function mapper)：产生一个Optional，语义类似流的map。调用函数，得到返回值。值不存在时返回空的Optional。
  - OPtional filter(Predicate predicate)：产生一个Optional，过滤。同样值不存在时返回空
  - Optional or(Supplier supplier)：当前Optional不为空，则使用当前Optional的值产生新的Optional，否则由 supplier产生一个Optional。

  > 可以将optional对象看做是尺寸为 0 或 1的流。

  ```java
  // 使用map，执行函数，并将返回值返回给新的optional对象transformed
  Optional<String> transformed = optionalString.map(String::toUpperCase);
  
  OPtional<String> result = optionalString.or(()-> alternatives.stream().findFirst())
  ```

**将 Optional 转换为流**

没看懂 P17

# 约简

- P11 简单约简：例如count、min、max
- P32 复杂约简：reduce

# 流的结果收集

当处理完流后，通常想要查看其结果，此时有两种方式：

- iterator 方法，它会产生用来迭代访问元素的旧式风格的迭代器

- forEach 方法，将某个函数应用于每个元素。例如：stream.forEach(System.out::println)

  在并行流上，forEach方法会以任意的顺序遍历各个元素。如果想要按照流中的顺序遍历，可以调用forEachOrdered，该方法会丧失并行处理的部分甚至全部优势。

当然，更多情况下，我们希望把流的处理结果收集到数据结构中。

- toAarray()：返回一个Object[]数组，如果想让数组具有正确的类型，可以传入构造器。

  例如：`String[] result = stream.toArray(String[]::new);`

针对将流中的元素收集到另一个目标中，有一个便携方法 collect 可用，它会接受一个Collector接口的实例，亦称为收集器。

收集器是一种收集众多元素并产生单一结果的对象，Collectors 类提供了大量用于生成常见收集器：

- List\<String> result = stream.collect(Collectors.toList())
- Collectors.toSet()
- ...

详见P20

收集到映射表中见：P24

# TODO

- 群组和分区
- 基本类型流
- 并行流