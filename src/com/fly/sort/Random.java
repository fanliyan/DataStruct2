package com.fly.sort;

import java.util.Objects;

/**
 * Random number class, using a 31-bit
 * linear congruential generator.
 * Note that java.util contains a class Random,
 * so watch out for name conflicts.
 * Created by Fanliyan on 2017/6/3.
 */
public class Random {
    private static final int A = 48271;
    private static final int M = 2147483647;
    private static final int Q = M / A;
    private static final int R = M % A;

    private int state;
    /**
     * Construct this Random object with
     * initial state obtained from system clock.
     */
    public Random(){
        this((int)(System.currentTimeMillis() % Integer.MAX_VALUE));
    }

    /**
     * Construct this Random object with
     * specified initial state.
     * @param initialValue the initial state.
     */
    public Random(int initialValue){
        if(initialValue < 0){
            initialValue += M;
        }
        state = initialValue;
        if(state == 0){
            state = 1;
        }
    }

    /**
     * Return a pseudorandom int, and change the
     * internal state.
     * @return the pseudorandom int.
     */
    public int randomInt(){
        int tmpState = A * (state % Q) - R * (state / Q);
        if(tmpState >= 0){
            state = tmpState;
        }else {
            state = tmpState + M;
        }
        return state;
    }

    /**
     * Return a pseudorandom int, and change the
     * internal state. DOES NOT WORK.
     * @return the pseudorandom int.
     */
    public int randomIntWORNG(){
        return state = (A * state) % M;
    }

    /**
     * Return a pseudorandom double in the open range 0..1
     * and change the internal state.
     * @return the pseudorandom double.
     */
    public double random0_1(){
        return (double)randomInt() / M;
    }

    /**
     * Return an int in the closed range [low,high], and
     * change the internal state.
     * @param low the minimum value returned.
     * @param high the maximum value returned.
     * @return the pseudorandom int.
     */
    public int randomInt(int low, int high){
        double partitionSize = (double)M / (high - low + 1);
        return (int)(randomInt() / partitionSize) + low;
    }

    /**
     * Return an long in the closed range [low,high], and
     * change the internal state.
     * @param low the minimum value returned.
     * @param high the maximum value returned.
     * @return the pseudorandom long.
     */
    public long randomLong(long low, long high){
        long longVal = ((long)randomInt() << 31) + randomInt();
        long longM = ((long)M << 31) + M;

        double partitionSize = (double)longM / (high - low + 1);
        return  (long)(longVal / partitionSize) + low;
    }

    /**
     * Randomly rearrange an array.
     * The random numbers used depend on the time and day.
     * @param a the array.
     */
    public static void permute(Object[] a){
        Random r = new Random();
        for(int j = 1; j < a.length; j++){
            Sort.swapReferences(a, j, r.randomInt(0, j));
        }
    }

    public static void main(String[] args) {
        Random r = new Random(1);
        for(int i = 0; i < 20; i++){
            System.out.println(r.randomInt());
        }
    }
}
