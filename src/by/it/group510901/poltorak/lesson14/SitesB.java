package by.it.group510901.poltorak.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> siteToId = new HashMap<>();
        List<int[]> edges = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("end")) {
                break;
            }

            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String site1 = parts[0].trim();
                String site2 = parts[1].trim();

                int id1 = siteToId.computeIfAbsent(site1, k -> siteToId.size());
                int id2 = siteToId.computeIfAbsent(site2, k -> siteToId.size());

                edges.add(new int[]{id1, id2});
            }
        }

        int n = siteToId.size();
        if (n == 0) {
            return;
        }

        DSU dsu = new DSU(n);

        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (dsu.parent[i] == i) {
                clusterSizes.add(dsu.size[i]);
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(clusterSizes.get(i));
        }

        System.out.println(sb);
    }
}