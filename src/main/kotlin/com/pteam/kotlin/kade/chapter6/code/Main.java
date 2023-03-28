package com.pteam.kotlin.kade.chapter6.code;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Generics<? extends Person> personGenerics = new Generics<Person>();
        Generics<? extends Person> workerGenerics = new Generics<Worker>();
        List<Generics<? extends Person>> list = new ArrayList<>();
        list.add(personGenerics);
        list.add(workerGenerics);
    }
}
