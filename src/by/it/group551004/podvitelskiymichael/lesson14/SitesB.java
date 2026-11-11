package by.it.group551004.podvitelskiymichael.lesson14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {

    private static int[] parent;
    private static int[] size;

    private static int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (size[a] < size[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end")) {
                break;
            }
            if (!line.isEmpty()) {
                lines.add(line);
            }
        }

        int maxSites = lines.size() * 2;
        parent = new int[maxSites];
        size = new int[maxSites];
        Map<String, Integer> ids = new HashMap<>();

        for (String line : lines) {
            String[] pair = line.split("\\+");
            int a = getId(ids, pair[0].trim());
            int b = getId(ids, pair[1].trim());
            union(a, b);
        }

        int n = ids.size();
        int[] sizes = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                sizes[count++] = size[i];
            }
        }

        for (int i = 1; i < count; i++) {
            int v = sizes[i];
            int j = i - 1;
            while (j >= 0 && sizes[j] < v) {
                sizes[j + 1] = sizes[j];
                j--;
            }
            sizes[j + 1] = v;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(sizes[i]);
        }
        System.out.println(sb);
    }

    private static int getId(Map<String, Integer> ids, String site) {
        Integer id = ids.get(site);
        if (id == null) {
            id = ids.size();
            ids.put(site, id);
            parent[id] = id;
            size[id] = 1;
        }
        return id;
    }
}