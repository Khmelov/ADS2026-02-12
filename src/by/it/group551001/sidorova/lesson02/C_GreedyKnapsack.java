package by.it.group551001.sidorova.lesson02;

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
        int remainingWeight = W;

        // Сортируем предметы по убыванию стоимости за единицу веса
        Arrays.sort(items, (i1, i2) -> {
            double ratio1 = (double) i1.cost / i1.weight;
            double ratio2 = (double) i2.cost / i2.weight;
            if (ratio1 > ratio2) return -1;
            if (ratio1 < ratio2) return 1;
            return 0;
        });

        // Жадный алгоритм для непрерывного рюкзака
        for (Item item : items) {
            if (remainingWeight == 0) break;

            if (item.weight <= remainingWeight) {
                // Берем предмет целиком
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.println("Взят целиком: " + item + " Осталось места: " + remainingWeight);
            } else {
                // Берем часть предмета
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Взят фрагмент %.2f от %s. Стоимость фрагмента: %.2f\n",
                        fraction, item, item.cost * fraction);
                remainingWeight = 0;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
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
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    " (price/kg=" + String.format("%.2f", (double)cost/weight) +
                    ")}";
        }
    }
}