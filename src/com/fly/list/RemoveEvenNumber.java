package com.fly.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 移除线性表中偶数项
 * Created by Fanliyan on 2017/5/16.
 */
public class RemoveEvenNumber {

    /**
     * ArrayList,remove效率不高，LinkedList对get(i)调用效率不高，调用remove(i)，到达i位置代价昂贵
     * @param list
     */
    public static void removeEvenVer1(List<Integer> list){
        int i = 0;
        while(i < list.size()){
            if(list.get(i) % 2 == 0){
                list.remove(i);
            }else{
                i++;
            }
        }
    }

    /**
     * 产生异常，不能期待增强for循环懂得只有当一项不被删除时它才必须向前
     * @param list
     */
    public static void removeEvenVer2(List<Integer> list){
        for(Integer x: list){
            if(x % 2 == 0){
                list.remove(x);
            }
        }
    }

    /**
     * LinkedList花费时间是线性的不是二次的，ArrayList即使用迭代器的remove()仍然代价昂贵。
     * @param list
     */
    public static void removeEvenVer3(List<Integer> list){
        Iterator<Integer> itr = list.iterator();
        while (itr.hasNext()){
            if(itr.next() % 2 == 0){
                itr.remove();
            }
        }
    }

    public static void main(String[] args) {
        int N = 1600000;
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new LinkedList<>();

        for (int i = 0; i < N; i++) {
            list1.add(i);
            list2.add(i);
        }

        long start, end;

        start = System.currentTimeMillis();
        removeEvenVer3(list2);
        end = System.currentTimeMillis();
        System.out.println((end - start) + " ms for LinkedList size " + N);

        start = System.currentTimeMillis();
        removeEvenVer3(list1);
        end = System.currentTimeMillis();
        System.out.println((end - start) + " ms for ArrayList size " + N);

//        start = System.currentTimeMillis();
//        removeEvenVer1(list2);
//        end = System.currentTimeMillis();
//        System.out.println((end - start) + " ms for LinkedList size " + N);
//
//        start = System.currentTimeMillis();
//        removeEvenVer1(list1);
//        end = System.currentTimeMillis();
//        System.out.println((end - start) + " ms for ArrayList size " + N);

//        start = System.currentTimeMillis();
//        removeEvenVer2(list2);
//        end = System.currentTimeMillis();
//        System.out.println((end - start) + " ms for LinkedList size " + N);
    }



}
