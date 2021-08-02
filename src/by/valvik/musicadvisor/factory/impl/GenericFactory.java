package by.valvik.musicadvisor.factory.impl;

import by.valvik.musicadvisor.factory.Factory;

import java.util.HashMap;
import java.util.Map;

public class GenericFactory<K, V> implements Factory<K, V> {

    private final Map<K, V> store;

    public GenericFactory() {

        this.store = new HashMap<>();

    }

    @Override
    public void add(K k, V v) {

        store.put(k, v);

    }

    @Override
    public V get(K k) {

        return store.get(k);

    }

}
