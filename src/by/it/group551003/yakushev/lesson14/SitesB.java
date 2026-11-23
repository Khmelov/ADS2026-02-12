package by.it.group551003.yakushev.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SitesB {

    private static int[] parent;
    private static int[] size;
    private static int count;
    private static Map<String, Integer> ids;

    private static int getId(String s) {
        Integer id = ids.get(s);
        if (id != null) return id;
        if (count == parent.length) {
            parent = Arrays.copyOf(parent, count * 2);
            size = Arrays.copyOf(size, count * 2);
        }
        parent[count] = count;
        size[count] = 1;
        ids.put(s, count);
        return count++;
    }

    // Эвристика 2: сжатие путей (итеративно)
    private static int find(int x) {
        int root = x;
        while (parent[root] != root) root = parent[root];
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // Эвристика 1: объединение по размеру
    private static void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return;
        if (size[ra] < size[rb]) {
            int t = ra; ra = rb; rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }

    public static void main(String[] args) throws IOException {
        // сброс состояния: main может вызываться многократно в одной JVM
        parent = new int[16];
        size = new int[16];
        count = 0;
        ids = new HashMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            int p = line.indexOf('+');
            if (p < 0) continue;
            String a = line.substring(0, p).trim();
            String b = line.substring(p + 1).trim();
            union(getId(a), getId(b));
        }

        List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (find(i) == i) sizes.add(size[i]);
        }
        sizes.sort(Collections.reverseOrder());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);
    }
}