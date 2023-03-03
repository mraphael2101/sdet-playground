package com.company.feature_file_analyser.custom_types;

public class Gen<T> {

    T obj;

    public Gen(T o) {
        obj = o;
    }

    T getObj() {
        return this.obj;
    }

    public void showType() {
        System.out.println(obj.getClass().getName());
    }

}
