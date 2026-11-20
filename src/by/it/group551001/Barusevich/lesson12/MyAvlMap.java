package by.it.group551001.Barusevich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private final IntStringTree tree = new IntStringTree(IntStringTree.AVL);

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof Integer integer && tree.containsKey(integer);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value != null && !(value instanceof String)) return false;
        return tree.containsValue((String) value);
    }

    @Override
    public String get(Object key) {
        return key instanceof Integer integer ? tree.get(integer) : null;
    }

    @Override
    public String put(Integer key, String value) {
        return tree.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return key instanceof Integer integer ? tree.remove(integer) : null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> map) {
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return tree.keySet();
    }

    @Override
    public Collection<String> values() {
        return tree.values();
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        return tree.entrySet();
    }

    @Override
    public String toString() {
        return tree.mapString(false);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Map<?, ?> map) || map.size() != size()) return false;
        for (Map.Entry<Integer, String> entry : entrySet()) {
            Object value = map.get(entry.getKey());
            if (value == null ? entry.getValue() != null || !map.containsKey(entry.getKey()) : !value.equals(entry.getValue())) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Map.Entry<Integer, String> entry : entrySet()) hash += entry.hashCode();
        return hash;
    }
}
