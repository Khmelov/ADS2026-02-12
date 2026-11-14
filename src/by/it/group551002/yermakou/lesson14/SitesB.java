package by.it.group551002.yermakou.lesson14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SitesB {

    static List<Integer> parent = new ArrayList<>();
    static List<Integer> size = new ArrayList<>();

    static int find(int x) {
        int root = x;
        while (parent.get(root) != root) {
            root = parent.get(root);
        }

        while (parent.get(x) != root) {
            int next = parent.get(x);
            parent.set(x, root);
            x = next;
        }
        return root;
    }

    static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (size.get(ra) < size.get(rb)) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent.set(rb, ra);
        size.set(ra, size.get(ra) + size.get(rb));
    }

    static int idOf(Map<String, Integer> ids, String site) {
        Integer id = ids.get(site);
        if (id == null) {
            id = parent.size();
            ids.put(site, id);
            parent.add(id);
            size.add(1);
        }
        return id;
    }

    public static void main(String[] args) {
        parent.clear();
        size.clear();

        Scanner sc = new Scanner(System.in);
        Map<String, Integer> ids = new HashMap<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\+");
            if (parts.length < 2) continue;
            int a = idOf(ids, parts[0].trim());
            int b = idOf(ids, parts[1].trim());
            union(a, b);
        }

        int n = parent.size();
        int[] sizes = new int[n];
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if (find(i) == i) {
                sizes[cnt++] = size.get(i);
            }
        }
        Arrays.sort(sizes, 0, cnt);

        StringBuilder sb = new StringBuilder();
        for (int i = cnt - 1; i >= 0; i--) {
            sb.append(sizes[i]);
            if (i > 0) sb.append(' ');
        }
        System.out.println(sb);
    }
}