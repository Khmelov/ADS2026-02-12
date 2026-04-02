package by.it.group551001.anenko.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    private void quickSort(Item[] items, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(items, left, right);
            quickSort(items, left, pivotIndex - 1);
            quickSort(items, pivotIndex + 1, right);
        }
    }

    private int partition(Item[] items, int left, int right) {
        double pivot = (double)items[right].cost / items[right].weight;
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if ((double)items[j].cost / items[j].weight >= pivot) {
                i++;
                Item temp = items[i];
                items[i] = items[j];
                items[j] = temp;
            }
        }

        Item temp = items[i + 1];
        items[i + 1] = items[right];
        items[right] = temp;

        return i + 1;
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();
        int W = input.nextInt();
        Item[] items = new Item[n];

        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        System.out.println("Исходные предметы:");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        quickSort(items, 0, items.length - 1);

        System.out.println("\nПредметы после сортировки (по убыванию цены за кг):");
        for (Item item : items) {
            System.out.printf("%s (цена за кг: %.2f)\n", item, (double)item.cost / item.weight);
        }

        double result = 0;
        int remainingWeight = W;

        for (Item item : items) {
            if (remainingWeight <= 0) break;

            if (item.weight <= remainingWeight) {
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.printf("Взяли весь предмет: %s, осталось места: %d\n", item, remainingWeight);
            } else {
                double fraction = (double)remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Взяли %.2f предмета: %s\n", fraction, item);
                remainingWeight = 0;
            }
        }

        System.out.printf("\nУдалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Item{cost=" + cost + ", weight=" + weight + "}";
        }
    }
}