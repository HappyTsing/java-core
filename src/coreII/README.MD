# Java 核心卷 II 代码重现

开始学习 Java 核心卷 II
## CH08 脚本、编译与注解处理

### 注解
注解是那些插入到源代码中使用其他工具可以对其进行处理的标签。这些工具可以在源码层次上进行操作，或者可以处理编译器在其中放置了注解的类文件。

注意：注解不会改变程序的编译方式。即Java编译器对于包含注解和不包含注解的代码会生成相同的虚拟机指令。

因此，为了让注解真正有效果，还需要一个 `处理工具` 。

如@Test注解本身不会做任何事，但Junit4测试工具可能会调用所有标识为@Test的方法，来对类进行测试。

#### 注解语法

```java
// 所有接口默认隐式扩展自java.lang.annotation.Annotation接口

// 如下定义一个注解

@Target(ElementType.XXX)
@Retention(RetentionPolicy.XXX)
@Documented
modifiers @interface AnnotationName {
    type elementName();

    type elementName() default value;
    ...
}
```
当定义注解的时候，往往会用到 `元注解`:
- @Target：限制该注解可以用到那些项上（类、方法、属性...）
- @Retention：一条注解应该保留多长时间，通常使用RUNTIME，此时可通过 `反射API` 获取注解
- @Document：JavaDoc归档
- @Inherited：注解可继承

注解和反射是许多框架的实现原理！他们通过添加注解，然后通过反射来干预代码的运行。