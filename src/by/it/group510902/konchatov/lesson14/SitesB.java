package by.it.group510902.konchatov.lesson14;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesB {

    static class DSU {
        private final int[] parent;
        private final int[] size;
        private final int[] count;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            count = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 0;
                count[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rx = find(x);
            int ry = find(y);
            if (rx == ry) return;

            if (size[rx] < size[ry]) {
                parent[rx] = ry;
                count[ry] += count[rx];
            } else if (size[rx] > size[ry]) {
                parent[ry] = rx;
                count[rx] += count[ry];
            } else {
                parent[ry] = rx;
                count[rx] += count[ry];
                size[rx]++;
            }
        }

        int sizeOf(int x) {
            return count[find(x)];
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Map<String, Integer> id = new HashMap<>();
        List<int[]> edges = new ArrayList<>();
        int counter = 0;

        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.equals("end")) break;

            int plus = line.indexOf('+');
            if (plus < 0) continue; 

            String a = line.substring(0, plus).trim();
            String b = line.substring(plus + 1).trim();

            if (!id.containsKey(a)) id.put(a, counter++);
            if (!id.containsKey(b)) id.put(b, counter++);

            edges.add(new int[]{ id.get(a), id.get(b) });
        }

        DSU dsu = new DSU(counter);
        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            if (dsu.find(i) == i) {
                sizes.add(dsu.sizeOf(i));
            }
        }
        sizes.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);
    }
}