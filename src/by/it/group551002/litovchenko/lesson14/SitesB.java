package by.it.group551002.litovchenko.lesson14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class SitesB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, Integer> siteToInd = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();

        String line = scanner.nextLine();
        while (!line.equals("end") && !line.isEmpty()) {
            String[] sites = line.split("\\+");
            int id1 = siteToInd.computeIfAbsent(sites[0], k -> siteToInd.size());
            int id2 = siteToInd.computeIfAbsent(sites[1], k -> siteToInd.size());
            pairs.add(new int[]{id1, id2});

            line = scanner.nextLine();
        }

        DSU dsu = new DSU(siteToInd.size());

        for (int[] pair: pairs) {
            dsu.union(pair[0], pair[1]);
        }


        List<Integer> clusterSizes = new ArrayList<>();
       for (int i = 0; i < siteToInd.size(); i++) {
            if (dsu.isRoot(i)) {
                clusterSizes.add(dsu.getSize(i));
            }
        }

        clusterSizes.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(clusterSizes.get(i));
        }
        System.out.println(sb);
    }
}
