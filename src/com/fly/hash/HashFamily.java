package com.fly.hash;

/**
 * Created by Fanliyan on 2017/5/27.
 */
public interface HashFamily<AnyType> {
    int hash(AnyType x, int which);
    int getNumberOfFunctions();
    void generateNewFunctions();
}
