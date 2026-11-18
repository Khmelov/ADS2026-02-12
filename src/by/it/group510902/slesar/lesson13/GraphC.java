package by.it.group510902.slesar.lesson13;

import java.util.*;

public class GraphC {

    static Map<Character, List<Character>> graph = new TreeMap<>();
    static Map<Character, List<Character>> reversedGraph = new TreeMap<>();

    static Set<Character> visited = new HashSet<>();
    static List<Character> order = new ArrayList<>();

    static void dfs1(char vertex) {
        visited.add(vertex);

        for (char neighbor : graph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs1(neighbor);
            }
        }

        order.add(vertex);
    }

    static void dfs2(char vertex, List<Character> component) {
        visited.add(vertex);
        component.add(vertex);

        for (char neighbor : reversedGraph.get(vertex)) {
            if (!visited.contains(neighbor)) {
                dfs2(neighbor, component);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String[] edges = input.split(",");

        for (String edge : edges) {
            String[] vertices = edge.trim().split("\\s*->\\s*");

            char from = vertices[0].charAt(0);
            char to = vertices[1].charAt(0);

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            reversedGraph.putIfAbsent(from, new ArrayList<>());
            reversedGraph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);
            reversedGraph.get(to).add(from);
        }

        for (char vertex : graph.keySet()) {
            Collections.sort(graph.get(vertex));
            Collections.sort(reversedGraph.get(vertex));
        }

        // First DFS: calculate finishing order
        for (char vertex : graph.keySet()) {
            if (!visited.contains(vertex)) {
                dfs1(vertex);
            }
        }

        // Second DFS: find strongly connected components
        visited.clear();
        Collections.reverse(order);

        for (char vertex : order) {
            if (!visited.contains(vertex)) {
                List<Character> component = new ArrayList<>();

                dfs2(vertex, component);

                Collections.sort(component);

                for (char c : component) {
                    System.out.print(c);
                }

                System.out.println();
            }
        }
    }
}