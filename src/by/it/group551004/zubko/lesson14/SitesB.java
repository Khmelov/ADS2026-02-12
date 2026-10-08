package by.it.group551004.zubko.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int capacity) {
            parent = new int[capacity];
            size = new int[capacity];
            for (int i = 0; i < capacity; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]);
        }

        void union(int i, int j) {
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
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end") || line.isEmpty()) break;

            int plusIdx = line.indexOf('+');
            if (plusIdx != -1) {
                String site1 = line.substring(0, plusIdx).trim();
                String site2 = line.substring(plusIdx + 1).trim();

                int id1 = siteToIndex.computeIfAbsent(site1, k -> siteToIndex.size());
                int id2 = siteToIndex.computeIfAbsent(site2, k -> siteToIndex.size());

                pairs.add(new int[]{id1, id2});
            }
        }

        int n = siteToIndex.size();
        DSU dsu = new DSU(n);
        for (int[] pair : pairs) {
            dsu.union(pair[0], pair[1]);
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        clusterSizes.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(clusterSizes.get(i));
        }
        System.out.println(sb.toString());
    }
}
