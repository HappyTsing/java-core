# CH02 输入与输出

## 补课：码点与代码单元

> coreI CH03 码点与代码单元 P32 48

Java中的char默认使用Unicode编码，占用两个字节，可以表示为十六进制值，Java中十六进制的前缀是`\u`，char 的取值范围为：`\u0000 `到 `\uFFFF`。

但是随着Unicode的不断扩展，Unicode 超过了65536个字符，其主要原因是加入了大量的汉语、日语和韩语中的表意文字。

于是，两个字节（16位）的 char 类型已经不能满足描述所有 Unicode 字符的需要了。例如 𝕆 这个字符，char 是无法容纳的。

Java 5开始为了解决该问题，引入概念：

- **码点**：采用十六进制书写，并加上前缀`U+`。Unicode代码空间中的一个值，码点取值范围从 `U+00000` 到 `U+10FFFF`。

  - **代码平面**：Unicode的码点分为十七个代码平面，其实就是把取值范围分为十七个部分:

    - 0号代码平面，又称为**基本多文种平面（BMP）**，其存放了范围是 `U+0000` 到 `U+FFFF` 的经典Unicode代码。

      UTF-16 编码：在基本多语言平面中，的每个字符用16位表示，通常称为**代码单元**，简称**码元**。

      Java中，char 类型描述了 UTF-16 编码中的一个代码单元。

      String类型由 char 值序列组成。

    - 其余16平面都称为**辅助平面**。

      - 1号平面：范围是 `U+10000` 到 `U+1FFFF`，又称为**多文种补充平面（SMP）**
      - ...

