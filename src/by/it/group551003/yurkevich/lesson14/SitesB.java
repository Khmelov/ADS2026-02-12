package by.it.group551003.yurkevich.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        int[] parent;
        int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            return parent[i] = find(parent[i]);
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();

        List<String[]> edges = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty() || !line.contains("+")) {
                continue;
            }

            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0].trim();
                String site2 = sites[1].trim();

                edges.add(new String[]{site1, site2});

                if (!siteToId.containsKey(site1)) {
                    siteToId.put(site1, siteToId.size());
                }
                if (!siteToId.containsKey(site2)) {
                    siteToId.put(site2, siteToId.size());
                }
            }
        }

        int n = siteToId.size();
        DSU dsu = new DSU(n);

        for (String[] edge : edges) {
            int id1 = siteToId.get(edge[0]);
            int id2 = siteToId.get(edge[1]);
            dsu.union(id1, id2);
        }

        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        Collections.sort(sizes, Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(sizes.get(i));
        }
        System.out.println(sb.toString());
    }
}