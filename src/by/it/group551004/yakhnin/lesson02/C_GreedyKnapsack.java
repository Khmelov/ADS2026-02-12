package by.it.group551004.yakhnin.lesson02;

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
        int n = input.nextInt();      //сколько предметов в файле
        int W = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        //покажем предметы
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        //тут необходимо реализовать решение задачи
        //итогом является максимально воможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        double result = 0;

        // Сортируем предметы по убыванию стоимости за единицу веса
        Arrays.sort(items);

        int currentWeight = 0;

        // Жадный алгоритм: берем предметы с наибольшей удельной стоимостью
        for (Item item : items) {
            if (currentWeight + item.weight <= W) {
                // Предмет помещается целиком
                result += item.cost;
                currentWeight += item.weight;
                System.out.println("Взят целиком: " + item + ", удельная стоимость: " + (double)item.cost/item.weight);
            } else {
                // Берем только часть предмета
                int remainingSpace = W - currentWeight;
                double partCost = (double)item.cost * remainingSpace / item.weight;
                result += partCost;
                System.out.printf("Взята часть предмета: %s, вес %.1f, стоимость %.1f\n",
                        item, (double)remainingSpace, partCost);
                break; // Рюкзак заполнен
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
            // Сортировка по убыванию стоимости за единицу веса
            double thisUnitCost = (double) this.cost / this.weight;
            double otherUnitCost = (double) o.cost / o.weight;

            if (thisUnitCost > otherUnitCost) {
                return -1; // this должен идти раньше (по убыванию)
            } else if (thisUnitCost < otherUnitCost) {
                return 1;  // this должен идти позже
            } else {
                return 0;  // равны
            }
        }
    }
}