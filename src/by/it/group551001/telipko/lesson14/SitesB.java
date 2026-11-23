package by.it.group551001.telipko.lesson14;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class SitesB {

    static int[] parent;
    static int[] size;
    static int count;
    static Map<String, Integer> id;


    static int idOf(String site) {
        Integer v = id.get(site);
        if (v != null) return v;
        if (count == parent.length) {
            int[] p2 = new int[count * 2], s2 = new int[count * 2];
            System.arraycopy(parent, 0, p2, 0, count);
            System.arraycopy(size, 0, s2, 0, count);
            parent = p2;
            size = s2;
        }
        parent[count] = count;
        size[count] = 1;
        id.put(site, count);
        return count++;
    }



    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) return;
        if (size[a] < size[b]) { int t = a; a = b; b = t; }
        parent[b] = a;
        size[a] += size[b];
    }

    public static void main(String[] args) {
        parent = new int[16];
        size = new int[16];
        count = 0;
        id = new HashMap<>();

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.equals("end")) break;
            if (line.isEmpty()) continue;
            String[] pair = line.split("\\+");
            union(idOf(pair[0]), idOf(pair[1]));
        }
        System.out.println(PointsA.clusterSizes(parent, size, count));
    }
}
