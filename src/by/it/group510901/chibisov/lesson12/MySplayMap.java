package by.it.group510901.chibisov.lesson12;

import java.util.*;

public class MySplayMap<K, V> implements NavigableMap<K, V> {

    private static class Node {
        Object key;
        Object value;
        Node left;
        Node right;
        Node parent;

        public Node(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    boolean isLeftChild(Node n) {
        return n.parent.left == n;
    }

    boolean isRightChild(Node n) {
        return n.parent.right == n;
    }

    private Node root;
    private int size;

    @SuppressWarnings("unchecked")
    private int compare(Object first, Object second) {
        return ((Comparable<? super K>) first).compareTo((K) second);
    }

    private void rotateRight(Node node) {
        Node pivot = node.left;
        Node orphan = pivot.right;

        pivot.right = node;
        node.left = orphan;

        pivot.parent = node.parent;

        if (node.parent == null) {
            root = pivot;
        } else if (isLeftChild(node)) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        node.parent = pivot;
        if (orphan != null) {
            orphan.parent = node;
        }
    }

    private void rotateLeft(Node node) {
        Node pivot = node.right;
        Node orphan = pivot.left;

        pivot.left = node;
        node.right = orphan;

        pivot.parent = node.parent;

        if (node.parent == null) {
            root = pivot;
        } else if (isLeftChild(node)) {
            node.parent.left = pivot;
        } else {
            node.parent.right = pivot;
        }
        node.parent = pivot;
        if (orphan != null) {
            orphan.parent = node;
        }
    }
    
    private void splay(Node n) {
        while (n.parent != null) {
            Node p = n.parent;
            Node g = p.parent;
            // zig
            if (g == null) {
                if (isLeftChild(n)) {
                    rotateRight(p);
                } else {
                    rotateLeft(p);
                }
            }
            // zig-zig
            else if (isLeftChild(n) && isLeftChild(p)) {
                rotateRight(g);
                rotateRight(p);
            } else if (isRightChild(n) && isRightChild(p)) {
                rotateLeft(g);
                rotateLeft(p);
            }
            // zig-zag
            else if (isLeftChild(n) && isRightChild(p)) {
                rotateRight(p);
                rotateLeft(g);
            } else if (isRightChild(n) && isLeftChild(p)) {
                rotateLeft(p);
                rotateRight(g);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////               Обязательные к реализации методы             /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isEmpty()) return "{}";
        sb.append('{');
        inOrder(root, sb);
        sb.delete(sb.length() - 2, sb.length());
        sb.append('}');
        return sb.toString();
    }

    private void inOrder(Node n, StringBuilder sb) {
        if (n == null) return;
        inOrder(n.left, sb);
        sb.append(n.key).append('=').append(n.value).append(", ");
        inOrder(n.right, sb);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V put(K key, V value) {
        if (key == null) throw new NullPointerException();
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                V oldValue = (V) current.value;
                current.value = value;
                splay(current);
                return oldValue;
            }
        }
        current = new Node(key, value);
        current.parent = currentPrev;

        if (currentPrev == null) {
            root = current;
        } else {
            if (compare(key, currentPrev.key) < 0) {
                currentPrev.left = current;
            } else {
                currentPrev.right = current;
            }
            splay(current);
        }

        size++;
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        if (key == null) throw new NullPointerException();
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                V oldValue = (V) current.value;
                Node c;
                if (current.left == null) {
                    c = current.right;
                } else if (current.right == null) {
                    c = current.left;
                } else {
                    Node successor = current.right;
                    while (successor.left != null) successor = successor.left;
                    current.key = successor.key;
                    current.value = successor.value;
                    current = successor;
                    c = current.right;
                }
                if (current.parent == null) {
                    root = c;
                } else {
                    if (isLeftChild(current)) {
                        current.parent.left = c;
                    } else {
                        current.parent.right = c;
                    }
                }
                if (c != null) {
                    c.parent = current.parent;
                    splay(c);
                } else {
                    if (current.parent != null) {
                        splay(current.parent);
                    }
                }
                size--;
                return oldValue;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        if (key == null) throw new NullPointerException();
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return (V) current.value;
            }
        }
        if (currentPrev != null) splay(currentPrev);
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new NullPointerException();
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return true;
            }
        }
        if (currentPrev != null) splay(currentPrev);
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return preOrder(root, value);
    }

    private boolean preOrder(Node n, Object value) {
        if (n == null) return false;
        if (Objects.equals(n.value, value)) return true;
        if (preOrder(n.left, value)) return true;
        return preOrder(n.right, value);
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        if (toKey == null) throw new NullPointerException();
        MySplayMap<K, V> newMap = new MySplayMap<>();
        collectHeadMap(root, toKey, newMap);
        return newMap;
    }

    @SuppressWarnings("unchecked")
    private void collectHeadMap(Node n, K toKey, MySplayMap<K, V> map) {
        if (n == null) return;
        collectHeadMap(n.left, toKey, map);
        if (compare(n.key, toKey) < 0) {
            map.put((K) n.key, (V) n.value);
        } else return;
        collectHeadMap(n.right, toKey, map);
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MySplayMap<K, V> newMap = new MySplayMap<>();
        collectTailMap(root, fromKey, newMap);
        return newMap;
    }

    @SuppressWarnings("unchecked")
    private void collectTailMap(Node n, K fromKey, MySplayMap<K, V> map) {
        if (n == null) return;
        collectTailMap(n.right, fromKey, map);
        if (compare(n.key, fromKey) >= 0) {
            map.put((K) n.key, (V) n.value);
        } else return;
        collectTailMap(n.left, fromKey, map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public K firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node firstNode = root;
        while (firstNode.left != null) firstNode = firstNode.left;
        splay(firstNode);
        return (K) firstNode.key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node lastNode = root;
        while (lastNode.right != null) lastNode = lastNode.right;
        splay(lastNode);
        return (K) lastNode.key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K lowerKey(K key) {
        if (key == null) throw new NullPointerException();
        Node resultNode = null;
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp > 0) {
                resultNode = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        if (resultNode != null) {
            splay(resultNode);
            return (K) resultNode.key;
        }
        if (currentPrev != null) splay(currentPrev);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K floorKey(K key) {
        if (key == null) throw new NullPointerException();
        Node resultNode = null;
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                resultNode = current;
                current = current.right;
            } else {
                splay(current);
                return (K) current.key;
            }
        }
        if (resultNode != null) {
            splay(resultNode);
            return (K) resultNode.key;
        }
        if (currentPrev != null) splay(currentPrev);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K ceilingKey(K key) {
        if (key == null) throw new NullPointerException();
        Node resultNode = null;
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                resultNode = current;
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                splay(current);
                return (K) current.key;
            }
        }
        if (resultNode != null) {
            splay(resultNode);
            return (K) resultNode.key;
        }
        if (currentPrev != null) splay(currentPrev);
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public K higherKey(K key) {
        if (key == null) throw new NullPointerException();
        Node resultNode = null;
        Node currentPrev = null;
        Node current = root;
        while (current != null) {
            int cmp = compare(key, current.key);
            currentPrev = current;
            if (cmp < 0) {
                resultNode = current;
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (resultNode != null) {
            splay(resultNode);
            return (K) resultNode.key;
        }
        if (currentPrev != null) splay(currentPrev);
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////              Необязательные к реализации методы            /////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Entry<K, V> lowerEntry(K key) {
        return null;
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        return null;
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        return null;
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        return null;
    }

    @Override
    public Entry<K, V> firstEntry() {
        return null;
    }

    @Override
    public Entry<K, V> lastEntry() {
        return null;
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }
}