package by.it.group551001.romanovich.lesson14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SitesB {
    static DisjointSetUnion cluster(BufferedReader input) throws IOException {
        DisjointSetUnion groups = new DisjointSetUnion(16);
        Map<String, Integer> ids = new HashMap<>();
        String line;
        while ((line = input.readLine()) != null) {
            line = line.trim();
            if (line.equals("end")) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }
            String[] pair = line.split("\\+", -1);
            if (pair.length != 2 || pair[0].isBlank() || pair[1].isBlank()) {
                throw new IllegalArgumentException("Expected a pair: site+site");
            }
            int first = ids.computeIfAbsent(pair[0].trim(), ignored -> groups.add());
            int second = ids.computeIfAbsent(pair[1].trim(), ignored -> groups.add());
            groups.union(first, second);
        }
        return groups;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println(cluster(input).sizesAsString(true));
    }
}
