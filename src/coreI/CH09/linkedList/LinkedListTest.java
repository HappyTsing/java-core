package coreI.CH09.linkedList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 链表：目的是为了尽可能地减少再列表中间插入或删除元素的开销
 * @author QingHappy
 */
public class LinkedListTest {
    public static void main(String[] args) {
        var a = new LinkedList<String>();
        a.add("Amy");
        a.add("Baby");
        a.add("Cat");

        var b = new LinkedList<String>();
        b.add("Dog");
        b.add("Email");
        b.add("Fly");
        b.add("Gay");

        /**
         * LinkedList实现了Deque接口，因此可以实现一些队列的常用功能，如输出头部元素peek(),或输出并删除头部元素pull()
         */
        System.out.println(a.peek());

        /**
         * listIterator是iterator的子接口
         *          LinkedList的add方法只能将对象添加到链表的末尾，那么如何在链表中间实现add呢？
         *          显然，需要用到迭代器！
         *          Iterator是没有add方法的！
         *          于是提供了它的子接口listIterator
         *                  1. add方法：在指定位置add
         *                  2. previous方法：next方法的反方法
         *                  3. hasPrevious方法：类似hasNext方法
         *
         *                  注：且listIterator是可以输入参数Index的，如a.listIterator(2),用于设定初始位置
         */
        ListIterator<String> aIter = a.listIterator();
        Iterator<String> bIter = b.iterator();


        //在a中一个隔一个插入b的对象
        while (bIter.hasNext()) {
            if (aIter.hasNext()) {
                aIter.next();
            }
            aIter.add(bIter.next());

        }

        System.out.println("a="+a);

        //一个隔着一个删除b的对象
        bIter = b.iterator();
        while (bIter.hasNext()) {
            bIter.next();
            if (bIter.hasNext()) {
                bIter.next();
                bIter.remove();
            }
        }
        System.out.println("b="+b);

        //从a中删除b的对象
        a.removeAll(b);

        System.out.println("a.removeAll(b)="+a);
    }



}
