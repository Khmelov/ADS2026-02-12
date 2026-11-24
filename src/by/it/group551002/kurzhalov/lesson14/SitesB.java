package by.it.group551002.kurzhalov.lesson14;

import java.util.Scanner;

public class SitesB {

    // ================= DSU: ранг + сжатие путей =================

    private static class DSU {
        private int[] parent;
        private int[] rank;
        private int[] size;
        private int capacity;
        private int count;

        DSU(int initialCapacity) {
            capacity = Math.max(initialCapacity, 16);
            parent = new int[capacity];
            rank = new int[capacity];
            size = new int[capacity];
            count = 0;
        }

        int add() {
            if (count == capacity) {
                grow();
            }
            parent[count] = count;
            rank[count] = 0;
            size[count] = 1;
            return count++;
        }

        private void grow() {
            int newCap = capacity * 2;
            int[] p = new int[newCap];
            int[] r = new int[newCap];
            int[] s = new int[newCap];
            System.arraycopy(parent, 0, p, 0, capacity);
            System.arraycopy(rank, 0, r, 0, capacity);
            System.arraycopy(size, 0, s, 0, capacity);
            parent = p;
            rank = r;
            size = s;
            capacity = newCap;
        }

        int find(int x) {
            int root = x;
            while (parent[root] != root) {
                root = parent[root];
            }
            while (parent[x] != root) {
                int next = parent[x];
                parent[x] = root;
                x = next;
            }
            return root;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
                size[ra] += size[rb];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
                rank[ra]++;
            }
        }

        int componentSize(int x) {
            return size[find(x)];
        }

        int total() {
            return count;
        }
    }

    // ================= main =================

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] names = new String[16];
        int nameCount = 0;

        DSU dsu = new DSU(16);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equals("end")) break;

            int plus = line.indexOf('+');
            if (plus < 0) continue;
            String a = line.substring(0, plus).trim();
            String b = line.substring(plus + 1).trim();
            if (a.isEmpty() || b.isEmpty()) continue;

            int ia = indexOf(names, nameCount, a);
            if (ia < 0) {
                if (nameCount == names.length) {
                    String[] bigger = new String[names.length * 2];
                    System.arraycopy(names, 0, bigger, 0, names.length);
                    names = bigger;
                }
                names[nameCount] = a;
                ia = nameCount;
                dsu.add();
                nameCount++;
            }
            int ib = indexOf(names, nameCount, b);
            if (ib < 0) {
                if (nameCount == names.length) {
                    String[] bigger = new String[names.length * 2];
                    System.arraycopy(names, 0, bigger, 0, names.length);
                    names = bigger;
                }
                names[nameCount] = b;
                ib = nameCount;
                dsu.add();
                nameCount++;
            }

            dsu.union(ia, ib);
        }
        scanner.close();

        int n = dsu.total();
        int[] sizes = new int[n];
        int count = 0;
        boolean[] seenRoot = new boolean[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            if (!seenRoot[root]) {
                seenRoot[root] = true;
                sizes[count++] = dsu.componentSize(root);
            }
        }

        // ===== СОРТИРОВКА ПО УБЫВАНИЮ (ключевая правка) =====
        for (int i = 1; i < count; i++) {
            int key = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] < key) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString());
    }

    // ================= вспомогательные =================

    private static int indexOf(String[] arr, int count, String s) {
        for (int i = 0; i < count; i++) {
            if (arr[i].equals(s)) return i;
        }
        return -1;
    }
}