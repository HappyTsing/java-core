package coreII.CH02;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 字符输入流/输出流：InputStream/OutputStream
 * 测试嵌套过滤器流，并提供了几个常用的代码案例。
 * 值得注意的是，当出现中文时，使用read()方法获取数据，并用sout(char(b))时，将会输出乱码。
 * 其原因是read()方法返回单个字节，在Java内部采用的UTF-16编码中，中文采用2个字节存储，因此无法输出。
 * 此外，当readAllBytes()，并将其保存为String类型时，即可正常输出。其原因是String是char的序列。
 * 为了解决Unicode字符的乱码问题，提供了Reader/Writer，这些读入和写出操作都是基于两字节的char值（Unicode码元）。
 */
public class IOStreamTest {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
        test4();
    }

    /**
     * 字节输入流 示例一
     * 首先通过 FileInputStream 获取文件字节流，然后过滤器流 BufferedInputStream 为其赋予缓冲机制，使得程序更加高效。
     * 提供了两种读取方式：
     * 1. 循环判定是否结束
     * 2. 全部写入String对象中
     */
    public static void test1() throws IOException {
        // 新建一个 BufferedInputStream 对象
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream("src/coreII/CH02/res/IOStream.txt"));

//        int r;
//        while((r = bin.read()) != -1){
//            System.out.println((char)r);
//        }

        // 读取文件的内容并复制到 String 对象中
        String result = new String(bin.readAllBytes());
        System.out.println(result);
    }

    /**
     * 字节输入流 示例二
     * 多重嵌套流过滤器
     * DataInputStream/DataOutputStream 实现了DataInput/DataOutput 接口，因此这两个接口用于 <Strong>读入和写出二进制格式的数据!</Strong>
     *
     * 在Java的实现中，writeUTF：
     * 1. 写出字符串为二进制格式，就是将Java内部采用的UTF-16转化为修订版的 8 位UTF-8
     * 2. 读入二进制格式的字符串，就是将修订版UTF-8的数据，转化为UTF-16
     *
     * 其余方法例如: writerInt, 总是将一个整数写成为 4 字节的二进制数量值，不管他有多少位。例如 1234 -> 000004D2
     *             writeDouble, 总是将一个 double 值写出为 8 字节 的二进制数量值
     *             ...
     * 此处将DataInputStream置于构造器链的最后，是因为我们希望使用DataInputStream的方法，并且希望它们能够使用带缓冲机制的read方法，即从文件中读取指定类型的数据
     *
     * 通过 hexdump -C /Users/happytsing/Projects/core-java/src/coreII/CH02/res/DataStream.txt 获取文件的二进制内容
     */
    public static void test2() throws IOException {
        int i= 1234;
        String s = "hello";
        double d = 12.0;
        DataOutputStream dout = new DataOutputStream(new FileOutputStream("src/coreII/CH02/res/DataStream.txt"));
        dout.writeInt(i);
        dout.writeUTF(s);
        dout.writeDouble(d);
        dout.flush();
        DataInputStream din = new DataInputStream(new BufferedInputStream(new FileInputStream("src/coreII/CH02/res/DataStream.txt")));
        System.out.println(din.readInt());
        System.out.println(din.readUTF());
        System.out.println(din.readDouble());
    }

    /**
     * 字节输入流 示例三
     * 预览下一个字节（或若干个），以了解它是否是你想要的值，此时使用过滤器流 PushbackInputStream
     * 并给出了另一种循环方式：使用available()方法判断 Stream 中还有多少可用字节
     */
    public static void test3() throws Exception {
        PushbackInputStream  pbin = new PushbackInputStream(
                new BufferedInputStream(
                        new FileInputStream("src/coreII/CH02/res/IOStream.txt")));
        while (pbin.available() > 0){
            // 预读下一个字节
            int b = pbin.read();
            if(b == '<') {
                System.out.println("找到了符号<，将其推回，并退出循环");
                // 预读字节并非期望值，将其推回流中
                pbin.unread(b);
                break;
            }
            System.out.println("当前符号为："+(char)b);
        }
    }

    /**
     * 字节输入流 示例四
     * 遍历 zip 中的文件
     */
    public static void test4() throws Exception {
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/coreII/CH02/res/Stream.zip"));
        ZipEntry entry;
        while((entry = zin.getNextEntry()) != null){
            System.out.println(entry.getName());
            zin.closeEntry();
        }
        zin.close();
    }
}
