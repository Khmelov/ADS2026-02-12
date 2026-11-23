package by.it.group551002.voroshko.lesson14;

import java.util.*;

public class SitesB {
    static class DSU {
        private int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        int find(int x) {
            if (x == parent[x]) return x;
            return parent[x] = find(parent[x]);
        }

        void union(int x, int y) {
            x = find(x);
            y = find(y);

            if (x == y) return;

            if (rank[x] < rank[y]) {
                int t = x;
                x = y;
                y = t;
            }

            parent[y] = x;

            rank[x] += rank[y];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<String[]> edges = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.equals("end")) break;
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\+");
            edges.add(parts);
        }

        sc.close();

        Map<String, Integer> id = new HashMap<>();
        for (String[] s : edges) {
            id.putIfAbsent(s[0], id.size());
            id.putIfAbsent(s[1], id.size());
        }

        DSU dsu = new DSU(id.size());

        for (String[] s : edges) {
            dsu.union(id.get(s[0]), id.get(s[1]));
        }

        Map<Integer, Integer> sizes = new HashMap<>();

        for (int i = 0; i < id.size(); i++) {
            int root = dsu.find(i);
            sizes.merge(root, 1, Integer::sum);
        }

        List<Integer> res = new ArrayList<>(sizes.values());
        Collections.sort(res, Collections.reverseOrder());

        for (int i = 0; i < res.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(res.get(i));
        }
        System.out.println();
    }
}
