package com.kings.yatharth.yatharthresume.controller;

/**
 * Created by yatharth on 21/06/17.
 */

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}