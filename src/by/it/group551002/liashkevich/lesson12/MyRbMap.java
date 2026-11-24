package by.it.group551002.liashkevich.lesson12;

import com.sun.source.tree.Tree;

import java.util.*;

public class MyRbMap implements SortedMap {

    static class TreeNode {
        Object value;
        Object key;
        TreeNode parent;
        TreeNode leftChild;
        TreeNode rightChild;
        boolean color;
        // false = black
        // true = red

        TreeNode(Object key, Object value, boolean color) {
            this.key =  key;
            this.value = value;
            this.parent = null;
            this.leftChild = null;
            this.rightChild = null;
            this.color = color;
        }

        private void flip_color() {
            this.color = this.color != true;
        }

    }

    TreeNode Root = null;
    int size = 0;

    private boolean is_red(TreeNode N) {
        return N != null && N.color;
    }

    private void inorderTraversal(TreeNode P, StringBuilder sb) {
        if (P == null)
            return;
        inorderTraversal(P.leftChild, sb);
        sb.append(P.key).append("=").append(P.value).append(", ");
        inorderTraversal(P.rightChild, sb);
    }

    private void headTraversal(TreeNode P, Object toKey, MyRbMap result) {
        if (P == null)
            return;
        headTraversal(P.leftChild, toKey, result);
        if (((Comparable<Object>)P.key).compareTo(toKey) >= 0)
            return;
        result.put(P.key, P.value);
        headTraversal(P.rightChild, toKey, result);
    }

    private void headTraversalBack(TreeNode P, Object fromKey, MyRbMap result) {
        if (P == null)
            return;
        headTraversalBack(P.rightChild, fromKey, result);
        if (((Comparable<Object>)P.key).compareTo(fromKey) < 0)
            return;
        result.put(P.key, P.value);
        headTraversalBack(P.leftChild, fromKey, result);
    }

    private TreeNode r_rotation(TreeNode P, TreeNode L) {
        TreeNode G = P.parent;
        P.leftChild = L.rightChild;
        if (L.rightChild != null)
            L.rightChild.parent = P;
        L.rightChild = P;
        L.parent = G;
        P.parent = L;
        if (G == null)
            this.Root = L;
        else if (G.leftChild == P)
            G.leftChild = L;
        else
            G.rightChild = L;
        return L;
    }

    private TreeNode l_rotation(TreeNode P, TreeNode R) {
        TreeNode G = P.parent;
        P.rightChild = R.leftChild;
        if (R.leftChild != null)
            R.leftChild.parent = P;
        R.leftChild = P;
        R.parent = G;
        P.parent = R;
        if (G == null)
            this.Root = R;
        else if (G.rightChild == P)
            G.rightChild = R;
        else
            G.leftChild = R;
        return R;
    }

    private TreeNode rl_rotation(TreeNode P, TreeNode R) {
        P.rightChild  = r_rotation(P.rightChild, R);
        P = l_rotation(P, P.rightChild);
        return P;
    }

    private TreeNode lr_rotation(TreeNode P, TreeNode L) {
        P.leftChild = l_rotation(P.leftChild, L);
        P = r_rotation(P, P.leftChild);
        return P;
    }

