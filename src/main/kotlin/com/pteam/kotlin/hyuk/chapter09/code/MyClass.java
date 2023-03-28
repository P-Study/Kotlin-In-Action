package com.pteam.kotlin.hyuk.chapter09.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyClass<T extends Animal> {
    public static void main(String[] args) {
        MyClass<Animal> a = new MyClass<>();
        MyClass<Cat> b = new MyClass<>();

        List<MyClass<Animal>> list = new ArrayList<MyClass<Animal>>();
        list.add(a);
//        list.add(b); // compile error (Java는 무공변)

//        Collections.copy(new ArrayList<MyClass<Animal>>(), new ArrayList<MyClass<Cat>>());
    }
}




