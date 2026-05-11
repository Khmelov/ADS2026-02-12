package by.it.group510901.jalilova.lesson02;
/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов
    100 50
    120 30
    100 50
Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е. алгоритм будет жадным)
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
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

        // РЕШЕНИЕ ЗАДАЧИ
        double result = 0;
        //тут реализуйте алгоритм сбора рюкзака
        // Используем сортировку пузырьком (своя сортировка)
        for (int i = 0; i < items.length - 1; i++) {
            for (int j = 0; j < items.length - i - 1; j++) {
                // Сравниваем удельную стоимость (cost/weight)
                double valuePerWeightJ = (double) items[j].cost / items[j].weight;
                double valuePerWeightJ1 = (double) items[j + 1].cost / items[j + 1].weight;

                if (valuePerWeightJ < valuePerWeightJ1) {
                    // Меняем местами
                    Item temp = items[j];
                    items[j] = items[j + 1];
                    items[j + 1] = temp;
                }
            }
        }

        // 2. ЖАДНЫЙ НАБОР РЮКЗАКА
        int remainingWeight = W; // оставшийся вес рюкзака

        for (Item item : items) {
            if (remainingWeight <= 0) break; // рюкзак уже полный

            if (item.weight <= remainingWeight) {
                // Предмет помещается целиком
                result += item.cost;
                remainingWeight -= item.weight;
                System.out.printf("Берем целиком %s. Осталось места: %d\n", item, remainingWeight);
            } else {
                // Предмет не помещается целиком - берем часть
                double fraction = (double) remainingWeight / item.weight;
                result += item.cost * fraction;
                System.out.printf("Берем %.2f от %s. Стоимость: %.2f\n", fraction, item, item.cost * fraction);
                remainingWeight = 0;
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
            //тут может быть ваш компаратор


            return 0;
        }
    }
}