    private TreeNode findNode(Object key) {
        TreeNode current = this.Root;
        while (current != null) {
            int cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0)
                current = current.leftChild;
            else if (cmp > 0)
                current = current.rightChild;
            else
                return current;
        }
        return null;
    }

    private void balanceTree(TreeNode S) {
        while (S != this.Root && S.parent.color) {
            TreeNode G = S.parent.parent;
            if (S.parent == G.leftChild) {
                TreeNode U = G.rightChild;
                if (is_red(U)) {
                    S.parent.flip_color();
                    U.flip_color();
                    G.flip_color();
                    S = G;
                    continue;
                }
                if (S == S.parent.rightChild) {
                    lr_rotation(G, S);
                    S.flip_color();
                    G.flip_color();
                }
                else {
                    r_rotation(G, S.parent);
                    S.parent.flip_color();
                    G.flip_color();
                }
                break;
            }
            else {
                TreeNode U = G.leftChild;
                if (is_red(U)) {
                    S.parent.flip_color();
                    U.flip_color();
                    G.flip_color();
                    S = G;
                    continue;
                }
                if (S == S.parent.leftChild) {
                    rl_rotation(G, S);
                    S.flip_color();
                    G.flip_color();
                }
                else {
                    l_rotation(G, S.parent);
                    S.parent.flip_color();
                    G.flip_color();
                }
                break;
            }
        }
        this.Root.color = false;
    }

    private void fix_double_black(TreeNode X) {
        while (X != this.Root && !is_red(X)) {
            TreeNode P = X.parent;
            if (X == P.leftChild) {
                TreeNode S = P.rightChild;
                if (is_red(S)) {
                    S.color = false;
                    P.color = true;
                    l_rotation(P, S);
                    S = P.rightChild;
                }
                if (!is_red(S.leftChild) && !is_red(S.rightChild)) {
                    S.color = true;
                    X = P;
                }
                else {
                    if (!is_red(S.rightChild)) {
                        S.leftChild.color = false;
                        S.color = true;
                        r_rotation(S, S.leftChild);
                        S = P.rightChild;
                    }
                    S.color = P.color;
                    P.color = false;
                    S.rightChild.color = false;
                    l_rotation(P, S);
                    X = this.Root;
                }
            }
            else {
                TreeNode S = P.leftChild;
                if (is_red(S)) {
                    S.color = false;
                    P.color = true;
                    r_rotation(P, S);
                    S = P.leftChild;
                }
                if (!is_red(S.leftChild) && !is_red(S.rightChild)) {
                    S.color = true;
                    X = P;
                }
                else {
                    if (!is_red(S.leftChild)) {
                        S.rightChild.color = false;
                        S.color = true;
                        l_rotation(S, S.rightChild);
                        S = P.leftChild;
                    }
                    S.color = P.color;
                    P.color = false;
                    S.leftChild.color = false;
                    r_rotation(P, S);
                    X = this.Root;
                }
            }
        }
        X.color = false;
    }

    private void delete(TreeNode toRemove) {
        if (toRemove.leftChild != null && toRemove.rightChild != null) {
            TreeNode S = toRemove.rightChild;
            while (S.leftChild != null)
                S = S.leftChild;
            toRemove.key = S.key;
            toRemove.value = S.value;
            toRemove = S;
        }
        TreeNode P = toRemove.parent;
        TreeNode C = (toRemove.leftChild != null) ? toRemove.leftChild : toRemove.rightChild;
        if (C != null) {
            C.parent = P;
            if (P == null)
                this.Root = C;
            else if (P.leftChild == toRemove)
                P.leftChild = C;
            else
                P.rightChild = C;
            C.color = false;
        }
        else if (P == null)
            this.Root = null;
        else {
            if (!toRemove.color)
                fix_double_black(toRemove);
            TreeNode Parent = toRemove.parent;
            if (Parent.leftChild == toRemove)
                Parent.leftChild = null;
            else
                Parent.rightChild = null;
            toRemove.parent = null;
        }
    }

    @Override
    public String toString() {
        if (this.Root == null)
            return "{}";
        StringBuilder result = new StringBuilder("{");
        this.inorderTraversal(Root, result);
        return result.delete(result.length() - 2, result.length()).append("}").toString();
    }

    @Override
    public Object put(Object key, Object value) {
        if (this.Root == null) {
            this.Root = new TreeNode(key, value, false);
            ++size;
            return null;
        }
        TreeNode newNode = new TreeNode(key, value, true);
        TreeNode parent = null;
        TreeNode current = this.Root;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = ((Comparable<Object>)key).compareTo(current.key);
            if (cmp < 0)
                current = current.leftChild;
            else if (cmp > 0)
                current = current.rightChild;
            else {
                Object toReturn = current.value;
                current.value = value;
                return toReturn;
            }
        }
        if (cmp < 0)
            parent.leftChild = newNode;
        else
            parent.rightChild = newNode;
        newNode.parent = parent;
        ++size;
        balanceTree(newNode);
        return null;
    }

    @Override
    public Object remove(Object key) {
        TreeNode toRemove = findNode(key);
        if (toRemove == null)
            return null;
        Object toReturn = toRemove.value;
        delete(toRemove);
        --size;
        return toReturn;
    }

    @Override
    public Object get(Object key) {
        TreeNode toReturn = findNode(key);
        return (toReturn == null) ? null : toReturn.value;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.get(key) != null;
    }

    private boolean recursiveContainsValue(TreeNode N, Object value) {
        if (N == null)
            return false;
        return Objects.equals(value, N.value) || recursiveContainsValue(N.leftChild, value) || recursiveContainsValue(N.rightChild, value);
    }

    @Override
    public boolean containsValue(Object value) {
        return recursiveContainsValue(this.Root, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.Root = null;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.Root == null;
    }

    @Override
    public SortedMap headMap(Object toKey) {
        MyRbMap result = new MyRbMap();
        headTraversal(this.Root, toKey, result);
        return result;
    }

    @Override
    public SortedMap tailMap(Object fromKey) {
        MyRbMap result = new MyRbMap();
        headTraversalBack(this.Root, fromKey, result);
        return result;
    }

    @Override
    public Object firstKey() {
        TreeNode current = this.Root;
        TreeNode toReturn = null;
        while (current != null) {
            toReturn = current;
            current = current.leftChild;
        }
        return toReturn != null ? toReturn.key : null;
    }

    @Override
    public Object lastKey() {
        TreeNode current = this.Root;
        TreeNode toReturn = null;
        while (current != null) {
            toReturn = current;
            current = current.rightChild;
        }
        return toReturn != null ? toReturn.key : null;
    }



    @Override
    public Comparator comparator() {
        return null;
    }

    @Override
    public SortedMap subMap(Object fromKey, Object toKey) {
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public Set keySet() {
        return Set.of();
    }

    @Override
    public Collection values() {
        return List.of();
    }

    @Override
    public Set<Entry> entrySet() {
        return Set.of();
    }
}
