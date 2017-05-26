package com.fly.hash;


import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;

import java.util.LinkedList;
import java.util.List;

/**
 * 分离链接哈希表类
 * Created by Fanliyan on 2017/5/26.
 *  CONSTRUCTION: an approximate initial size or default of 101
 *  Separate chaining table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 */
public class SeparateChainingHashTable<AnyType> {

    private static final int DEFAULT_TABLE_SIZE = 101;
    //数组集合
    private List<AnyType>[] theLists;
    private int currentSize;

    /**
     * 构造哈希表
     */
    public SeparateChainingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }

    /**
     * 构造哈希表
     * @param size  合适的表大小
     */
    public SeparateChainingHashTable(int size){
        theLists = new LinkedList[nextPrime(size)];
        for (int i = 0; i < theLists.length; i++) {
            theLists[i] = new LinkedList<>();
        }
    }

    /**
     * 写入哈希表。如果元素已经存在，什么也不做
     * @param x 要插入的元素
     */
    public void insert(AnyType x){
        List<AnyType> whichList = theLists[myhash(x)];
        if(!whichList.contains(x)){
            whichList.add(x);
            if(++currentSize > theLists.length){
                rehash();
            }
        }
    }

    /**
     * 从哈希表中移除
     * @param x 移除的元素
     */
    public void remove(AnyType x){
        List<AnyType> whichList = theLists[myhash(x)];
        if(whichList.contains(x)){
            whichList.remove(x);
            currentSize--;
        }
    }

    /**
     * 在哈希表中查找元素
     * @param x 要查找的元素
     * @return
     */
    public boolean contains(AnyType x){
        List<AnyType> whichList = theLists[myhash(x)];
        return whichList.contains(x);
    }

    /**
     * 制造一张逻辑上为空的哗然表
     */
    public void makeEmpty(){
        for(int i = 0; i < theLists.length; i++){
            theLists[i].clear();
        }
        currentSize = 0;
    }

    /**
     * A hash routine for String objects.
     * @param key the String to hash.
     * @param tableSize the size of the hash table.
     * @return the hash value.
     */
    public static int hash(String key, int tableSize){
        int hashVal = 0;
        for (int i = 0; i < key.length(); i++) {
            hashVal = 37 * hashVal + key.charAt(i);
        }
        hashVal %= tableSize;
        if(hashVal < 0){
            hashVal += tableSize;
        }
        return hashVal;
    }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime(int n){
        if(n % 2 == 0){
            n++;
        }
        for( ; !isPrime(n); n += 2){
            ;
        }
        return n;
    }

    /**
     * 测试一个数是否是素数
     * 不是一个有效率的算法
     * @param n 要测试的数字
     * @return 测试结果
     */
    private static boolean isPrime(int n){
        if(n == 2 || n == 3){
            return true;
        }
        if(n == 1 || n % 2 == 0){
            return false;
        }
        for(int i = 3; i * i <= n; i += 2){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }

    private void rehash(){
        List<AnyType>[] oldLists = theLists;
        //创建一个新的两倍大小的空表
        theLists = new List[nextPrime(2 * theLists.length)];
        for(int j = 0; j < theLists.length; j++){
            theLists[j] = new LinkedList<>();
        }
        currentSize = 0;
        for(List<AnyType> list: oldLists){
            for(AnyType item: list){
                insert(item);
            }
        }
    }

    private int myhash(AnyType x){
        int hashVal = x.hashCode();
        hashVal %= theLists.length;
        if(hashVal < 0){
            hashVal += theLists.length;
        }
        return hashVal;
    }

    public static void main(String[] args) {
        SeparateChainingHashTable<Integer> H = new SeparateChainingHashTable<>();
        long startTime = System.currentTimeMillis();

        final int NUMS = 2000000;
        final int GAP = 37;

        System.out.println("Checking...(no more output means success)");

        for(int i = GAP; i != 0; i = (i + GAP) % NUMS){
            H.insert(i);
        }
        for(int i = 1; i < NUMS; i += 2){
            H.remove(i);
        }
        for(int i = 2; i < NUMS; i += 2){
            if(!H.contains(i)){
                System.out.println("Find fails " + i);
            }
        }
        for(int i = 1; i < NUMS; i += 2){
            if(H.contains(i)){
                System.out.println("OOPS!!!" + i);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time:" + (endTime - startTime));
    }
}
