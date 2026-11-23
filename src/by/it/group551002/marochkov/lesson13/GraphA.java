package by.it.group551002.marochkov.lesson13;


import java.util.*;

public class GraphA {


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



    private static String topologicalSort(Map<String, List<String>> graph) {

        Map<String,Integer> inDegree =  new HashMap<>();
        for(String v : graph.keySet()){
            inDegree.put(v,0);
        }

        for(List<String> neighbours : graph.values()){
            for(String to : neighbours){
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        PriorityQueue<String> ready = new PriorityQueue<>();
        for(Map.Entry<String,Integer> e  : inDegree.entrySet()){
            if(e.getValue() == 0){
                ready.add(e.getKey());
            }
        }

        StringBuilder result = new StringBuilder();
        while(!ready.isEmpty()){
            String v = ready.poll();
            result.append(v).append(" ");

            for(String to : graph.get(v)){
                int d = inDegree.get(to) - 1;
                inDegree.put(to, d);
                if(d == 0){
                    ready.add(to);
                }
            }
        }
        return result.toString().trim();

    }

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        Map<String, List<String>> graph = parse(line);
        System.out.println(topologicalSort(graph));

    }

}
