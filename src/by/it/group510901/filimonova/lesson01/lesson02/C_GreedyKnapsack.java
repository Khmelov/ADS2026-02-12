package by.it.group510901.filimonova.lesson01.lesson02;

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

        double remainingCapacity = W;

        // Жадный алгоритм: берем предметы с наибольшей ценностью за кг
        for (Item item : items) {
            if (remainingCapacity <= 0) {
                break;
            }

            if (item.weight <= remainingCapacity) {
                // Берем весь предмет
                result += item.cost;
                remainingCapacity -= item.weight;
                System.out.printf("Берем полностью предмет: %s, осталось места: %.2f\n", item, remainingCapacity);
            } else {
                // Берем часть предмета
                double fraction = remainingCapacity / item.weight;
                result += item.cost * fraction;
                System.out.printf("Берем %.2f часть предмета %s, стоимость части: %.2f\n",
                        fraction, item, item.cost * fraction);
                remainingCapacity = 0;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;
        double valuePerWeight; // ценность за единицу веса

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
            this.valuePerWeight = (double) cost / weight;
        }

        @Override
        public String toString() {
            return String.format("Item{cost=%d, weight=%d, valuePerWeight=%.2f}",
                    cost, weight, valuePerWeight);
        }

        @Override
        public int compareTo(Item o) {
            // Сортируем по убыванию ценности за единицу веса
            if (this.valuePerWeight > o.valuePerWeight) {
                return -1; // этот предмет лучше (больше ценность)
            } else if (this.valuePerWeight < o.valuePerWeight) {
                return 1;  // другой предмет лучше
            } else {
                return 0;  // равны
            }
        }
    }
}

/*1. Для каждого предмета вычисляем ценность за кг: valuePerWeight = cost / weight
2. Сортируем предметы по убыванию ценности за кг
3. Берем предметы целиком, пока они помещаются
4. Когда очередной предмет не помещается целиком, берем часть, заполняющую рюкзак до конца*/