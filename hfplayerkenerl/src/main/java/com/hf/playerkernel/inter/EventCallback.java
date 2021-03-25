package com.hf.playerkernel.inter;


public interface EventCallback<T> {
    void onEvent(T t);
}
