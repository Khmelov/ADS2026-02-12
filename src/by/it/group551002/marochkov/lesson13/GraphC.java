package by.it.group551002.marochkov.lesson13;

import java.util.*;

public class GraphC {


    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        Map<String,List<String>> graph = parse(line);
        List<String> components = findComponents(graph);


        for(String component : components){
            System.out.println(component);
        }
    }


    private static Map parse(String str){
        Map<String, List<String>> graph = new TreeMap<>();

        for(String edge : str.split(",")){
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            graph.computeIfAbsent(from,k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }

        return graph;

    }


    private static Map<String, List<String>> transpose(Map<String, List<String>> graph){
        Map<String,List<String>> reversed = new TreeMap<>();

        for(String v : graph.keySet()){
            reversed.put(v, new ArrayList<>());
        }

        for(Map.Entry<String, List<String>> e : graph.entrySet()){
            for(String to : e.getValue()){
                reversed.get(to).add(e.getKey());
            }
        }
        return reversed;
    }


    private static void fillOrder(String v, Map<String, List<String>> graph,Set<String> visited, List<String> order){
        visited.add(v);
        for (String to : graph.get(v)) {
            if (!visited.contains(to)) {
                fillOrder(to, graph, visited, order);
            }
        }
        order.add(v);
    }

    private static void collect(String v,Map<String, List<String>> graph, Set<String> visited,Set<String>component){
        visited.add(v);
        component.add(v);
        for(String to : graph.get(v)){
            if(!visited.contains(to)){
                collect(to,graph,visited,component);
            }
        }
    }




    private static List<String> findComponents(Map<String, List<String>> graph){
        List<String> order = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for(String v : graph.keySet()){
            if(!visited.contains(v)){
                fillOrder(v,graph,visited,order);
            }
        }


        Map<String, List<String>> reversedGraph = transpose(graph);
        visited.clear();

        List<String> result = new ArrayList<>();

        for(int i = order.size() - 1; i >= 0;i--){
            String v = order.get(i);

            if(visited.contains(v)){
                continue;
            }


            Set<String> component = new TreeSet<>();

            collect(v,reversedGraph,visited,component);

            StringBuilder str = new StringBuilder();
            for (String s : component){
                str.append(s);
            }


            result.add(str.toString());


        }
        return result;
    }























}
