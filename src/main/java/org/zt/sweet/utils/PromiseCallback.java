package org.zt.sweet.utils;

public abstract class PromiseCallback<T> {
    abstract public void onComplete(T value, Throwable error);
}
