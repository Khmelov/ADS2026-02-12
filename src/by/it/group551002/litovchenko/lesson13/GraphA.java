package by.it.group551002.litovchenko.lesson13;

import java.util.*;

public class GraphA {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Map<String, Set<String>> graph = new HashMap<>();
        Map<String, Integer> inOrder = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();

        for (String str: scanner.nextLine().split(",")){
            String[] vertexes = str.split("->");
            String outVert = vertexes[0].trim();
            String inVert = vertexes[1].trim();

            graph.computeIfAbsent(outVert, k -> new HashSet<>());

            if (graph.get(outVert).add(inVert)){
                inOrder.put(inVert, inOrder.getOrDefault(inVert, 0) + 1);
            }
        }

        PriorityQueue<String>  readyVert = new PriorityQueue<>();

        for (String key: graph.keySet()){
            if (inOrder.getOrDefault(key, 0) == 0) {
                readyVert.add(key);
            }
        }

        while (!readyVert.isEmpty()){
            String removeVert = readyVert.poll();
            result.add(removeVert);

            for (String inVert : graph.getOrDefault(removeVert, Collections.emptySet())){
                inOrder.put(inVert, inOrder.get(inVert) - 1);
                if (inOrder.get(inVert) == 0) readyVert.add(inVert);
            }
        }


        System.out.println(String.join(" ", result));
    }
}
