package by.it.group510902.slesar.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
            if (parent[i] == i) {
                return i;
            }
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
                if (!siteToId.containsKey(parts[0])) {
                    siteToId.put(parts[0], siteToId.size());
                }
                if (!siteToId.containsKey(parts[1])) {
                    siteToId.put(parts[1], siteToId.size());
                }
                edges.add(new int[]{siteToId.get(parts[0]), siteToId.get(parts[1])});
            }
        }

        int n = siteToId.size();
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
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i) + (i == clusterSizes.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}