package com.fly.hash;


/**
 * Created by Fanliyan on 2017/5/27.
 */
public class QuadraticProbingHashTable<AnyType> {

    private static final int DEFAULT_TABLE_SIZE = 101;
    private HashEntry<AnyType>[] array; //元素数组
    private int occupied;      //The number of occupied cells
    private int theSize;       //当前大小

    //构造哈希表
    public QuadraticProbingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public QuadraticProbingHashTable(int size){
        allocateArray(size);
        doClear();
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public boolean insert(AnyType x){
        // Insert x as active
        int currentPos = findPos(x);
        if(isActive(currentPos)){
            return false;
        }
        if(array[currentPos] == null){
            ++occupied;
        }
        array[currentPos] = new HashEntry<>(x, true);
        theSize++;
        if(occupied > array.length / 2){
            rehash();
        }
        return true;
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item removed
     */
    public boolean remove(AnyType x){
        int currentPos = findPos(x);
        if(isActive(currentPos)){
            array[currentPos].isActive = false;
            theSize--;
            return true;
        }else {
            return false;
        }
    }

    /**
     * Get current size.
     * @return the size.
     */
    public int size(){
        return theSize;
    }

    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity(){
        return array.length;
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public boolean contains(AnyType x){
        int currentPos = findPos(x);
        return isActive(currentPos);
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty(){
        doClear();
    }

    /**
     * Expand the hash table.
     */
    private void rehash(){
        HashEntry<AnyType>[] oldArray = array;
        // Create a new double-sized, empty table
        allocateArray(2 * oldArray.length);
        occupied = 0;
        theSize = 0;

        for(HashEntry<AnyType> entry: oldArray){
            if(entry != null && entry.isActive){
                insert(entry.element);
            }
        }
    }

    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    private boolean isActive(int currentPos){
        return array[currentPos] != null && array[currentPos].isActive;
    }

    private int findPos(AnyType x){
        int offset = 1;
        int currentPos = myhash(x);
        while (array[currentPos] != null && !array[currentPos].element.equals(x)){
            currentPos += offset;    // Compute ith probe
            offset += 2;
            if(currentPos >= array.length){
                currentPos -= array.length;
            }
        }
        return  currentPos;
    }

    private  void doClear(){
        occupied = 0;
        for(int i = 0; i < array.length; i++){
            array[i] = null;
        }
    }

    private int myhash(AnyType x){
        int hashVal = x.hashCode();
        hashVal %= array.length;
        if(hashVal < 0){
            hashVal += array.length;
        }
        return  hashVal;
    }

    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    private void allocateArray(int arraySize){
        array = new HashEntry[nextPrime(arraySize)];
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
        for(; !isPrime(n); n += 2){
            ;
        }
        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
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

    private  static class HashEntry<AnyType>{
        public  AnyType element;     //元素
        public boolean isActive;    //如果被标记删除则是false

        public HashEntry(AnyType e){
            this(e, true);
        }

        public HashEntry(AnyType e, boolean i){
            element = e;
            isActive = i;
        }
    }

    public static void main(String[] args) {
        QuadraticProbingHashTable<String> H = new QuadraticProbingHashTable<>();
        long startTime = System.currentTimeMillis();

        final int NUMS = 2000000;
        final int GAP = 37;

        System.out.println("Checking...(no more output means sucess)");

        for(int i = GAP; i != 0; i = (i + GAP) % NUMS){
            H.insert(" " + i);
        }
        for(int i = GAP; i != 0; i = (i + GAP) % NUMS){
            if(H.insert(" " + i)){
                System.out.println("OOPS!!! " + i);
            }
        }
        for(int i = 1; i < NUMS; i += 2){
            H.remove(" " + i);
        }
        for(int i = 2; i < NUMS; i += 2){
            if(!H.contains(" " + i)){
                System.out.println("Find fails " + i);
            }
        }
        for(int i = 1; i < NUMS; i += 2){
            if(H.contains(" " + i)){
                System.out.println("OOPS!!! " + i);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (endTime - startTime));
    }
}