再次谈论𝕆，其码点为：`U+1D546	`，转化为代码单元为 `U+D835` 和 `U+DD46`，编码转换算法[见](https://tools.ietf.org/html/rfc2781)，在线转换网址[见](https://www.qqxiuzi.cn/bianma/Unicode-UTF.php)。

Java 中 char 两个字节，因此无法容纳四个字节的𝕆。而 String 又是 Char 的序列，因此采用 String 存储即可：

```java
char a = '𝕆'       // Too many characters in character literal
String word = "𝕆"; // Success
```

核心卷建议不要操作过于底层的 char，而采用String。

### Unicode、UTF-8、UTF-16

**Unicode** 是容纳世界所有文字符号的国际标准编码，使用**四个字节**为每个字符编码。

**UTF** 是英文 Unicode Transformation Format 的缩写，意为把 Unicode 字符转换为某种格式。UTF 系列编码方案（UTF-8、UTF-16、UTF-32）均是由 Unicode 编码方案衍变而来，以适应不同的数据存储或传递，它们都可以完全表示 Unicode 标准中的所有字符。目前，这些衍变方案中 UTF-8 和 UTF-16 被广泛使用，而 UTF-32 则很少被使用。

**UTF-8** 使用一至四个字节为每个字符编码，是可变长度的编码方式，其相对于 Unicode 编码可以减少存储占用的空间，所以被广泛使用，对于汉字的编码：

- BMP 平面的汉字：三个字节
- 辅助平面的汉字：四个字节

**UTF-16** 使用二或四个字节为每个字符编码，对于汉字的编码：

- BMP 平面的汉字：两个字节
- 辅助平面的汉字：四个字节

UTF-16 编码有大尾序和小尾序之别，即 UTF-16BE 和 UTF-16LE，在编码前会放置一个 U+FEFF 或 U+FFFE（UTF-16BE 以 FEFF 代表，UTF-16LE 以 FFFE 代表），其中 U+FEFF 字符在 Unicode 中代表的意义是 ZERO WIDTH NO-BREAK SPACE，顾名思义，它是个没有宽度也没有断字的空白。

**Java内部采用UTF-16编码方式，且采用大尾序**

仍旧以一号辅助平面SMP的字符𝕆为例，讲解Java中各种概念，首先通过在线转换网址获取𝕆的各种编码数据：

```
字符: 𝕆
Unicode编码: 0001D546
UTF8编码: F09D9586
UTF16BE编码: FEFFD835DD46
UTF16LE编码: FFFE35D846DD
UTF32BE编码: 0000FEFF0001D546
UTF32LE编码: FFFE000046D50100
```

可以看到，`码点 `就是代表着 `Unicode编码`，也就是 Unicode 代码空间中的一个值。

`代码单元`其实就是`UTF-16BE编码`，去除其代码大尾序的前缀后，将剩余两个字节分为两个代码单元。

另外值得注意的是，UTF-16的汉字最少只需要2个字节，而UTF-8的汉字至少需要3个字节

```
字符：卿
Unicode编码：0000537F
UTF8编码：E58DBF
UTF16BE编码：FEFF537F
UTF16LE编码：FFFE7F53
```

##  IO流简介

IO 即 `Input/Output`，输入和输出。数据输入到计算机内存的过程即输入，反之输出到外部存储（比如数据库，文件，远程主机）的过程即输出。数据传输过程类似于水流，因此称为 IO 流。IO 流在 Java 中分为输入流和输出流，而根据数据的处理方式又分为字节流和字符流。

Java IO 流的 40 多个类都是从如下 4 个抽象类基类中派生出来的。

- `InputStream`/`Reader`: 所有的输入流的基类，前者是字节输入流，后者是字符输入流。
- `OutputStream`/`Writer`: 所有输出流的基类，前者是字节输出流，后者是字符输出流。

## 字节输入流

![InputStream](https://img-blog.csdnimg.cn/2019090909411795.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMzIyMTAz,size_16,color_FFFFFF,t_70)

`java.io.InputStream` 抽象类是所有字节输入类的父类，其提供一个抽象方法 read() 和若干个非抽象的方法：

- `abstract int read()`：子类必须实现该方法。该方法将读入一个字节，并返回读入的字节，或者在遇到输入源结尾时返回-1
- `read(byte b[ ])` : 从输入流中读取一些字节存储到数组 `b` 中。如果数组 `b` 的长度为零，则不读取。如果没有可用字节读取，返回 `-1`。如果有可用字节读取，则最多读取的字节数最多等于 `b.length` ， 返回读取的字节数。这个方法等价于 `read(b, 0, b.length)`。
- `read(byte b[], int off, int len)` ：在`read(byte b[ ])` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。
- `byte[] readAllBytes()` ：读取输入流中的所有字节，返回字节数组。
- `readNBytes(byte[] b, int off, int len)` ：阻塞直到读取 `len` 个字节。
- `long transferTo(OutputStream out)` ： 将所有字节从一个输入流传递到一个输出流。
- `long skip(long n)` ：忽略输入流中的 n 个字节 ,返回实际忽略的字节数。
- `available()` ：返回输入流中可以读取的字节数。
- `close()` ：关闭输入流释放相关的系统资源。

下面对各种各种子类做简要的介绍：

首先讲解直接子类：

- AudioInputStream：具有音频格式的输入流，长度以样本帧表示，而不是字节。

- ByteArrayInputStream：在内存中创建一个字节数组缓冲区，从输入流读取的数据保存在该字节数组缓冲区中。

- **FileInputStream**：使用由String字符串或**File**对象指定路径名的文件传建一个新的文件输入流。

- PipedInputStream：PipedInputStream和PipedOutStream是java语言中线程间传输数据的一种方式。A线程使用PipedOutStream写数据，B线程使用PipedInputStream接收数据。

- **FilterInputStream**：其构造类为`FilterInputStream(InputStream in)`，其接受流，并对其进行转化。本身仅重写父类InputStream的方法，并将其转发给接受的流，例如：

  ```java
  public int read() throws IOException {
    return in.read();
  }
  ```

  其子类会进一步覆盖这些方法，同时提供一些新的方法。后续详解。

- SequenceInputStream：以把多个 InputStream 合并为一个 InputStream . 按照指定的顺序，把几个输入流连续地合并起来，使得使用起来像一个流一样。

- ~~StringBufferInputStream：在JDK11中已经废弃：This class does not properly convert characters into bytes. As of JDK 1.1, the preferred way to create a stream from a string is via the StringReader class.~~

- **ObjectInpuStream**：用于从输入流中读取 Java 对象（反序列化）

---

**嵌套过滤器**

在其他编程语言的输入/输出流类库中，诸如缓冲机制和预览等细节都是自动处理的，而Java则相对麻烦，使用**流过滤器**来分离职责，必须将其组合起来才能实现各种功能。

例如某些输入流（例如 FileInputStream）可以从文件中获取字节，而其他的输入流（例如DataInputStream）可以将字节组装到更有用的数据类型中。Java程序员必须对这二者进行组合。

此外，可以**通过嵌套过滤器输入流来添加多重功能**。

FilterInputStream 的子类中，较为常用的有：

- DataInputStream：以二进制格式读所有的基本Java类型
- BufferedInputStream：创建一个带缓冲区的输入流。带缓冲区的输入流在从流中读入字符时，不会每次都访问设备。当缓存区为空时，会向缓冲区读入一个新的数据块
- PushbackInputStream：创建一个可以预览一个字节或具有指定尺寸的回退缓冲区的输入流
- ZipInputStream：以压缩格式读文件
- JarInputStream：

<u>示例一</u>

下面这段代码在我们的项目中就比较常见，首先通过 FileInputStream 获取文件字节流，然后过滤器流 BufferedInputStream 为其赋予缓冲机制，使得程序更加高效。最后，通过 `readAllBytes()` 读取输入流所有字节并将其直接赋值给一个 `String` 对象。

```java
// 新建一个 BufferedInputStream 对象
BufferedInputStream bin = new BufferedInputStream(new FileInputStream("input.txt"));
// 读取文件的内容并复制到 String 对象中
String result = new String(bin.readAllBytes());
System.out.println(result);
```

<u>示例二</u>

当然，也可以进行多重嵌套，此处将DataInputStream置于构造器链的最后，是因为我们希望使用DataInputStream的方法，并且希望它们能够使用带缓冲机制的read方法，即从文件中读取指定类型的数据：

```java
DataInputStream din = new DataInputStream(
  												new BufferedInputStream(
                              new FileInputStream("input.txt")));
//可以读取任意具体的类型数据
din.readBoolean();
din.readInt();
din.readUTF();
```

<u>示例三</u>

有时需要预览下一个字节（或若干个），以了解它是否是你想要的值，此时使用过滤器流 PushbackInputStream：

```java
PushbackInputStream  pbin = new PushbackInputStream(
																new BufferedInputStream(
                                		new FileInputStream("input.txt")));
int b = pbin.read(); // 预读下一个字节
if(b != '<') pbin.unread(b); // 预读字节并非期望值，将其推回流中
```

读入和推回是PushbackInputStream**仅有**的方法，如果希望能够预先浏览并且还可以读入数字，那么需要一个既是可回推输入流，又是一个数据输入流的引用：

```java
DataInputStream din = new DataInputStream(pbin);
```

<u>示例四</u>

从ZIP压缩文件中通过组合流过滤器，来读入数字：

```java
ZipInpuStream zin = new ZipInpuStream(new FileInputStream("input.zip"));
DataInputStream din = new DataInputStream(zin);
```

此外，ZIP文档通常以压缩格式存储了一个或多个文件，每个ZIP文档都有一个头，包含诸如每个文件名字和所使用的压缩方法等信息。

```java
var zin = new ZipInputStream(new FileInputStream("input.zip"));
ZipEntry entry;
while((entry = zin.getNextEntry()) != null)
{
  // read the content of zin
  zin.closeEntry();
}
zin.close();
```

每个单独的文档称为一个项，`getNextEntry()` 可以返回一个描述这些项的 ZipEntry 类型的对象。

在循环中使用 `closeEntry()` 进入下一项，在读完最后一项后，调用`close()` 关闭 zin。

此外，Jar包是特殊的ZIP文件，称为清单，可以使用 `JarInputStream/JarOutputStream` 进行读写清单项。

## 字节输出流

![OutputStream](https://img-blog.csdnimg.cn/20190909122613665.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMzIyMTAz,size_16,color_FFFFFF,t_70)

`java.io.OutputStream` 抽象类是所有字节输入类的父类，其提供一个抽象方法 write() 和若干个非抽象的方法：

- `abstract write(int b)` ：子类需要实现该类。写一个字节的数据。

- `write(byte b[ ])` : 将数组`b` 写入到输出流，等价于 `write(b, 0, b.length)` 。

- `write(byte[] b, int off, int len)` : 在`write(byte b[ ])` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。

- `flush()` ：刷新此输出流并强制写出所有缓冲的输出字节。在FileOutputStream中是空实现，在BufferedOutputStream中进行了实现：

  ```java
  @Override
  public synchronized void flush() throws IOException {
    flushBuffer();
    out.flush();
  }    
  private void flushBuffer() throws IOException {
    if (count > 0) {
      out.write(buf, 0, count);
      count = 0;
    }
  }
  // OutPutStream中定义方法，空实现
  public void flush() throws IOException {
  }
  ```

- `close()` ：关闭输出流释放相关的系统资源，同时会重刷该输出流的缓存区。

## ☆文本输入与输出

> 将知识点串起来，非常重要！

在计算机中，任何东西都是二进制的，本质上来说，**流就是不间断的01序列**。

因此，无论是磁盘上的文件，还是网络传输的流，都是01序列！

刚好IO的操作又分为两种：**读入和写出**。读入可以是读磁盘的文件，读网络传输过来的流；写出可以是将流写入磁盘文件中，或传输给另一个网络地址。

但是，无论是输出流，还是输入流，都只是01序列而已。人类世界是不认可01序列的，因此需要为01序列赋予意义！

什么是意义呢？也就是人类的语言。例如英语中的abc，都是**字符**。当然，汉语中的每个字也称为**字符**。

于是，就引申出了**字符编码**，也就是说，将人类语言中的字符，用01序列表示！

刚开始发明计算机的是美国人，由于英语只有26个字母，再加上一些符号，因此8位01序列足以编码他们的整个语言，于是发明了8位，即1个字节的ASCII编码。

随后，随着越来越多的国家加入互联网，需要一种大家都能用的编码方式，Unicode应运而生，它使用四个字节为每个字符编码。

但是，四个字节显然太大了，于是出现了几种衍变方案，即UTF系列，UTF是英文 Unicode Transformation Format 的缩写，意为把 Unicode 字符转换为某种格式：UTF-8、UTF-16。

UTF-8由于其轻量性，常作为网络传输使用的编码方式，而Java内部采用UTF-16作为编码方式。因此，Java所谓的**二进制文本**，其实就是将UTF-16转换为UTF-8，**不过由于Java转换的UTF-8为了向后兼容早期的JVM，其对UTF-8编码进行了修订**，因此与标准的UTF-8编码方式对大于0xFFFF的字符的处理有所不同，例如𐐷。(core2 P62)。

例如：

```java
var out = new DataOutputStream((new FileOutputStream("test_utf.txt")));
out.writeUTF("卿");
hexdump test_utf.txt   //查看二进制代码
00000000  00 03 e5 8d bf
00000005
```

其中`00 03`表示该共有3个字节，此时通过该[网站](https://www.qqxiuzi.cn/bianma/Unicode-UTF.php)查看`卿`的各种编码：

```
Unicode编码：0000537F
UTF8编码：E58DBF
UTF16BE编码：FEFF537F
UTF16LE编码：FFFE7F53
```

可以看到，使用Java转换二进制的方法转换后的结果，和UTF-8编码是相同的！

虽然在此处，UTF-16的编码只有2个字节，但UTF-8有3个字节，这是由于这两种编码方式对中文的编码不同导致的，不过UTF-8包含了英语中用到的所有字符的ASCII字符集中的每个字符，在UTF-8字符集中只占用1个字节，而互联网上传输的往往就是这些字符，因此UTF-8相对来说还是更加轻量的！

一个字节就是8个比特，这是硬件决定的，而8个比特又刚好可以表示所有的传统英语字符，这是历史决定的。

于是（个人猜想），字节输入流和输出流就应运而生，他们每次调用read()方法时，刚好读取8位，也就是一个字节！

但是很不幸，世界上有那么多国家，比如中文起码有两个字节，因此使用字节流（InputStream/OutputStream）处理就很麻烦，于是Java引入了字符输入与输出（Reader/Writer），其及子类的读入和写出操作都是基于**两个字节**的char值（即 Unicode 码元）。

**Java字符串采用UTF-16的编码方式**。

> 这意味着，如果某个符号其是四个字节，例如𐐷，其UTF-16编码为D801DC37，那么使用Reader/Writer也无法正确获取，而是将其分成两个部分：D801、DC37分别进行输入和输出。
>
> ```java
> 对应字符：𐐷
> Unicode编码：00010437
> UTF-8编码：F09090B7
>   
> public static void main(String[] args) throws IOException {
>   var out = new PrintWriter("src/coreII/CH02/leki.txt", StandardCharsets.UTF_8);
>   out.print("𐐷");
>   // out.print("\uD801\uDC37");
>   out.flush();
> 
>   var in = new InputStreamReader(
>     new FileInputStream("src/coreII/CH02/leki.txt"), StandardCharsets.UTF_8);
>   int content;
>   while((content = in.read()) != -1){
>     System.out.printf("%x\n", content); // 16进制输出
>   }
> }
> 
> // 使用Reader.read()读入时将自动转换为Java内部使用的UTF-16编码格式
> 结果为：
> d801
> dc37
> 
> // 代码中定义写出为UTF-8的编码格式，因此保存的文件结果为f0 90 90 b7
> hexdump -C leki.txt   
> 00000000  f0 90 90 b7
> 00000004
> ```

此处介绍一个概念：**二进制格式和文本格式**

- 二进制格式已经可以理解，就是01序列，此时的01序列的意义是未经定义的，也就是说，二进制格式的编码方式是自定义的。

  于是，二进制格式可以理解为自定义编码方式的文本格式！

  例如，我可以将`101`序列认为是文本`↑↓↑`，即我认为`↑`的编码为`1`，而`↓`的编码为`0`，这一切都取决于你的定义！

  Java所谓读写二进制数据，其实就是自定义了编码方式！(core2/P61)

- 文本格式就是根据字符编码方式，来将字符串、数字等内容，转换为对应的01序列。例如，当字符编码方式为UTF-16时，"A"的存储结果的十六进制形式为 `FEFF0041`，而当字符编码方式为UTF-8或ASCII时，其01序列的十六进制形式为`41`。

文本格式的输入和输出使用**字符输入/输出流即可实现**，二进制格式的读写在Java中使用**DataOutput/DataInput接口**定义了用于以二进制格式写数组、字符、boolean值和字符串的方法，例如：writeChars、wtriteUTF、readChar、ReadUTF...

在字节输入流/输出流家族中，其中的**DataInputStream/DataOutputStream类实现了DataInput/DataOutput接口**，因此，想要读写二进制数据，可以使用这两个类。

例如，为了从文件中读入二进制数据，可以将DataInputStream与某个二进制数据源组合，例如FileInputStream：

```java
var in = new DataInputStream(new FileInputStream("input.txt"));
```

于此类似，想要写出二进制数据，使用实现了DataOutput接口的DataOutputStream类即可：

```java
var out = new DataOutputStream(new FileInputStream("output.txt"))
```

那么，Java的二进制格式的编码方式是如何自定义的呢？

根据提供的不同的方法，会有不同的方式，例如：

- writeInt：总是将一个整数写成为 4 字节的二进制数量值，不管他有多少位。例如 1234 -> 000004D2
- writeDouble：总是将一个 double 值写出为 8 字节 的二进制数量值
- WriteUTF：使用修订版的 8 位 Unicode 转换格式写出字符串，为了兼容古老的虚拟机，该修订版与标准的UTF-8在大于 0xFFFF 的字符的处理有所不同。


---

**随机访问文件**

RandomAccessFile 类可以在文件中的任何位置查找或写入数据。磁盘文件都是随机访问的，但是与网络套接字通信的输入/输出流却不是！

此外，RandomAccessFile也实现了**DataOutput/DataInput接口**。

你可以打开一个随机访问文件，只用于读入或者同时用于读写：

```java
var in = new RandomAccessFile("input.txt","r");
var in = new RandomAccessFile("input.txt","rw");
```

随机访问文件有一个表示下一个将被读入或写出的字节所处位置的**文件指针**，`seek(long pos)` 方法可以用来将文件指针设置到文件中任意字节位置，`pos`的值位于0到文件按照字节度量的长度之间。`getFilePointer()` 返回文件指针的当前位置。

## 字符输入/输出流

![Reader](https://img-blog.csdnimg.cn/20190909152758854.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMzIyMTAz,size_16,color_FFFFFF,t_70)

`Reader`用于从源头（通常是文件）读取数据（字符信息）到内存中，`java.io.Reader`抽象类是所有字符输入流的父类。

`Reader` 用于读取文本， `InputStream` 用于读取原始字节。

`Reader` 常用方法 ：

- `read()` : 从输入流读取一个字符。
- `read(char[] cbuf)` : 从输入流中读取一些字符，并将它们存储到字符数组 `cbuf`中，等价于 `read(cbuf, 0, cbuf.length)` 。
- `read(char[] cbuf, int off, int len)` ：在`read(char[] cbuf)` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。
- `skip(long n)` ：忽略输入流中的 n 个字符 ,返回实际忽略的字符数。
- `close()` : 关闭输入流并释放相关的系统资源。

`InputStreamReader` 是**字节流转换为字符流的桥梁**，它将包含字节（用某种字符编码方式表示的字符）的输入流转换为可以产生 Unicode 码元的读入器。

其子类 `FileReader` 是基于该基础上的封装，可以直接操作字符文件。

![Writer](https://img-blog.csdnimg.cn/20190909155331862.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQyMzIyMTAz,size_16,color_FFFFFF,t_70)

`Writer`用于将数据（字符信息）写入到目的地（通常是文件），`java.io.Writer`抽象类是所有字节输出流的父类。

`Writer` 常用方法 ：

- `write(int c)` : 写入单个字符。
- `write(char[] cbuf)` ：写入字符数组 `cbuf`，等价于`write(cbuf, 0, cbuf.length)`。
- `write(char[] cbuf, int off, int len)` ：在`write(char[] cbuf)` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。
- `write(String str)` ：写入字符串，等价于 `write(str, 0, str.length())` 。
- `write(String str, int off, int len)` ：在`write(String str)` 方法的基础上增加了 `off` 参数（偏移量）和 `len` 参数（要读取的最大字节数）。
- `append(CharSequence csq)` ：将指定的字符序列附加到指定的 `Writer` 对象并返回该 `Writer` 对象。
- `append(char c)` ：将指定的字符附加到指定的 `Writer` 对象并返回该 `Writer` 对象。
- `flush()` ：刷新此输出流并强制写出所有缓冲的输出字符。
- `close()`:关闭输出流释放相关的系统资源。

`OutputStreamWriter` 是**字符流转换为字节流的桥梁**，它使用使用选定的字符编码方式，把 Unicode 码元的输出流转化为字节流。

其子类 `FileWriter` 是基于该基础上的封装，可以直接将字符写入到文件。

**读入器Reader读入流并转换为字符流**

从控制台读入键盘敲击信息，并将其转换为 UTF-8

```java
// 假定字符编码方式为系统默认的编码方式
InputStreamReader rin = new InputStreamReader(System.in);

// 应该总是指定一种具体的编码方式
InputStreamReader rin = new InputStreamReader(System.in, StandardCharsets.UTF_8);
```

或从文件中读入字节流

```java
InputStreamReader rin = new InputStreamReader(
														new FileInputStream("input.txt"),StandardCharsets.UTF_8);
```

**写出器Writer写出为文本格式**

PrintWriter 类拥有以文本格式打印字符串和数字的方法，其拥有多个构造方法，可以通过输入一个String filename、Writer、OutPutStream或者File，都可以构造出PrintWriter实例：

使用 print、println和printf 方法打印数字、字符、布尔值、字符串和对象。例如：

```java
PrintWriter out = PrintWriter("output.txt", StandardCharsets.UTF_8);
out.print("test");
out.print(person);
```

此后，字符将根据给定的编码格式转换为字节并写入 `output.txt`中。

<u>自动冲刷模式</u>

默认关闭，开启后只要println被调用，缓冲区中的所有字符都会被发送到它们的目的地（打印写出器总是待缓冲区的）。

```java
PrintWriter out = PrintWriter(new OutPutStreamWriter(
																	new FileOutPutStream("output.txt"), StandardCharsets.UTF_8),
                              true); // autoflush
```

## 流家族接口介绍

![DataIOInterface](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/DataIOInterface.png)

上图的接口主要用于

- 二进制数据的输入和输出：前文已讲解
- 对象序列化：下文详解

![IOInterface](https://happytsing-figure-bed.oss-cn-hangzhou.aliyuncs.com/java_core/IOInterface.png)

结合Java核心卷 `coreI/CH07 ` P292 try-with-resources 语句的相关知识，实现了 `AutoCloseable` 接口的类，可以使用：

```java
try(Resource res = ...)
{
  // work with res
}
```

其中接口  `Closeable `是`Autocloseable` 的子类，不同之处在于前者只抛出IOException，而后者可以抛出任意异常。

如图所示：

- InputStream、OutputStream、Reader和Writer 都实现了 `Closeable` 接口，因此都能使用 try-with-resources 语句。
- OutputStream 和 Writer 都实现了 `Flushable` 接口，用于输出是冲刷缓存。
- Reader 和 CharBuffer 都实现了 `Readable` 接口，该接口只有一个方法：`int read(CharBuffer cb)`，`CharBuffer` 类拥有按顺序和随机地进行读写访问的方法，它表示一个内存中的缓冲区或者一个内存映像文件。
- Writer、StringBuffer 和 CharBuffer 都实现了 `Appendable` 接口，该接口有两个用于添加单个字符和字符序列的方法：

  - `Appendable append(char c)`
  - `Appendable append(CharSequence s)`

  `CharSequence` 接口描述了一个 char 值序列的基本属性，String、CharBuffer、StringBuilder 和 StringBuffer 都实现了它。

## 序列化和反序列化

## 操作文件

输入/输出流关心的是文件的内容，此处操作文件的类（Files、Path）关注的是文件在磁盘上的存储和管理，例如重命名、移动、查询修改时间等。

`java.nio.file.Files`和`java.nio.file.Path`在 Java 7 中新添加进来，比 JDK1.0  就引入的`java.io.File` 使用起来更加方便。

**Path**



**Files**

## 内存映射文件

## 文件加锁机制

## 正则表达式

# BIO、NIO、AIO

## 计算机角度看IO

根据冯.诺依曼结构，计算机结构分为 5 大部分：运算器、控制器、存储器、输入设备、输出设备。

![冯诺依曼计算机结构](https://camo.githubusercontent.com/857f334445f9f4207ae563382f86b5bf11d451cd1b14a6278c7d913178115c9e/68747470733a2f2f696d672d626c6f672e6373646e696d672e636e2f32303139303632343132323132363339382e6a7065673f782d6f73732d70726f636573733d696d6167652f77617465726d61726b2c747970655f5a6d46755a33706f5a57356e6147567064476b2c736861646f775f31302c746578745f6148523063484d364c7939706379316a624739315a4335696247396e4c6d4e7a5a473475626d56302c73697a655f31362c636f6c6f725f4646464646462c745f3730)

输入设备（比如键盘）和输出设备（比如显示器）都属于外部设备。网卡、硬盘这种既可以属于输入设备，也可以属于输出设备。

输入设备向计算机输入数据，输出设备接收计算机输出的数据。

**从计算机结构的视角来看的话， I/O 描述了计算机系统与外部设备之间通信的过程。**

## 操作系统角度看IO

![数据的内核态和用户态](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510747-754105974.png)

根据操作系统相关的知识：为了保证操作系统的稳定性和安全性，一个进程的地址空间划分为 **用户空间（User space）** 和 **内核空间（Kernel space ）** 。

像我们平常运行的应用程序都是运行在用户空间，只有内核空间才能进行系统态级别的资源有关的操作，比如文件管理、进程通信、内存管理等等。也就是说，我们想要进行 IO 操作，一定是要依赖内核空间的能力。

并且，用户空间的程序不能直接访问内核空间。

当想要执行 IO 操作时，由于没有执行这些操作的权限，只能发起系统调用请求操作系统帮忙完成。

因此，用户进程想要执行 IO 操作的话，必须通过 **系统调用** 来间接访问内核空间

我们在平常开发过程中接触最多的就是 **磁盘 IO（读写文件）** 和 **网络 IO（网络请求和响应）**。

**从应用程序的视角来看的话，我们的应用程序对操作系统的内核发起 IO 调用（系统调用），操作系统负责的内核执行具体的 IO 操作。也就是说，我们的应用程序实际上只是发起了 IO 操作的调用而已，具体 IO 的执行是由操作系统的内核来完成的。**

应用程序发起的一次IO操作实际包含两个阶段：

1. IO调用阶段：应用程序进程向内核发起系统调用
2. IO执行阶段：内核执行IO操作并返回
   1. 准备数据阶段：内核等待I/O设备准备好数据
      1. 对于写请求：等待系统调用的完整请求数据，并写入内核缓冲区；
      2. 对于读请求：等待系统调用的完整请求数据；（若请求数据不存在于内核缓冲区）则将外围设备的数据读入到内核缓冲区。
   2. 拷贝数据阶段：将数据从内核缓冲区拷贝到用户空间缓冲区

应用程序发起系统调用的方法是，调用系统提供的接口：

> man <f_name> 查看系统调用

- socketcall socket系统调用
- socket 建立socket
- bind 绑定socket到端口
- connect 连接远程主机
- accept 响应socket连接请求
- send 通过socket发送信息
- sendto 发送UDP信息
- recv 通过socket接收信息
- recvfrom 接收UDP信息
- listen 监听socket端口
- select/poll、epoll 实现IO多路复用
- shutdown 关闭socket上的连接
- sigaction 设置对指定信号的处理方法

## UNIX 的五种IO模型

UNIX 系统下， IO 模型一共有 5 种： **阻塞 I/O**、**非阻塞 I/O**、**I/O 多路复用**、**信号驱动 I/O** 和**异步 I/O**。

常用的是阻塞IO和I/O多路复用，真正的异步I/O只有Windows平台下的IOCP技术实现，Linux上底层还是用epoll实现的。epoll是实现I/O多路复用的技术。

### 阻塞I/O

应用程序中进程在发起IO调用后至内核执行IO操作返回结果之前，若发起系统调用的线程一直处于等待状态，则此次IO操作为阻塞IO。阻塞IO简称BIO，Blocking IO。其处理流程如下图所示：

![BIO](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510664-625360599.png)

从上图可知当用户进程发起IO系统调用后，内核从准备数据到拷贝数据到用户空间的两个阶段期间**用户调用线程选择阻塞等待**数据返回。

因此BIO带来了一个问题：如果内核数据需要耗时很久才能准备好，那么用户进程将被阻塞，浪费性能。为了提升应用的性能，虽然可以通过多线程来提升性能，但线程的创建依然会借助系统调用，同时多线程会导致频繁的线程上下文的切换，同样会影响性能。所以要想解决BIO带来的问题，我们就得看到问题的本质，那就是**阻塞**二字。

### 非阻塞I/O

那解决方案自然也容易想到，将阻塞变为非阻塞，那就是用户进程在发起系统调用时指定为非阻塞，内核接收到请求后，就会立即返回，然后用户进程通过轮询的方式来拉取处理结果。也就是如下图所示：

![NIO](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510967-1828941712.png)

应用程序中进程在发起IO调用后至内核执行IO操作返回结果之前，若发起系统调用的线程不会等待而是立即返回，则此次IO操作为非阻塞IO模型。非阻塞IO简称NIO，Non-Blocking IO。

然而，非阻塞IO虽然相对于阻塞IO大幅提升了性能，但依旧不是完美的解决方案，其依然存在性能问题，也就是频繁的轮询导致频繁的系统调用，会耗费大量的CPU资源。比如当并发很高时，假设有1000个并发，那么单位时间循环内将会有1000次系统调用去轮询执行结果，而实际上可能只有2个请求结果执行完毕，这就会有998次无效的系统调用，造成严重的性能浪费。有问题就要解决，那**NIO问题的本质就是频繁轮询导致的无效系统调用**。

### I/O多路复用

#### select/poll

Select是内核提供的系统调用，它支持一次查询多个系统调用的可用状态，当任意一个结果状态可用时就会返回，用户进程再发起一次系统调用进行数据读取。换句话说，就是NIO中N次的系统调用，借助Select，只需要发起一次系统调用就够了。其IO流程如下所示：
![I/O Multiplexing](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510633-1284554925.png)

但是，select有一个限制，就是存在连接数限制，针对于此，又提出了poll。其与select相比，主要是解决了连接限制。

select/epoll 虽然解决了NIO重复无效系统调用用的问题，但同时又引入了新的问题。问题是：

1. 用户空间和内核空间之间，大量的数据拷贝
2. 内核循环遍历IO状态，浪费CPU时间

换句话说，select/poll虽然减少了用户进程的发起的系统调用，但内核的工作量只增不减。在高并发的情况下，内核的性能问题依旧。所以select/poll的问题本质是：内核存在无效的循环遍历。

#### epoll

针对select/pool引入的问题，我们把解决问题的思路转回到内核上，如何减少内核重复无效的循环遍历呢？变主动为被动，基于事件驱动来实现。其流程图如下所示：

![epoll](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510688-699537399.png)

epoll相较于select/poll，多了两次系统调用，其中epoll_create建立与内核的连接，epoll_ctl注册事件，epoll_wait阻塞用户进程，等待IO事件。

![select,poll,epoll](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510883-1267943094.png)

epoll，已经大大优化了IO的执行效率，但在IO执行的第一阶段：数据准备阶段都还是被阻塞的。所以这是一个可以继续优化的点。

### 信号驱动I/O

信号驱动IO与BIO和NIO最大的区别就在于，在IO执行的数据准备阶段，不会阻塞用户进程。
如下图所示：当用户进程需要等待数据的时候，会向内核发送一个信号，告诉内核我要什么数据，然后用户进程就继续做别的事情去了，而当内核中的数据准备好之后，内核立马发给用户进程一个信号，说”数据准备好了，快来查收“，用户进程收到信号之后，立马调用recvfrom，去查收数据。

![SIGIO](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510676-1445375786.png)

乍一看，信号驱动式I/O模型有种异步操作的感觉，但是在IO执行的第二阶段，也就是将数据从内核空间复制到用户空间这个阶段，用户进程还是被阻塞的。

综上，你会发现，不管是BIO还是NIO还是SIGIO，它们最终都会被阻塞在IO执行的第二阶段。
那如果能将IO执行的第二阶段变成非阻塞，那就完美了。

### 异步I/O

异步IO真正实现了IO全流程的非阻塞。用户进程发出系统调用后立即返回，内核等待数据准备完成，然后将数据拷贝到用户进程缓冲区，然后发送信号告诉用户进程**IO操作执行完毕**（与SIGIO相比，一个是发送信号告诉用户进程数据准备完毕，一个是IO执行完毕）。其流程如下：
![AIO](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510923-45783458.png)

所以，之所以称为异步IO，取决于IO执行的第二阶段是否阻塞。因此前面讲的BIO，NIO和SIGIO均为同步IO。

![img](https://img2022.cnblogs.com/blog/577140/202211/577140-20221104170510662-1066833223.png)

## 同步与异步

同步和异步指的是一个执行流程中每个方法是否必须依赖前一个方法完成后才可以继续执行。假设我们的执行流程中：依次是方法一和方法二。

同步指的是调用一旦开始，调用者必须等到方法调用返回后，才能继续后续的行为。即方法二一定要等到方法一执行完成后才可以执行。

异步指的是调用立刻返回，调用者不必等待方法内的代码执行结束，就可以继续后续的行为。（具体方法内的代码交由另外的线程执行完成后，可能会进行回调）。即执行方法一的时候，直接交给其他线程执行，不由主线程执行，也就不会阻塞主线程，所以方法二不必等到方法一完成即可开始执行。

同步与异步关注的是方法的执行方是主线程还是其他线程，主线程的话需要等待方法执行完成，其他线程的话无需等待立刻返回方法调用，主线程可以直接执行接下来的代码。

同步与异步是从多个线程之间的协调来实现效率差异。

**在五种IO模型中，除了异步I/O，其余四个全是同步I/O。**

## 阻塞与非阻塞

阻塞与非阻塞指的是单个线程内遇到同步等待时，是否在原地不做任何操作。

阻塞指的是遇到同步等待后，一直在原地等待同步方法处理完成。

非阻塞指的是遇到同步等待，不在原地等待，先去做其他的操作，隔断时间再来观察同步方法是否完成。

阻塞与非阻塞关注的是线程是否在原地等待。

# NETTY

# Questions

Java 使用二进制会更快吗？似乎不会，因为0并不会消失。文本格式的1比正常的1还小！

# Reference

- [漫话：如何给女朋友解释什么是Linux的五种IO模型？](https://mp.weixin.qq.com/s?__biz=Mzg3MjA4MTExMw==&mid=2247484746&idx=1&sn=c0a7f9129d780786cabfcac0a8aa6bb7&source=41#wechat_redirect)
- [浅聊Linux的五种IO模型](https://segmentfault.com/a/1190000039898780)
- [JavaGuide-io模型](https://github.com/Snailclimb/JavaGuide/blob/main/docs/java/io/io-model.md)
- [☆IO模型知多少-select/poll/epoll](https://www.cnblogs.com/sheng-jie/p/how-much-you-know-about-io-models.html)
