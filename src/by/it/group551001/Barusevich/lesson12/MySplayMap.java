package by.it.group551001.Barusevich.lesson12;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

public class MySplayMap implements NavigableMap<Integer, String> {
    private final IntStringTree tree;
    private final boolean reversed;

    public MySplayMap() {
        this(new IntStringTree(IntStringTree.SPLAY), false);
    }

    private MySplayMap(IntStringTree tree, boolean reversed) {
        this.tree = tree;
        this.reversed = reversed;
    }

    private int compare(Integer first, Integer second) {
        return reversed ? Integer.compare(second, first) : Integer.compare(first, second);
    }

    private Integer firstNatural() {
        return reversed ? tree.lastKey() : tree.firstKey();
    }

    private Integer lastNatural() {
        return reversed ? tree.firstKey() : tree.lastKey();
    }

    private MySplayMap rangeCopy(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
        MySplayMap result = new MySplayMap(new IntStringTree(IntStringTree.SPLAY), reversed);
        int[] ordered = tree.keys(reversed);
        for (int key : ordered) {
            Integer boxed = key;
            if (from != null) {
                int c = compare(boxed, from);
                if (c < 0 || (c == 0 && !fromInclusive)) continue;
            }
            if (to != null) {
                int c = compare(boxed, to);
                if (c > 0 || (c == 0 && !toInclusive)) continue;
            }
            result.tree.put(boxed, tree.get(boxed));
        }
        return result;
    }

    private Map.Entry<Integer, String> entry(Integer key) {
        return key == null ? null : new SnapshotEntry(key, tree.get(key));
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return reversed ? (first, second) -> Integer.compare(second, first) : null;
    }

    @Override
    public Integer lowerKey(Integer key) {
        return reversed ? tree.higherKey(key) : tree.lowerKey(key);
    }

    @Override
    public Integer floorKey(Integer key) {
        return reversed ? tree.ceilingKey(key) : tree.floorKey(key);
    }

    @Override
    public Integer ceilingKey(Integer key) {
        return reversed ? tree.floorKey(key) : tree.ceilingKey(key);
    }

    @Override
    public Integer higherKey(Integer key) {
        return reversed ? tree.lowerKey(key) : tree.higherKey(key);
    }

    @Override
    public Map.Entry<Integer, String> lowerEntry(Integer key) {
        return entry(lowerKey(key));
    }

    @Override
    public Map.Entry<Integer, String> floorEntry(Integer key) {
        return entry(floorKey(key));
    }

    @Override
    public Map.Entry<Integer, String> ceilingEntry(Integer key) {
        return entry(ceilingKey(key));
    }

    @Override
    public Map.Entry<Integer, String> higherEntry(Integer key) {
        return entry(higherKey(key));
    }

    @Override
    public Map.Entry<Integer, String> firstEntry() {
        return isEmpty() ? null : entry(firstNatural());
    }

    @Override
    public Map.Entry<Integer, String> lastEntry() {
        return isEmpty() ? null : entry(lastNatural());
    }

    @Override
    public Map.Entry<Integer, String> pollFirstEntry() {
        if (isEmpty()) return null;
        Integer key = firstNatural();
        Map.Entry<Integer, String> result = entry(key);
        tree.remove(key);
        return result;
    }

    @Override
    public Map.Entry<Integer, String> pollLastEntry() {
        if (isEmpty()) return null;
        Integer key = lastNatural();
        Map.Entry<Integer, String> result = entry(key);
        tree.remove(key);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        IntStringTree copy = new IntStringTree(IntStringTree.SPLAY);
        for (int key : tree.keys(false)) copy.put(key, tree.get(key));
        return new MySplayMap(copy, !reversed);
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return new KeySetView();
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        if (compare(fromKey, toKey) > 0) throw new IllegalArgumentException("fromKey is greater than toKey");
        return rangeCopy(fromKey, fromInclusive, toKey, toInclusive);
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return rangeCopy(null, true, toKey, inclusive);
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return rangeCopy(fromKey, inclusive, null, true);
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        return headMap(toKey, false);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        return tailMap(fromKey, true);
    }

