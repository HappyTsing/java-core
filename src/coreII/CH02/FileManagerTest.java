package coreII.CH02;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManagerTest {
    public static void main(String[] args) throws IOException {
//        testPath();
        testFiles();

    }
    public static void testPath() throws IOException {
        Path absolute = Paths.get("/","absolute");
        // 相对路径是从项目路径开始，例如 relative 其实是 ~/.../core-java/relative
        Path relative = Paths.get("relative");

        // p.resolve(q)  若q是绝对路径，则结果就是q；若q不是绝对路径，则结果为Paths.get(p,q)
        System.out.println(relative.resolve(absolute));
        System.out.println(absolute.resolve(relative));

        // 创建指定路径的兄弟路径
        Path brother = absolute.resolveSibling("brother");
        System.out.println(brother);


        // relativize，用户将路径相对化，似乎没啥用？

        // toAbsolutePath，产生给定路径的绝对路径
        System.out.println(relative.toAbsolutePath());


        Path file = Paths.get("src","coreII","CH02","res","IOStream.txt");
        Path parent = absolute.getParent();
        Path file_name = absolute.getFileName();
        Path root = absolute.getRoot();

        // 从 Path 对象中构建 Scanner 对象
        Scanner scanner = new Scanner(file);
    }

    public static void testFiles() throws IOException {

        Path dirPath = Paths.get("src","coreII","CH02","res","newDir");
        // 除最后一个要创建的目录外，其余路径需要存在
        Files.createDirectory(dirPath);

        // 自动创建中途的目录
        Files.createDirectories(dirPath);

        Path filePath = Paths.get(String.valueOf(dirPath),"newFile.txt");

        // 创建已存在的文件会抛出异常
        Files.createFile(filePath);

        // 创建临时文件或文件夹
//        Files.createTempFile()
//        Files.createTempDirectory()

        // 复制和移动
//        Files.copy(fromPath,toPath);
//        Files.move(fromPath,toPath);


        // 获取文件信息


    }
}

