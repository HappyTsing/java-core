package coreI.CH07.tryWithResouces;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TryWithResources {
    public static void main(String[] args) throws IOException {

        try(var in = new Scanner(new FileInputStream("src/coreII/CH01/txt/cities.txt"), StandardCharsets.UTF_8);
            var out = new PrintWriter("coreI_CH06_TryWithResources_out.log",StandardCharsets.UTF_8))
        {
            while(in.hasNext()) {
                out.println(in.next().toUpperCase());
                System.out.println(in.next());
            }
        }
    }

}
