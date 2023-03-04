package com.company.feature_file_analyser.core.custom_types;

public class GenericType<T> {

    T obj;

    public GenericType(T o) {
        obj = o;
    }

    public T getObj() {
        return this.obj;
    }

    public void showType() {
        System.out.println(obj.getClass().getName());
    }

}