    @Override
    public Integer firstKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return firstNatural();
    }

    @Override
    public Integer lastKey() {
        if (isEmpty()) throw new NoSuchElementException();
        return lastNatural();
    }

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
        for (Map.Entry<? extends Integer, ? extends String> entry : map.entrySet()) put(entry.getKey(), entry.getValue());
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public NavigableSet<Integer> keySet() {
        return navigableKeySet();
    }

    @Override
    public Collection<String> values() {
        return new AbstractCollection<>() {
            @Override
            public int size() {
                return MySplayMap.this.size();
            }

            @Override
            public void clear() {
                MySplayMap.this.clear();
            }

            @Override
            public Iterator<String> iterator() {
                Iterator<Map.Entry<Integer, String>> entries = entrySet().iterator();
                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return entries.hasNext();
                    }

                    @Override
                    public String next() {
                        return entries.next().getValue();
                    }

                    @Override
                    public void remove() {
                        entries.remove();
                    }
                };
            }
        };
    }

    @Override
    public Set<Map.Entry<Integer, String>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public int size() {
                return MySplayMap.this.size();
            }

            @Override
            public void clear() {
                MySplayMap.this.clear();
            }

            @Override
            public Iterator<Map.Entry<Integer, String>> iterator() {
                int[] snapshot = tree.keys(reversed);
                return new Iterator<>() {
                    int index;
                    Integer last;

                    @Override
                    public boolean hasNext() {
                        return index < snapshot.length;
                    }

                    @Override
                    public Map.Entry<Integer, String> next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        last = snapshot[index++];
                        return entry(last);
                    }

                    @Override
                    public void remove() {
                        if (last == null) throw new IllegalStateException();
                        MySplayMap.this.remove(last);
                        last = null;
                    }
                };
            }
        };
    }

    @Override
    public String toString() {
        return tree.mapString(reversed);
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

    private final class KeySetView extends AbstractSet<Integer> implements NavigableSet<Integer> {
        @Override
        public int size() {
            return MySplayMap.this.size();
        }

        @Override
        public boolean contains(Object object) {
            return MySplayMap.this.containsKey(object);
        }

        @Override
        public boolean remove(Object object) {
            if (!contains(object)) return false;
            MySplayMap.this.remove(object);
            return true;
        }

        @Override
        public void clear() {
            MySplayMap.this.clear();
        }

        @Override
        public Iterator<Integer> iterator() {
            Iterator<Map.Entry<Integer, String>> entries = entrySet().iterator();
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return entries.hasNext();
                }

                @Override
                public Integer next() {
                    return entries.next().getKey();
                }

                @Override
                public void remove() {
                    entries.remove();
                }
            };
        }

        @Override
        public Iterator<Integer> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override
        public Comparator<? super Integer> comparator() {
            return MySplayMap.this.comparator();
        }

        @Override
        public Integer first() {
            return MySplayMap.this.firstKey();
        }

        @Override
        public Integer last() {
            return MySplayMap.this.lastKey();
        }

        @Override
        public Integer lower(Integer key) {
            return lowerKey(key);
        }

        @Override
        public Integer floor(Integer key) {
            return floorKey(key);
        }

        @Override
        public Integer ceiling(Integer key) {
            return ceilingKey(key);
        }

        @Override
        public Integer higher(Integer key) {
            return higherKey(key);
        }

        @Override
        public Integer pollFirst() {
            Map.Entry<Integer, String> entry = pollFirstEntry();
            return entry == null ? null : entry.getKey();
        }

        @Override
        public Integer pollLast() {
            Map.Entry<Integer, String> entry = pollLastEntry();
            return entry == null ? null : entry.getKey();
        }

        @Override
        public NavigableSet<Integer> descendingSet() {
            return descendingMap().navigableKeySet();
        }

        @Override
        public NavigableSet<Integer> subSet(Integer from, boolean fromInclusive, Integer to, boolean toInclusive) {
            return MySplayMap.this.subMap(from, fromInclusive, to, toInclusive).navigableKeySet();
        }

        @Override
        public NavigableSet<Integer> headSet(Integer to, boolean inclusive) {
            return MySplayMap.this.headMap(to, inclusive).navigableKeySet();
        }

        @Override
        public NavigableSet<Integer> tailSet(Integer from, boolean inclusive) {
            return MySplayMap.this.tailMap(from, inclusive).navigableKeySet();
        }

        @Override
        public SortedSet<Integer> subSet(Integer from, Integer to) {
            return subSet(from, true, to, false);
        }

        @Override
        public SortedSet<Integer> headSet(Integer to) {
            return headSet(to, false);
        }

        @Override
        public SortedSet<Integer> tailSet(Integer from) {
            return tailSet(from, true);
        }
    }

    private static final class SnapshotEntry implements Map.Entry<Integer, String> {
        private final Integer key;
        private final String value;

        SnapshotEntry(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Map.Entry<?, ?> entry)) return false;
            return (key == null ? entry.getKey() == null : key.equals(entry.getKey())) &&
                    (value == null ? entry.getValue() == null : value.equals(entry.getValue()));
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }
    }
}
