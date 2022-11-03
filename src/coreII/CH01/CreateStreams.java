package coreII.CH01;

import java.io.*;
import java.math.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.*;


public class CreateStreams {
    public static <T> void show(String title, Stream<T> stream){
        final int SIZE = 10;
        List<T> firstElemetns = stream.limit(SIZE+1).collect(Collectors.toList());
        System.out.print(title+":");
        for(int i = 0; i < firstElemetns.size(); i++) {
            if(i>0){
                System.out.print(", ");
            }
            if(i<SIZE){
                System.out.print(firstElemetns.get(i));
            }else{
                System.out.println("...");
            }
        }
        System.out.println();

    }



    public static void main(String[] args) throws IOException {
        Path path = Paths.get("src/coreII/CH01/txt/alice30.txt");
        String contents = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        // \\PL 以非字母分隔
        Stream<String> words = Stream.of(contents.split("\\PL+"));
        show("words",words);

        Stream<String> song = Stream.of("just","a","test");
        show("song",song);

        Stream<String> silence = Stream.empty();
        show("silence", silence);


        Stream<String> echos = Stream.generate(()->"echos");
        show("echos",echos);

        Stream<Double> randoms = Stream.generate(Math::random);
        show("randoms",randoms);

        Stream<BigInteger> integers = Stream.iterate(BigInteger.ONE,n -> n.add(BigInteger.ONE));
        show("integers",integers);
        Stream<String> wordsAnotherWay = Pattern.compile("\\PL+").splitAsStream(contents);

        show("wordsAnotherWay",wordsAnotherWay);

        try(Stream<String> lines = Files.lines(path,StandardCharsets.UTF_8)){
            show("lines",lines);
        }
        Iterable<Path> iterable = FileSystems.getDefault().getRootDirectories();
        Stream<Path> rootDirectories = StreamSupport.stream(iterable.spliterator(), false);
        show("rootDirectories", rootDirectories);

        Iterator<Path> iterator = Paths.get("src/coreII/CH01/txt/alice30.txt").iterator();
        Stream<Path> pathComponents = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                iterator, Spliterator.ORDERED), false);
        show("pathComponents", pathComponents);

    }

}

