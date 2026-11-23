package by.it.group551003.kravchenkovikk.lesson12;

import java.util.SortedMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.Map;

public class MyRbMap implements SortedMap<Integer, String>{

    enum nodeColor {RED, BLACK};

    private Node root;
    private int size;

    public MyRbMap(){
        this.root = null;
        this.size = 0;
    }

    private static class Node {
        Integer key;
        String value;
        nodeColor color;
        Node parent;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.color = nodeColor.RED;
        }
    }

    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }
        x.right = y;
        y.parent = x;
    }

    private void insertFixup(Node z) {
        while (z.parent != null && z.parent.color == nodeColor.RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y != null && y.color == nodeColor.RED) {
                    z.parent.color = nodeColor.BLACK;
                    y.color = nodeColor.BLACK;
                    z.parent.parent.color = nodeColor.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = nodeColor.BLACK;
                    z.parent.parent.color = nodeColor.RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.color == nodeColor.RED) {
                    z.parent.color = nodeColor.BLACK;
                    y.color = nodeColor.BLACK;
                    z.parent.parent.color = nodeColor.RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = nodeColor.BLACK;
                    z.parent.parent.color = nodeColor.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        if (root != null) {
            root.color = nodeColor.BLACK;
        }
    }

    private Node getNode(Integer key) {
        Node curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.key);
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr;
        }
        return null;
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void deleteNode(Node z) {
        Node y = z;
        nodeColor yOriginalColor = y.color;
        Node x;
        Node xParent;

        if (z.left == null) {
            x = z.right;
            xParent = z.parent;
            transplant(z, z.right);
        } else if (z.right == null) {
            x = z.left;
            xParent = z.parent;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                xParent = y;
            } else {
                xParent = y.parent;
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == nodeColor.BLACK) {
            deleteFixup(x, xParent);
        }
    }

    private void deleteFixup(Node x, Node xParent) {
        while (x != root && (x == null || x.color == nodeColor.BLACK)) {
            if (xParent == null) break;
            if (x == xParent.left) {
                Node w = xParent.right;
                if (w != null && w.color == nodeColor.RED) {
                    w.color = nodeColor.BLACK;
                    xParent.color = nodeColor.RED;
                    leftRotate(xParent);
                    w = xParent.right;
                }
                if ((w == null || w.left == null || w.left.color == nodeColor.BLACK) &&
                        (w == null || w.right == null || w.right.color == nodeColor.BLACK)) {
                    if (w != null) w.color = nodeColor.RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (w == null || w.right == null || w.right.color == nodeColor.BLACK) {
                        if (w != null && w.left != null) w.left.color = nodeColor.BLACK;
                        if (w != null) w.color = nodeColor.RED;
                        rightRotate(w);
                        w = xParent.right;
                    }
                    if (w != null) {
                        w.color = xParent.color;
                        if (w.right != null) w.right.color = nodeColor.BLACK;
                    }
                    xParent.color = nodeColor.BLACK;
                    leftRotate(xParent);
                    x = root;
                    xParent = null;
                }
            } else {
                Node w = xParent.left;
                if (w != null && w.color == nodeColor.RED) {
                    w.color = nodeColor.BLACK;
                    xParent.color = nodeColor.RED;
                    rightRotate(xParent);
                    w = xParent.left;
                }
                if ((w == null || w.right == null || w.right.color == nodeColor.BLACK) &&
                        (w == null || w.left == null || w.left.color == nodeColor.BLACK)) {
                    if (w != null) w.color = nodeColor.RED;
                    x = xParent;
                    xParent = x.parent;
                } else {
                    if (w == null || w.left == null || w.left.color == nodeColor.BLACK) {
                        if (w != null && w.right != null) w.right.color = nodeColor.BLACK;
                        if (w != null) w.color = nodeColor.RED;
                        leftRotate(w);
                        w = xParent.left;
                    }
                    if (w != null) {
                        w.color = xParent.color;
                        if (w.left != null) w.left.color = nodeColor.BLACK;
                    }
                    xParent.color = nodeColor.BLACK;
                    rightRotate(xParent);
                    x = root;
                    xParent = null;
                }
            }
        }
        if (x != null) {
            x.color = nodeColor.BLACK;
        }
    }

    private void fillHeadMap(Node node, Integer toKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(toKey) < 0) {
            fillHeadMap(node.left, toKey, sub);
            sub.put(node.key, node.value);
            fillHeadMap(node.right, toKey, sub);
        } else {
            fillHeadMap(node.left, toKey, sub);
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "{}";
        }
        StringBuilder str = new StringBuilder("{");
        Node[] stack = new Node[size];
        int top = -1;
        Node curr = root;
        while (curr != null || top >= 0) {
            while (curr != null) {
                top++;
                stack[top] = curr;
                curr = curr.left;
            }
            curr = stack[top];
            top--;
            str.append(curr.key).append("=").append(curr.value).append(", ");
            curr = curr.right;
        }
        str.setLength(str.length() - 2);
        str.append("}");
        return str.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Node y = null;
        Node x = root;
        while (x != null) {
            y = x;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            } else if (cmp > 0) {
                x = x.right;
            } else {
                String oldValue = x.value;
                x.value = value;
                return oldValue;
            }
        }

        Node z = new Node(key, value);
        z.parent = y;
        if (y == null) {
            root = z;
        } else if (key.compareTo(y.key) < 0) {
            y.left = z;
        } else {
            y.right = z;
        }
        z.color = nodeColor.RED;
        insertFixup(z);
        size++;
        return null;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node z = getNode((Integer) key);
        if (z == null) return null;
        String oldValue = z.value;
        deleteNode(z);
        size--;
        return oldValue;
    }

    @Override
    public String get(Object key) {
        Node curr = root;
        while (curr != null) {
            if (curr.key.compareTo((Integer)key) > 0) {
                curr = curr.left;
            } else if (curr.key.compareTo((Integer)key) < 0) {
                curr = curr.right;
            } else {
                return curr.value;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        String isContains = get(key);
        if (isContains != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        if (root == null) return false;
        Node[] stack = new Node[size];
        int top = -1;
        Node curr = root;
        while (curr != null || top >= 0) {
            while (curr != null) {
                top++;
                stack[top] = curr;
                curr = curr.left;
            }
            curr = stack[top];
            top--;
            if (value == null ? curr.value == null : value.equals(curr.value)) {
                return true;
            }
            curr = curr.right;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MyRbMap sub = new MyRbMap();
        if (root == null) return sub;
        fillHeadMap(root, toKey, sub);
        return sub;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MyRbMap sub = new MyRbMap();
        if (root == null) return sub;
        fillTailMap(root, fromKey, sub);
        return sub;
    }

    private void fillTailMap(Node node, Integer fromKey, MyRbMap sub) {
        if (node == null) return;
        if (node.key.compareTo(fromKey) >= 0) {
            fillTailMap(node.left, fromKey, sub);
            sub.put(node.key, node.value);
            fillTailMap(node.right, fromKey, sub);
        } else {
            fillTailMap(node.right, fromKey, sub);
        }
    }

    @Override
    public Integer firstKey() {
        if (root == null) return null;
        Node curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) return null;
        Node curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.key;
    }

    @Override public Comparator<? super Integer> comparator() { throw new UnsupportedOperationException(); }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
