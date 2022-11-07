package coreII.CH02;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author happytsing
 */
public class ReaderWriterTest {
    public static void main(String[] args) throws IOException {
        test1();
    }
    /**
     * 字符读入和写出测试。
     * 结论：以两个字节为单位进行读入和写出，因此，无法正确处理UTF-16中需要四个字节进行编码的字符，例如 𐐷
     *      其四个字节会被分为两个部分读出！
     *
     * Java 内部采用 UTF-16 进行编码，在使用Reader/Writer时需要指定编码方式。
     * 本例中使用UTF-8进行编码方式，因此在写出时，会将UTF-16编码的字符串转换为UTF-8的二进制，存入目标文件中。
     * 例如: 卿 的UTF-16编码为: 537F
     *       使用UTF-8编码格式写出到文件后，使用 hexdump RW.txt 查看，可知文件中存储的数据，也就是UTF-8编码的数据为：E58DBF
     *       在线网址 @see <a href="https://www.qqxiuzi.cn/bianma/Unicode-UTF.php">unicode-UTF</a> 查看可知，确实如此。
     *
     * 同时，本例中 out 使用 try-with-resources，因此无需显示调用 out.close()
     *            out 在构建时设置了 autoFlush = true，因此无需显示调用 out.flush()，在每次 out.print()时会自动冲刷缓存
     *
     *            in 没有使用 try-with-resources ，因此需要显示调用 in.close()
     *
     * InputStreamReader/OutputStreamWriter 是字节流和字符流转换的桥梁！
     */
    public static void test1() throws IOException {
        try(PrintWriter out = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream("src/coreII/CH02/res/RW.txt"), StandardCharsets.UTF_8),true)
        ){
            // 𐐷在UTF-16中用四个字节编码：D801DC37
            out.print("𐐷");
            out.print("卿");
//            out.flush();

            var in = new InputStreamReader(
                    new FileInputStream("src/coreII/CH02/res/RW.txt"), StandardCharsets.UTF_8);
            int content;
            while((content = in.read()) != -1){
                System.out.println("符号: "+(char)content);
                System.out.printf("十六进制: "  + "%x\n", content);
            }
            System.out.println("组合D801DC37: "+"\uD801\uDC37");
            in.close();
        }
    }


}
