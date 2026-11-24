package by.it.group551003.popko.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SitesB {
    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int capacity) {
            parent = new int[capacity];
            size = new int[capacity];
            for (int i = 0; i < capacity; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public void ensureCapacity(int capacity) {
            if (capacity > parent.length) {
                int oldLen = parent.length;
                parent = Arrays.copyOf(parent, capacity);
                size = Arrays.copyOf(size, capacity);
                for (int i = oldLen; i < capacity; i++) {
                    parent[i] = i;
                    size[i] = 1;
                }
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]); // Сжатие пути
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> siteToId = new HashMap<>();
        DSU dsu = new DSU(100);
        int idCounter = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("end") || line.isEmpty()) {
                break;
            }
            String[] parts = line.split("\\+");
            if (parts.length != 2) continue;

            String s1 = parts[0].trim();
            String s2 = parts[1].trim();

            Integer id1 = siteToId.get(s1);
            if (id1 == null) {
                id1 = idCounter++;
                siteToId.put(s1, id1);
            }

            Integer id2 = siteToId.get(s2);
            if (id2 == null) {
                id2 = idCounter++;
                siteToId.put(s2, id2);
            }

            dsu.ensureCapacity(idCounter);
            dsu.union(id1, id2);
        }

        int n = idCounter;
        int[] clusterSizes = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes[count++] = dsu.size[i];
            }
        }

        int[] finalSizes = Arrays.copyOf(clusterSizes, count);
        Arrays.sort(finalSizes);

        StringBuilder sb = new StringBuilder();
        for (int i = finalSizes.length - 1; i >= 0; i--) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(finalSizes[i]);
        }
        System.out.println(sb);
    }
}