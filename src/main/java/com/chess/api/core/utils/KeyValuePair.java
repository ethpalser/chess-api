package com.chess.api.core.utils;

public class KeyValuePair <K, V> {

    public final K key;
    public final V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static KeyValuePair<String, String> of(String key, String value) {
        return new KeyValuePair<>(key, value);
    }

}
