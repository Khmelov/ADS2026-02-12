package by.it.group510901.chernyavskaya.lesson14;
//сгруппировать сайты, связанные гиперссылками, и вывести размеры полученных сетей по убыванию.
import java.util.*;

public class SitesB {
    static int[] parent;
    static int[] size;

    static int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    static void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if (rootA != rootB) {
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> sites = new HashMap<>();
        List<int[]> links = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;
            String[] parts = line.split("\\+");
            sites.putIfAbsent(parts[0], sites.size());
            sites.putIfAbsent(parts[1], sites.size());
            links.add(new int[]{sites.get(parts[0]), sites.get(parts[1])});
        }

        int n = sites.size();
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        for (int[] link : links) {
            union(link[0], link[1]);
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                res.add(size[i]);
            }
        }

        res.sort(Collections.reverseOrder());
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i) + (i == res.size() - 1 ? "" : " "));
        }
        System.out.println();
    }
}