package by.it.group551002.sevkovich.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesB {

    private static class DSU {
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

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> ids = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            int plus = line.indexOf('+');
            if (plus < 0) {
                continue;
            }
            String a = line.substring(0, plus);
            String b = line.substring(plus + 1);
            int ia = getId(ids, a);
            int ib = getId(ids, b);
            pairs.add(new int[]{ia, ib});
        }

        DSU dsu = new DSU(ids.size());
        for (int[] p : pairs) {
            dsu.union(p[0], p[1]);
        }

        int count = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (dsu.find(i) == i) {
                count++;
            }
        }
        int[] sizes = new int[count];
        int idx = 0;
        for (int i = 0; i < ids.size(); i++) {
            if (dsu.find(i) == i) {
                sizes[idx++] = dsu.size[i];
            }
        }
        Arrays.sort(sizes);
        StringBuilder sb = new StringBuilder();
        for (int i = sizes.length - 1; i >= 0; i--) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(sizes[i]);
        }
        System.out.println(sb.toString());
    }

    private static int getId(Map<String, Integer> ids, String name) {
        Integer id = ids.get(name);
        if (id == null) {
            id = ids.size();
            ids.put(name, id);
        }
        return id;
    }
}