package by.it.group551002.marochkov.lesson13;

import java.util.*;

public class GraphB {

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        Map<Integer, List<Integer>> graph = parse(line);
        if(containsCycles(graph)){
            System.out.println("yes");
        }else{
            System.out.println("no");
        }

    }



    private static Map parse(String str){
        Map<Integer, List<Integer>> graph = new TreeMap<>();

        for(String edge : str.split(",")){
            String[] parts = edge.split("->");
            int from = Integer.parseInt(parts[0].trim());
            int to = Integer.parseInt(parts[1].trim());

            graph.computeIfAbsent(from,k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }

        return graph;

    }


    private static boolean containsCycles(Map<Integer, List<Integer>> graph){

        Map<Integer, Integer> state = new HashMap<>();
        for (Integer v : graph.keySet()) {
            if (dfs(v, state, graph)) return true;
        }
        return false;

    }



    private static boolean dfs(Integer v,Map<Integer, Integer> state,Map<Integer, List<Integer>> graph){
        int s = state.getOrDefault(v, 0);
        if (s == 1) return true;
        if (s == 2) return false;

        state.put(v, 1);
        for (Integer to : graph.get(v)) {
            if (dfs(to, state, graph)) return true;
        }
        state.put(v, 2);
        return false;
    }
















}
