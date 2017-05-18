package com.fly.list;



import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Fanliyan on 2017/5/17.
 */
public class MyArraiyList<AnyType> implements Iterable<AnyType> {
    private static final int DEFAULT_CAPACITY = 10;
    private int theSize;
    private AnyType[] theItems;

    /**
     * 构造一个空ArrayList
     */
    public MyArraiyList(){
        doClear();
    }

    public void clear(){
        doClear();
    }

    private void doClear(){
        theSize = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    /**
     * 如果ArrayList是空返回true
     * @return
     */
    public boolean isEmpty(){
        return size() == 0;
    }

    public void trimToSize(){
        ensureCapacity(size());
    }

    /**
     * 返回ArrayList大小
     * @return
     */
    public int size(){
        return theSize;
    }

    /**
     * 返回位置idx的值
     * @param idx
     * @return
     */
    public  AnyType get(int idx){
        if(idx < 0 || idx >= size()){
            throw new ArrayIndexOutOfBoundsException();
        }
        return theItems[idx];
    }

    /**
     * 改变位置idx的值
     * @param idx
     * @param newVal
     * @return
     */
    public AnyType set(int idx, AnyType newVal){
        if(idx < 0 || idx >= size()){
            throw new ArrayIndexOutOfBoundsException();
        }
        AnyType old = theItems[idx];
        theItems[idx] = newVal;
        return old;
    }

    /**
     * 在ArrayList末尾增加元素x
     * @param x
     * @return
     */
    public boolean add(AnyType x){
       add(size(), x);
        return true;
    }

    /**
     * 在位置idx插入x
     * @param idx
     * @param x
     */
    public void add(int idx, AnyType x){
        if(theItems.length == size()){
            ensureCapacity(size() * 2 + 1);
        }
        for (int i = theSize; i > idx ; i--) {
            theItems[i] = theItems[i - 1];
        }
        theItems[idx] = x;
        theSize++;
    }

    /**
     * 在ArrayList中移除索引idx的值
     * @param idx
     * @return
     */
    public AnyType remove(int idx){
        AnyType removedItem = theItems[idx];
        for (int i = idx; i < size() - 1; i++) {
            theItems[i] = theItems[i+1];
        }
        theSize--;
        return removedItem;
    }

    /**
     * 扩展MyArrayList容量
     * @param newCapacity
     */
    public void ensureCapacity(int newCapacity){
        if(newCapacity < theSize){
            return;
        }
        AnyType[] old = theItems;
        theItems = (AnyType[])new Object[newCapacity];
        for(int i = 0; i < size(); i++){
            theItems[i] = old[i];
        }
    }

    /**
     * 获得遍历ArrayList对象
     * @return
     */
    public Iterator<AnyType> iterator(){
        return new ArrayListIterator();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[ ");
        for (AnyType x: this) {
            sb.append(x + " ");
        }
        sb.append("]");
        return new String(sb);
    }

    private class  ArrayListIterator implements Iterator<AnyType>{

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size();
        }

        @Override
        public AnyType next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return theItems[current++];
        }

        @Override
        public void remove() {
                MyArraiyList.this.remove(--current);
        }
    }
}

class TestArrayList{
    public static void main(String[] args) {
        MyArraiyList<Integer> lst = new MyArraiyList<>();
        for(int i = 0; i < 10; i++){
            lst.add(i);
        }
        for(int i = 20; i < 30; i++){
            lst.add(0, i);
        }
        lst.remove(0);
        lst.remove(lst.size() - 1);
        System.out.println(lst);
    }
}
