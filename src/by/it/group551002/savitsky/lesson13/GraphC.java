package by.it.group551002.savitsky.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Map<String, List<String>> neighbors = new TreeMap<>();
        Map<String, List<String>> reversedNeighbors = new TreeMap<>();

        String[] arrows = input.split(",");

        for (String arrow : arrows) {
            String[] ends = arrow.split("->");
            String source = ends[0].trim();
            String target = ends[1].trim();

            neighbors.computeIfAbsent(source, summit -> new ArrayList<>()).add(target);
            neighbors.computeIfAbsent(target, summit -> new ArrayList<>());

            reversedNeighbors.computeIfAbsent(target, summit -> new ArrayList<>()).add(source);
            reversedNeighbors.computeIfAbsent(source, summit -> new ArrayList<>());
        }

        Set<String> visited = new HashSet<>();
        List<String> exitOrder = new ArrayList<>();

        for (String summit : neighbors.keySet()) {
            if (!visited.contains(summit)) {
                visit(summit, neighbors, visited, exitOrder);
            }
        }

        Set<String> visitedOnReversed = new HashSet<>();

        for (int i = exitOrder.size() - 1; i >= 0; i--) {
            String summit = exitOrder.get(i);

            if (!visitedOnReversed.contains(summit)) {
                List<String> component = new ArrayList<>();
                visit(summit, reversedNeighbors, visitedOnReversed, component);

                Collections.sort(component);
                System.out.println(String.join("", component));
            }
        }
    }

    private static void visit(String summit, Map<String, List<String>> graph,
                              Set<String> visited, List<String> exitOrder) {

        visited.add(summit);

        for (String neighbor : graph.get(summit)) {
            if (!visited.contains(neighbor)) {
                visit(neighbor, graph, visited, exitOrder);
            }
        }

        exitOrder.add(summit);
    }
}
