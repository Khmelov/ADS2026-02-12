package by.it.group551001.maxim_belyak.lesson02;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        // Убедитесь, что файл greedyKnapsack.txt находится в той же папке, что и класс
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)\n", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // количество предметов
        int W = input.nextInt();      // вместимость рюкзака
        Item[] items = new Item[n];

        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        // 1. Сортируем предметы по удельной стоимости (цена/вес) в порядке убывания
        Arrays.sort(items);

        double result = 0;
        int currentWeight = 0;

        // 2. Жадный алгоритм сбора
        for (Item item : items) {
            // Если предмет влезает целиком
            if (currentWeight + item.weight <= W) {
                result += item.cost;
                currentWeight += item.weight;
            } else {
                // Если не влезает целиком, берем только часть (режем его)
                int remainingCapacity = W - currentWeight;
                result += (double) item.cost * remainingCapacity / item.weight;
                currentWeight = W; // Рюкзак полон
                break;
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
            return "Item{cost=" + cost + ", weight=" + weight + "}";
        }

        @Override
        public int compareTo(Item o) {
            // Сравнение по удельной стоимости: (this.cost / this.weight) vs (o.cost / o.weight)
            // Чтобы избежать потери точности при делении int, используем перекрестное умножение:
            // (double)this.cost / this.weight > (double)o.cost / o.weight

            double r1 = (double) this.cost / this.weight;
            double r2 = (double) o.cost / o.weight;

            // Сортировка по убыванию (от самых дорогих за кг к самым дешевым)
            return Double.compare(r2, r1);
        }
    }
}