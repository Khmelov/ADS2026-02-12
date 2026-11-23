package by.it.group551002.litovchenko.lesson14;

public class DSU {
    private final int[] parent;
    private final int[] size;
    private int count;

    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        return parent[i] = find(parent[i]);
    }

    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
            count--;
            return true;
        }
        return false;
    }

    public boolean isRoot(int i) {
        return parent[i] == i;
    }

    public int getSize(int i) {
        return size[i];
    }

    public boolean connected(int i, int j) {
        return find(i) == find(j);
    }

    public int getCount() {
        return count;
    }
}
