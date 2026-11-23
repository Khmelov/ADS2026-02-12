package by.it.group551002.savitsky.lesson13;

import java.util.*;

public class GraphA {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> neighbors = new HashMap<>();
        Map<String, Integer> incomingCount = new HashMap<>();

        String[] arrows = input.split(", ");

        for (String arrow : arrows) {
            String[] ends = arrow.split("->");
            String source = ends[0].trim();
            String target = ends[1].trim();

            neighbors.computeIfAbsent(source, summit -> new ArrayList<>()).add(target);
            neighbors.computeIfAbsent(target, summit -> new ArrayList<>());

            incomingCount.putIfAbsent(source, 0);
            incomingCount.merge(target, 1, Integer::sum);
        }

        PriorityQueue<String> readySummits = new PriorityQueue<>();

        for (Map.Entry<String, Integer> entry : incomingCount.entrySet()) {
            if (entry.getValue() == 0) {
                readySummits.add(entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();

        while (!readySummits.isEmpty()) {
            String current = readySummits.poll();
            result.add(current);

            for (String neighbor : neighbors.get(current)) {
                incomingCount.put(neighbor, incomingCount.get(neighbor) - 1);

                if (incomingCount.get(neighbor) == 0) {
                    readySummits.add(neighbor);
                }
            }
        }

        System.out.println(String.join(" ", result));
    }
}
