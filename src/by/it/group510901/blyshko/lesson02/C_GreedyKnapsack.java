package by.it.group510901.blyshko.lesson02;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      
        int W = input.nextInt();      
        Item[] items = new Item[n];   
        for (int i = 0; i < n; i++) { 
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        
        
        
        double result = 0;
        
        
        

        
        Arrays.sort(items);
        int currentWeight = 0;
        for (Item item : items) {
            if (currentWeight == W) {
                break;
            }
            int availableWeight = W - currentWeight;
            if (item.weight <= availableWeight) {
                result += item.cost;
                currentWeight += item.weight;
            } else {
                result += (double) item.cost * availableWeight / item.weight;
                currentWeight = W;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                   "cost=" + cost +
                   ", weight=" + weight +
                   '}';
        }

        @Override
        public int compareTo(Item o) {
            
            return Double.compare(
                    (double) o.cost / o.weight,
                    (double) cost / weight
            );
        }
    }
}
