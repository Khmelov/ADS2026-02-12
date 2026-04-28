package by.it.group510902.gordeev.lesson02;

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

        int remainingCapacity = W;

        for (Item item : items) {
            if (remainingCapacity <= 0) break;

            if (item.weight <= remainingCapacity) {

                result += item.cost;
                remainingCapacity -= item.weight;
            } else {

                double fraction = (double) remainingCapacity / item.weight;
                result += item.cost * fraction;
                remainingCapacity = 0;
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


            long left = (long) this.cost * o.weight;
            long right = (long) o.cost * this.weight;

            return Long.compare(right, left);
        }
    }
}