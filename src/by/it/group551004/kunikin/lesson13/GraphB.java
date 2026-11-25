package by.it.group551004.kunikin.lesson13;

import java.util.*;

public class GraphB {
    private enum VertexState {
        NOT_VISITED,
        EXPLORING,
        FINISHED
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder textBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                if (textBuilder.length() > 0) {
                    textBuilder.append(", ");
                }
                textBuilder.append(line);
            }
        }

        String input = textBuilder.toString().trim();
        if (input.isEmpty()) {
            System.out.println("no");
            return;
        }

        Map<String, List<String>> adjacency = new HashMap<>();
        for (String segment : input.split(",")) {
            String[] tokens = segment.split("->");
            if (tokens.length == 2) {
                String source = tokens[0].trim();
                String target = tokens[1].trim();
                adjacency.computeIfAbsent(source, k -> new ArrayList<>()).add(target);
                adjacency.putIfAbsent(target, new ArrayList<>());
            }
        }

        Map<String, VertexState> states = new HashMap<>();
        for (String vertex : adjacency.keySet()) {
            states.put(vertex, VertexState.NOT_VISITED);
        }

        for (String vertex : adjacency.keySet()) {
            if (states.get(vertex) == VertexState.NOT_VISITED) {
                if (hasCycle(vertex, adjacency, states)) {
                    System.out.println("yes");
                    return;
                }
            }
        }

        System.out.println("no");
    }

    private static boolean hasCycle(String current, Map<String, List<String>> adjacency, Map<String, VertexState> states) {
        states.put(current, VertexState.EXPLORING);

        for (String next : adjacency.get(current)) {
            VertexState nextState = states.get(next);
            if (nextState == VertexState.EXPLORING) {
                return true;
            }
            if (nextState == VertexState.NOT_VISITED && hasCycle(next, adjacency, states)) {
                return true;
            }
        }

        states.put(current, VertexState.FINISHED);
        return false;
    }
}
