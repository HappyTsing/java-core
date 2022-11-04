# CH08 脚本、编译与注解处理

## 注解
注解是那些插入到源代码中使用其他工具可以对其进行处理的标签。这些工具可以在源码层次上进行操作，或者可以处理编译器在其中放置了注解的类文件。

注意：注解不会改变程序的编译方式。即Java编译器对于包含注解和不包含注解的代码会生成相同的虚拟机指令。

因此，为了让注解真正有效果，还需要一个 `处理工具` 。

如@Test注解本身不会做任何事，但Junit4测试工具可能会调用所有标识为@Test的方法，来对类进行测试。

### 注解语法

**1. 注解接口**

注解是由注解接口来定义的：


```java
// 所有接口默认隐式扩展自java.lang.annotation.Annotation接口

// 如下定义一个注解

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
modifiers @interface AnnotationName {
    
    // 若干个elementDeclaration，具有两种形式：
    type elementName();
    type elementName() default value;
    ...
}
```

永远不用为注解接口提供实现类。

当定义注解的时候，往往会用到 `元注解`:
- @Target：限制该注解可以用到那些项上（类、方法、属性...）
    - TYPE：类、接口、枚举、注解
    - FIELD：成员变量
    - METHOD：成员方法
    - PARAMETER：方法参数
    - CONSTRUCTOR：构造器
    - PACKAGE：包
    - ANNOTION_TYPE：注解，元注解都是这个！
- @Retention：一条注解应该保留多长时间，通常使用RUNTIME，此时可通过 `反射API` 获取注解
    - SOURCE: 编译期，编译器处理之后就不再保留，不会写入字节码中，例如@Override
    - CLASS：类加载期，注解信息保留到类对应的class文件中，但在运行class文件时被丢弃。
    - RUNTIME：运行期间，最常用，可以进行反射等操作。
- @Document：JavaDoc归档
- @Inherited：注解可继承

注解和反射是许多框架的实现原理！他们通过添加注解，然后通过反射来干预代码的运行。

**2. 使用注解**

每个注解都具有下面这种格式：
```java
@AnnotationName(elementName1=value1,elementName2=value2,...)
// 例如
@Test(Timeout="1000")
```
有两种情况下，可以简化注解：

- 标记注解：没有指定elementName，也就是说注解中没有任何的元素，或者所有元素都使用默认值，此时不需要使用小括号。例如：@Test，此时timeout使用默认值。
- 单值注解：注解接口中只有一个元素，且该元素的名称为 `value`，此时可以忽略元素名和等号。例如：@Test("1000")

> Todo：P377 8.4.3->5 注解各类声明、注解类型用法、注解this

### 标准注解

Java SE 在 java.lang、java.lang.annotation 和 javax.annotation 包中定义了大量的注解接口，其中四个是元注解，用于描述注解接口的行为属性，其他三个是规则接口，用来注解源代码中的项。
参见：P380

### 实现注解功能

注解是一个标记，如果想要真正实现注解的功能，需要通过反射获取注解的属性，然后进行一定的操作。详细见代码 `CH08`