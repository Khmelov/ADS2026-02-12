package by.it.group510902.shust.lesson14;

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

        int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]);
        }

        void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI; rootI = rootJ; rootJ = temp;
                }
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> siteToIndex = new HashMap<>();
        List<int[]> edges = new ArrayList<>();

        int index = 0;
        while (scanner.hasNext()) {
            String line = scanner.next();
            if (line.equals("end")) break;
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                if (!siteToIndex.containsKey(sites[0])) siteToIndex.put(sites[0], index++);
                if (!siteToIndex.containsKey(sites[1])) siteToIndex.put(sites[1], index++);
                edges.add(new int[]{siteToIndex.get(sites[0]), siteToIndex.get(sites[1])});
            }
        }

        DSU dsu = new DSU(index);
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            if (dsu.parent[i] == i) {
                sizes.add(dsu.size[i]);
            }
        }

        sizes.sort(Collections.reverseOrder());

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i) + (i == sizes.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}