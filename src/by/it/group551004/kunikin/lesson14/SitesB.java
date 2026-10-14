package by.it.group551004.kunikin.lesson14;

import java.util.*;

public class SitesB {
    static class DisjointSet {
        private final int[] parent;
        private final int[] size;

        DisjointSet(int capacity) {
            parent = new int[capacity];
            size = new int[capacity];
            for (int i = 0; i < capacity; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA != rootB) {
                // Union by size
                if (size[rootA] < size[rootB]) {
                    parent[rootA] = rootB;
                    size[rootB] += size[rootA];
                } else {
                    parent[rootB] = rootA;
                    size[rootA] += size[rootB];
                }
            }
        }

        int getSize(int root) {
            return size[root];
        }

        boolean isRoot(int x) {
            return parent[x] == x;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteIndices = new HashMap<>();
        List<int[]> connections = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("end")) {
                break;
            }

            int separatorIndex = line.indexOf('+');
            if (separatorIndex != -1) {
                String firstSite = line.substring(0, separatorIndex).trim();
                String secondSite = line.substring(separatorIndex + 1).trim();

                int id1 = siteIndices.computeIfAbsent(firstSite, k -> siteIndices.size());
                int id2 = siteIndices.computeIfAbsent(secondSite, k -> siteIndices.size());

                connections.add(new int[]{id1, id2});
            }
        }

        int totalSites = siteIndices.size();
        if (totalSites == 0) {
            return;
        }

        DisjointSet dsu = new DisjointSet(totalSites);
        for (int[] pair : connections) {
            dsu.union(pair[0], pair[1]);
        }

        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < totalSites; i++) {
            if (dsu.isRoot(i)) {
                clusterSizes.add(dsu.getSize(i));
            }
        }

        clusterSizes.sort(Collections.reverseOrder());

        StringJoiner joiner = new StringJoiner(" ");
        for (int sz : clusterSizes) {
            joiner.add(String.valueOf(sz));
        }

        System.out.println(joiner);
    }
}
