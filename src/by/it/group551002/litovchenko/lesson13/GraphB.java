package by.it.group551002.litovchenko.lesson13;

import java.util.*;

public class GraphB {
    private static boolean hasCycle(String u, Map<String, Set<String>> graph,
                            Set<String> visited, Set<String> inPath) {
        visited.add(u);
        inPath.add(u);
        for (String next : graph.getOrDefault(u, Collections.emptySet())) {
            if (inPath.contains(next)) {
                return true;
            }
            if (!visited.contains(next)) {
                if (hasCycle(next, graph, visited, inPath)) {
                    return true;
                }
            }
        }
        inPath.remove(u); // вышли из вершины
        return false;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Map<String, Set<String>> graph = new HashMap<>();

        for (String str: scanner.nextLine().split(",")){
            if (str.trim().isEmpty()) {
                continue;
            }
            String[] vertexes = str.split("->");
            String outVert = vertexes[0].trim();
            String inVert = vertexes[1].trim();

            graph.computeIfAbsent(outVert, k -> new HashSet<>());
            graph.computeIfAbsent(inVert, k -> new HashSet<>());
            graph.get(outVert).add(inVert);
        }

        Set<String> visited = new HashSet<>();
        Set<String> inPath = new HashSet<>();
        boolean hasCycle = false;

        for (String vert : graph.keySet()) {
            if (!visited.contains(vert) && hasCycle(vert, graph, visited, inPath)) {
                hasCycle = true;
                break;
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }
}
