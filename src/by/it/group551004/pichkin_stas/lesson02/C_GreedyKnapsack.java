package by.it.group551004.pichkin_stas.lesson02;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {

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
        public int compareTo(Item other) {
            // Сортируем по убыванию удельной стоимости (cost/weight)
            double thisRatio = (double) this.cost / this.weight;
            double otherRatio = (double) other.cost / other.weight;

            if (thisRatio > otherRatio) {
                return -1;
            } else if (thisRatio < otherRatio) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    double calc(File source) throws FileNotFoundException {
        Scanner input = new Scanner(source);
        int n = input.nextInt();      // количество предметов
        int W = input.nextInt();      // вместимость рюкзака

        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        // Сортируем предметы по убыванию удельной стоимости
        Arrays.sort(items);

        double result = 0;
        int remainingWeight = W;

        for (Item item : items) {
            if (remainingWeight >= item.weight) {
                // Берем весь предмет
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                break; // Рюкзак заполнен
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir");
        File file = new File(root + "/src/by/it/a_khmelev/lesson02/greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(file);
        System.out.println("costFinal=" + costFinal);
    }
}