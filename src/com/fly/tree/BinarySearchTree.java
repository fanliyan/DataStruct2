package com.fly.tree;


import java.nio.BufferUnderflowException;

/**
 * Created by Fanliyan on 2017/5/22.
 * 实现一棵不平衡的二叉搜索树
 */
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {

    private BinaryNode<AnyType> root;    //树根


    /**
     * 构造树
     */
    public BinarySearchTree(){
        root = null;
    }

    /**
     * 测试树是否是空
     * @return 如果是空返回true, 否则返回false
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * 插入到树，重复的元素什么也不做
     * @param x 插入的元素
     */
    public void insert(AnyType x){
        root = insert(x, root);
    }

    /**
     * 从树中移除操作，如果x没找到，则什么也不做
     * @param x 要移除的元素
     */
    public void remove(AnyType x){
        root = remove(x, root);
    }

    /**
     * 找到树中最小元素
     * @return 元素最小值，如果是空树，返回null
     */
    public AnyType findMin(){
        if(isEmpty()){
            throw new BufferUnderflowException();
        }
        return findMin(root).element;
    }

    /**
     * 找到树中最大元素
     * @return 元素最大值，如果是空树，返回null
     */
    public  AnyType findMax(){
        if(isEmpty()){
            throw new BufferUnderflowException();
        }
        return findMax(root).element;
    }

    /**
     * 找到树中的一个元素
     * @param x 要寻找的元素
     * @return 找到返回true,否则false
     */
    public boolean contains(AnyType x){
        return contains(x, root);
    }

    /**
     * 创建一棵逻辑上是空的树
     */
    public void makeEmpty(){
        root = null;
    }

    /**
     * 打印排序后的树
     */
    public void printTree(){
        if(isEmpty()){
            System.out.println("Empty tree");
        }else{
            printTree(root);
        }
    }

    /**
     * 插入一棵子树
     * @param x  插入的元素
     * @param t  根的子树的结点
     * @return   子树的新根
     */
    private BinaryNode<AnyType> insert(AnyType x, BinaryNode<AnyType> t){
        if(t == null){
            return new BinaryNode<>(x, null, null);
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = insert(x, t.left);
        }else if(compareResult > 0){
            t.right = insert(x, t.right);
        }else{
            ;          //重复，什么也不做
        }
        return t;
    }

    /**
     * 从子树中移除
     * @param x  移除的元素
     * @param t  根的子树的节点
     * @return  子树的新根
     */
    private BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t){
        if(t == null){
            return t;     //元素没找到，什么也不做
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = remove(x, t.left);
        }else if(compareResult > 0){
            t.right = remove(x, t.right);
        }else if (t.left != null && t.right != null){   //有两个孩子
                t.element = findMin(t.right).element;
                t.right = remove(t.element, t.right);
        }else {
            t = (t.left != null) ? t.left : t.right;
        }
        return t;
    }

    /**
     * 查找子树中最小的元素（递归方法）
     * @param t 根的子树节点
     * @return 包含最小元素的节点
     */
    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t){
        if(t == null){
            return null;
        }else if(t.left == null){
            return t;
        }
        return findMin(t.left);
    }

    /**
     * 查找子树中最大的元素（非递归方法）
     * @param t  根的子树的节点
     * @return 包含最大元素的节点
     */
    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t){
        if(t != null){
            while (t.right != null){
                t = t.right;
            }
        }
        return t;
    }

    /**
     * 找到子树中的一个元素
     * @param x 要查找的元素
     * @param t 根的子树的节点
     * @return 包含匹配项的节点
     */
    private boolean contains(AnyType x, BinaryNode<AnyType> t){
        if(t == null){
            return false;
        }
        int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            return contains(x, t.left);
        }else if(compareResult > 0){
            return contains(x, t.right);
        }else{
            return true;
        }
    }

    /**
     * 打印一棵排序后的子树
     * @param t 根的子树的节点
     */
    private void printTree(BinaryNode<AnyType> t){
        if(t != null){
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    /**
     * 计算子树的高度
     * @param t  根的子树的节点
     * @return
     */
    private int height(BinaryNode<AnyType> t){
        if(t == null){
            return -1;
        }else{
            return 1 + Math.max(height(t.left), height(t.right));
        }
    }

    /**
     * 二叉树节点类
     * @param
     */
    private static class BinaryNode<AnyType>{
        AnyType element;         //节点中的数据
        BinaryNode<AnyType> left;   //左孩子
        BinaryNode<AnyType> right;  //右孩子

        BinaryNode(AnyType theElement){
            this(theElement, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt){
            element = theElement;
            left = lt;
            right = rt;
        }
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> t = new BinarySearchTree<>();
        final int NUMS = 4000;
        final int GAP = 37;

        System.out.println("Checking... (no more output means success)");

        for (int i = GAP; i != 0 ; i = (i + GAP) % GAP) {
            t.insert(i);
        }

        for (int i = 1; i < NUMS; i += 2) {
            t.remove(i);
        }

        if(NUMS < 40){
            t.printTree();
        }

        if(t.findMin() != 2 || t.findMax() != NUMS - 2){
            System.out.println("FindMin or FindMax error!");
        }

        for(int i = 2; i < NUMS; i += 2){
            if(!t.contains(i)){
                System.out.println("Find error!");
            }
        }

        for (int i = 1; i < NUMS; i+=2) {
            if(t.contains(i)){
                System.out.println("Find error2!");
            }
        }
    }
}
