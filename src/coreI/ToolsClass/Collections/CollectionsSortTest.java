package coreI.ToolsClass.Collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CollectionsSortTest {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(-1);
        arrayList.add(3);
        arrayList.add(3);
        arrayList.add(-5);
        arrayList.add(7);
        arrayList.add(4);
        arrayList.add(-9);
        arrayList.add(-7);
        System.out.println("原始数组:");
        System.out.println(arrayList);
        // void reverse(List list)：反转
        //将原始数组的顺序反转
        Collections.reverse(arrayList);
        System.out.println("coreI.ToolsClass.Collections.reverse(arrayList):");
        System.out.println(arrayList);

        //void rotate(List list, int distance)：旋转
        // 当distance为正数时，将list的后distance个元素整体移到前面。
        // 当distance为负数时，将list的前distance个元素整体移到后面。
        Collections.rotate(arrayList, 3);
        System.out.println("coreI.ToolsClass.Collections.rotate(arrayList, 3):");
        System.out.println(arrayList);

        // void sort(List list),按自然排序的升序排序
        Collections.sort(arrayList);
        System.out.println("coreI.ToolsClass.Collections.sort(arrayList):");
        System.out.println(arrayList);

        // void shuffle(List list),随机排序
        Collections.shuffle(arrayList);
        System.out.println("coreI.ToolsClass.Collections.shuffle(arrayList):");
        System.out.println(arrayList);

        // void swap(List list, int i , int j),交换两个索引位置的元素
        Collections.swap(arrayList, 2, 5);
        System.out.println("coreI.ToolsClass.Collections.swap(arrayList, 2, 5):");
        System.out.println(arrayList);

        // 定制排序的用法
        Collections.sort(arrayList, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("定制排序后：");
        System.out.println(arrayList);
    }
}
