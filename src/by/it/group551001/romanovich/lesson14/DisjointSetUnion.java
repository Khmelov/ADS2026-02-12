package by.it.group551001.romanovich.lesson14;

import java.util.Arrays;

final class DisjointSetUnion {
    private int[] parent;
    private int[] size;
    private int count;

    DisjointSetUnion(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative capacity");
        }
        parent = new int[Math.max(1, capacity)];
        size = new int[parent.length];
    }

    int add() {
        if (count == parent.length) {
            int capacity = Math.multiplyExact(parent.length, 2);
            parent = Arrays.copyOf(parent, capacity);
            size = Arrays.copyOf(size, capacity);
        }
        parent[count] = count;
        size[count] = 1;
        return count++;
    }

    int find(int element) {
        if (element < 0 || element >= count) {
            throw new IndexOutOfBoundsException("Unknown element: " + element);
        }
        int root = element;
        while (root != parent[root]) {
            root = parent[root];
        }
        while (element != root) {
            int next = parent[element];
            parent[element] = root;
            element = next;
        }
        return root;
    }

    boolean union(int first, int second) {
        int a = find(first);
        int b = find(second);
        if (a == b) {
            return false;
        }
        if (size[a] < size[b]) {
            int temp = a;
            a = b;
            b = temp;
        }
        parent[b] = a;
        size[a] += size[b];
        return true;
    }

    int componentSize(int element) {
        return size[find(element)];
    }

    int[] componentSizes() {
        int components = 0;
        for (int i = 0; i < count; i++) {
            if (parent[i] == i) {
                components++;
            }
        }
        int[] sizes = new int[components];
        int index = 0;
        for (int i = 0; i < count; i++) {
            if (parent[i] == i) {
                sizes[index++] = size[i];
            }
        }
        Arrays.sort(sizes);
        return sizes;
    }

    String sizesAsString(boolean descending) {
        int[] sizes = componentSizes();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(sizes[descending ? sizes.length - 1 - i : i]);
        }
        return result.toString();
    }
}
