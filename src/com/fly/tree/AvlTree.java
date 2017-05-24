package com.fly.tree;



import java.nio.BufferUnderflowException;

/**
 * AVL树类
 * 请注意,所有的“匹配”是基于compareTo方法
 * @author 范立炎
 * Created by Fanliyan on 2017/5/23.
 */
public class AvlTree<AnyType extends Comparable<? super AnyType>> {

    private AvlNode<AnyType> root;    //树根

    /**
     * 构造一棵树
     */
     public AvlTree(){
         root = null;
     }

    /**
     * 插入树，重复则什么也不做
     * @param x 插入的元素
     */
    public void insert(AnyType x){
        root = insert(x, root);
    }

    public void remove(AnyType x){
        root = remove(x, root);
    }

    /**
     * 从一棵子树中删除
     * @param x 要删除的元素
     * @param t 根的子树的节点
     * @return 新子树的根
     */
    private AvlNode<AnyType> remove(AnyType x, AvlNode<AnyType> t){
        if(t == null){
            return t;    //元素没找到，什么也不做
        }
        int compareResult = x.compareTo(t.element);

        if(compareResult < 0){
            t.left = remove(x, t.left);
        }else if(compareResult > 0){
            t.right = remove(x, t.right);
        }else if(t.left != null && t.right != null){  //两个孩子
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        }else{
            t = (t.left != null) ? t.left : t.right;
        }
        return balance(t);
    }

    /**
     * 找到树中最小元素
     * @return 返回最小元素，如果是空返回null
     */
    public AnyType findMin(){
        if(isEmpty()){
            throw new BufferUnderflowException();
        }
        return findMin(root).element;
    }

    /**
     * 找到树中的最大元素
     * @return 返回最大元素，如果是空返回null
     */
    public AnyType findMax(){
        if(isEmpty()){
            throw new BufferUnderflowException();
        }
        return findMax(root).element;
    }

