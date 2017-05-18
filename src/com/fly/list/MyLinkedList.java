package com.fly.list;


import org.omg.CORBA.Any;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * LinkedList实现（双链表）
 * Created by Fanliyan on 2017/5/18.
 */
public class MyLinkedList<AnyType> implements Iterable<AnyType> {

    private int theSize;
    private  int modCount = 0;
    private  Node<AnyType> beginMarker;
    private Node<AnyType> endMarker;

    /**
     * 构造一个空LinkedList
     */
    public MyLinkedList(){
        doClear();
    }

    public void clear(){
        doClear();
    }

    /**
     * 返回LinkedList中元素的大小
     * @return
     */
    public int size(){
        return theSize;
    }

    public boolean isEmpty(){
        return  size() == 0;
    }
    /**
     * 创建并连接头节点和尾节点，然后设置大小为0
     */
    public void doClear(){
        beginMarker = new Node<>(null, null, null);
        endMarker = new Node<>(null, beginMarker, null);
        beginMarker.next = endMarker;

        theSize = 0;
        modCount++;
    }

    /**
     * 在LinkedList最后添加一个值
     * @param x
     * @return
     */
    public boolean add(AnyType x){
        add(size(), x);
        return true;
    }

    /**
     * 在指定位置添加一个值
     * @param idx
     * @param x
     */
    public void add(int idx, AnyType x){
        addBefore(getNode(idx, 0, size()), x);
    }

    /**
     * 在指定位置p添加一个值
     * @param p
     * @param x
     */
    private void addBefore(Node<AnyType> p, AnyType x){
        //前三行可用p.prev = p.prev.next = new Node(x, p.prev, p)
        Node<AnyType> newNode = new Node<>(x, p.prev, p);
        p.prev.next = newNode;
        p.prev = newNode;
        theSize ++;
        modCount++;
    }

    /**
     * 获得位置idx的节点,idx范围必须是从0到size() - 1
     * @param idx
     * @return
     */
    private Node<AnyType> getNode(int idx){
        return getNode(idx, 0, size() - 1);
    }

    /**
     *获得在位置idx的节点,idx范围必须是从lower到upper
     * @param idx 用于搜索的索引
     * @param lower 最低有效索引
     * @param upper  最高有效索引
     * @return
     */
    private Node<AnyType> getNode(int idx, int lower, int upper){
        Node<AnyType> p;
        if(idx < lower || idx > upper){
            throw new IndexOutOfBoundsException("getNode index: " + "; size: " + size());
        }

        if(idx < size() / 2){
            p = beginMarker.next;
            for(int i = 0; i < idx; i++){
                p = p.next;
            }
        }else{
            p = endMarker;
            for(int i = size(); i > idx; i--){
                p = p.prev;
            }
        }
        return p;
    }

    /**
     * 根据索引idx移除一个值
     * @param idx
     * @return
     */
    public AnyType remove(int idx){
        return remove(getNode(idx));
    }

    /**
     * 移除节点p
     * @param p
     * @return
     */
    private AnyType remove(Node<AnyType> p){
        p.prev.next = p.next;
        p.next.prev = p.prev;
        theSize--;
        modCount++;
        return p.data;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[ ");
        for(AnyType x: this){
            sb.append(x + " ");
        }
        sb.append("]");
        return new String(sb);
    }

    /**
     * 获得一个用于遍历集合的Iterator对象
     * @return
     */
    public Iterator<AnyType> iterator(){
        return new LinkedListIterator();
    }

    /**
     *
     */
    private class LinkedListIterator implements Iterator<AnyType>{

        private Node<AnyType> current = beginMarker.next;
        private int expectedModCount = modCount;
        private boolean okToRemove = false;

        @Override
        public boolean hasNext(){
            return current != endMarker;
        }

        @Override
        public AnyType next() {
            if(modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            if(!hasNext()){
                throw new NoSuchElementException();
            }

            AnyType nextItem = current.data;
            current = current.next;
            okToRemove = true;
            return nextItem;
        }

        @Override
        public void remove() {
            if(modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            if(!okToRemove){
                throw new IllegalStateException();
            }
            MyLinkedList.this.remove(current.prev);
            expectedModCount++;
            okToRemove = false;
        }
    }

    /**
     * 双链表节点类
     * @param <AnyType>
     */
    private static class Node<AnyType>{

        public AnyType data;
        public Node<AnyType> prev;
        public Node<AnyType> next;

        public Node(AnyType d, Node<AnyType> p, Node<AnyType> n){
            data = d;
            prev = p;
            next = n;
        }
    }
}

class TestLinkedList{
    public static void main(String[] args) {
        MyLinkedList<Integer> lst = new MyLinkedList<>();
        for(int i = 0; i < 10; i++){
            lst.add(i);
        }
        for(int i = 20; i < 30; i++){
            lst.add(0, i);
        }
        lst.remove(0);
        lst.remove(lst.size() - 1);
        System.out.println(lst);

        Iterator<Integer> itr = lst.iterator();
        while(itr.hasNext()){
            itr.next();
            itr.remove();
            System.out.println(lst);
        }
    }
}
