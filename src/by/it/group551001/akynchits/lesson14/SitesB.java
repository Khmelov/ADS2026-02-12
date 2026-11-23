package by.it.group551001.akynchits.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesB {

    static class DSU {
        private int[] parent;
        private int[] size;
        private int count;

        public DSU(int initialCapacity) {
            int cap = Math.max(1, initialCapacity);
            parent = new int[cap];
            size = new int[cap];
            count = 0;
        }

        public int makeSet() {
            if (count == parent.length) {
                int newCapacity = parent.length * 2;
                parent = Arrays.copyOf(parent, newCapacity);
                size = Arrays.copyOf(size, newCapacity);
            }
            int id = count++;
            parent[id] = id;
            size[id] = 1;
            return id;
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return;
            }
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        public int getCount() {
            return count;
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> siteToId = new HashMap<>();
        DSU dsu = new DSU(16);

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            int plusIdx = line.indexOf('+');
            if (plusIdx == -1) {
                continue;
            }

            String site1 = line.substring(0, plusIdx).trim();
            String site2 = line.substring(plusIdx + 1).trim();

            int id1 = siteToId.computeIfAbsent(site1, k -> dsu.makeSet());
            int id2 = siteToId.computeIfAbsent(site2, k -> dsu.makeSet());

            dsu.union(id1, id2);
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < dsu.getCount(); i++) {
            if (dsu.find(i) == i) {
                clusterSizes.add(dsu.getSize(i));
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                result.append(' ');
            }
            result.append(clusterSizes.get(i));
        }
        System.out.println(result);
    }
}