    /**
     * 找到子树中最小的元素
     * @param t  树根的节点
     * @return  包含最小元素的节点
     */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t){
        if(t == null){
            return t;
        }
        while (t.left != null){
            t = t.left;
        }
        return t;
    }

    /**
     * 在一棵子树中找到最大的元素
     * @param t 树根的节点
     * @return 包含最大元素的节点
     */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t){
        if(t == null){
            return t;
        }
        while (t.right != null){
            t = t.right;
        }
        return t;
    }

    /**
     * 创建一棵逻辑上的空树
     */
    public void makeEmpty(){
        root = null;
    }

    /**
     * 测试树是否在逻辑上为空的
     * @return 如果为空返回true,否则返回false
     */
    public boolean isEmpty(){
        return root == null;
    }

    /**
     * 找到树中的元素
     * @param x 要搜索的元素
     * @return 如果x被找到返回true
     */
    public boolean contains(AnyType x){
        return contains(x, root);
    }

    /**
     * 打印排序后树的内容
     */
    public void printTree(){
        if(isEmpty()){
            System.out.println("Empty tree");
        }else{
            printTree(root);
        }
    }

    public void checkBalance(){
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> t){
        if(t == null){
            return -1;
        }
        if(t != null){
            int hl = checkBalance(t.left);
            int hr = checkBalance(t.right);
            if(Math.abs(height(t.left) - height(t.right)) > 1 || height(t.left) != hl || height(t.right) != hr){
                System.out.println("OOPS");
            }
        }
        return height(t);
    }

    /**
     * 有序地打印子树
     * @param t 树根的节点
     */
    private void printTree(AvlNode<AnyType> t){
        if(t != null){
            printTree(t.left);
            System.out.println(t.element);
            printTree(t.right);
        }
    }

    private boolean contains(AnyType x, AvlNode<AnyType> t){
        while (t != null){
            int compareResult = x.compareTo(t.element);
            if(compareResult < 0){
                t = t.left;
            }else if(compareResult > 0){
                t = t.right;
            }else{
                return true;     //匹配
            }
        }
        return false;    //不匹配
    }

    /**
     * 插入子树
     * @param x 插入的元素
     * @param t 根的子树的节点
     * @return 子树的新根
     */
    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t){
        if(t == null){
            return new AvlNode<AnyType>(x, null, null);
        }
       int compareResult = x.compareTo(t.element);
        if(compareResult < 0){
            t.left = insert(x, t.left);
        }else if(compareResult > 0){
            t.right = insert(x, t.right);
        }else{
            ;     //重复什么也不做
        }
        return balance(t);
    }

    private static final int ALLOWED_IMBALANCE = 1;
    private AvlNode<AnyType> balance(AvlNode<AnyType> t){
        if(t == null){
            return t;
        }
        if(height(t.left) - height(t.right) > ALLOWED_IMBALANCE){
            if(height(t.left.left) >= height(t.left.right)){
                t = rotateWithLeftChild(t);
            }else {
                t = doubleWithLeftChild(t);
            }
        }else {
            if(height(t.right) - height(t.left) > ALLOWED_IMBALANCE){
                if(height(t.right.right) >= height(t.right.left)){
                    t = rotateWithRightChild(t);
                }else {
                    t = doubleWithRightChild(t);
                }
            }
            t.height = Math.max(height(t.left), height(t.right)) + 1;
        }
        return t;
    }

    /**
     * 旋转二叉树带着左孩子
     * 属于AVL树的第1种情况
     * 更新二叉树高度
     * @param k2
     * @return 新根的引用
     */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2){
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * 旋转二叉树带着右孩子
     * 属于AVL树的第4种情况
     * 更新高度
     * @param k1
     * @return 新根的引用
     */
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k1){
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * 双旋转二叉树节点：第一个左孩子和它的右孩子；然后节点k3带着新的左孩子
     * 这是AVL树第2种情况
     * 更新高度
     * @param k3
     * @return 新根的引用
     */
    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3){
        k3.left = rotateWithRightChild(k3.left);
        return  rotateWithLeftChild(k3);
    }

    /**
     * 双旋转二叉树节点：第1个右孩子和它的左孩子，然后节点1带着新的右孩子
     * 这是第3种情况
     * 更新高度
     * @param k1
     * @return  新根引用
     */
    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> k1){
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }

    /**
     * 返回节点t的高，如果是null则返回-1
     * @param t
     * @return
     */
    private int height(AvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    /**
     * AVL树节点类
     * @param <AnyType>
     */
    private static class AvlNode<AnyType>{

        AnyType element;      //节点数据
        AvlNode<AnyType> left;  //左孩子
        AvlNode<AnyType> right;  //右孩子
        int height;    //高度

        AvlNode(AnyType theElement){
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt){
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
        }
    }

    public static void main(String[] args) {
        AvlTree<Integer> t = new AvlTree<>();
        final int SMALL = 40;
        final int NUMS = 1000000;   //一定是偶数
        final int GAP = 37;

        System.out.println("Checking...(no more output means success)");

        for(int i = GAP; i != 0; i = (i + GAP) % NUMS){
            t.insert(i);
            if(NUMS < SMALL){
                t.checkBalance();
            }
        }
        for(int i = 1; i < NUMS; i += 2){
            t.remove(i);
            if(NUMS < SMALL){
                t.checkBalance();
            }
        }
        if(NUMS < SMALL){
            t.printTree();
        }
        if(t.findMin() != 2 || t.findMax() != NUMS - 2){
            System.out.println("FindMin or FindMax error!");
        }
        for(int i = 2; i < NUMS; i += 2){
            if(!t.contains(i)){
                System.out.println("Find error1");
            }
        }
        for(int i = 2; i < NUMS; i += 2){
            if(t.contains(i)){
                System.out.println("Find error2!");
            }
        }
    }
}
