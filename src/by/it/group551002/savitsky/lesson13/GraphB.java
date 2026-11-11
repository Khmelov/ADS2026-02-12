package by.it.group551002.savitsky.lesson13;

import java.util.*;

public class GraphB {

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

        Queue<String> readySummits = new ArrayDeque<>();

        for (Map.Entry<String, Integer> entry : incomingCount.entrySet()) {
            if (entry.getValue() == 0) {
                readySummits.add(entry.getKey());
            }
        }

        int processedCount = 0;

        while (!readySummits.isEmpty()) {
            String current = readySummits.poll();
            processedCount++;

            for (String neighbor : neighbors.get(current)) {
                incomingCount.put(neighbor, incomingCount.get(neighbor) - 1);

                if (incomingCount.get(neighbor) == 0) {
                    readySummits.add(neighbor);
                }
            }
        }

        if (processedCount == neighbors.size()) {
            System.out.println("no");
        } else {
            System.out.println("yes");
        }
    }
}
