package by.it.group510901.blyshko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;



































public class A_Huffman {

    
    static private final Map<Character, String> codes = new TreeMap<>();
    private int nodeOrder;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = A_Huffman.class.getResourceAsStream("dataA.txt");
        A_Huffman instance = new A_Huffman();
        long startTime = System.currentTimeMillis();
        String result = instance.encode(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("%d %d\n", codes.size(), result.length());
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.printf("%s: %s\n", entry.getKey(), entry.getValue());
        }
        System.out.println(result);
    }

    
    String encode(InputStream inputStream) throws FileNotFoundException {
        
        Scanner scanner = new Scanner(inputStream);
        String s = scanner.next();
        codes.clear();
        nodeOrder = 0;

        
        

        Map<Character, Integer> count = new HashMap<>();
        
        
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : new TreeMap<>(count).entrySet()) {
            priorityQueue.add(new LeafNode(entry.getValue(), entry.getKey()));
        }

        
        
        
        
        while (priorityQueue.size() > 1) {
            Node left = priorityQueue.poll();
            Node right = priorityQueue.poll();
            priorityQueue.add(new InternalNode(left, right));
        }

        
        
        StringBuilder sb = new StringBuilder();
        Node root = priorityQueue.poll();
        root.fillCodes("");
        for (char c : s.toCharArray()) {
            sb.append(codes.get(c));
        }

        return sb.toString();
        
        
    }

    
    abstract class Node implements Comparable<Node> {
        
        
        
        private final int frequence; 
        private final int order;

        
        private Node(int frequence) {
            this.frequence = frequence;
            this.order = nodeOrder++;
        }

        
        
        abstract void fillCodes(String code);

        
        
        @Override
        public int compareTo(Node o) {
            int compare = Integer.compare(frequence, o.frequence);
            if (compare != 0) {
                return compare;
            }
            return Integer.compare(order, o.order);
        }
    }

    
    
    private class InternalNode extends Node {
        
        Node left;  
        Node right; 

        
        
        InternalNode(Node left, Node right) {
            super(left.frequence + right.frequence);
            this.left = left;
            this.right = right;
        }

        @Override
        void fillCodes(String code) {
            left.fillCodes(code + "0");
            right.fillCodes(code + "1");
        }

    }
    

    
    
    private class LeafNode extends Node {
        
        char symbol; 

        LeafNode(int frequence, char symbol) {
            super(frequence);
            this.symbol = symbol;
        }

        @Override
        void fillCodes(String code) {
            
            
            if (code.isEmpty()) {
                code = "0";
            }
            codes.put(this.symbol, code);
        }
    }

}
