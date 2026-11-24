package by.it.group510902.ryzhkov.lesson14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {
    static class DSU {
        int[] parent, rank;
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;
            if (rank[rx] < rank[ry]) parent[rx] = ry;
            else if (rank[rx] > rank[ry]) parent[ry] = rx;
            else { parent[ry] = rx; rank[rx]++; }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> id = new HashMap<>();
        List<int[]> edges = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            String a = parts[0], b = parts[1];
            id.putIfAbsent(a, id.size());
            id.putIfAbsent(b, id.size());
            edges.add(new int[]{id.get(a), id.get(b)});
        }
        int n = id.size();
        DSU dsu = new DSU(n);
        for (int[] e : edges) dsu.union(e[0], e[1]);

        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            count.put(root, count.getOrDefault(root, 0) + 1);
        }
        List<Integer> sizes = new ArrayList<>(count.values());
        sizes.sort((a, b) -> b - a); 
        for (int size : sizes) System.out.print(size + " ");
    }
}
