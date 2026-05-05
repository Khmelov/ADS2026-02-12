package by.it.group551001.telipko.lesson02;
/*
Даны
1) объем рюкзака 4
2) число возможных предметов 60
3) сам набор предметов

Все это указано в файле (by/it/a_khmelev/lesson02/greedyKnapsack.txt)

Необходимо собрать наиболее дорогой вариант рюкзака для этого объема
Предметы можно резать на кусочки (т.е. Алгоритм будет жадным)
 */

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

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      //сколько предметов в файле
        int totalWeight = input.nextInt();      //какой вес у рюкзака
        Item[] items = new Item[n];   //получим список предметов
        for (int i = 0; i < n; i++) { //создавая каждый конструктором
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, totalWeight);

        bubbleSort(items);

        double result = 0;

        for (int index = 0; index < items.length - 1; index++) {
            Item item = items[index];
            if (totalWeight >= item.weight) {
                result += item.cost;
                totalWeight -= item.weight;
            } else {
                result += totalWeight * (double) (items[index + 1].cost / items[index + 1].weight);
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
            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    '}';
        }

        @Override
        public int compareTo(Item o) {
            if (this.weight < o.weight) return 1;
            else if (this.weight > o.weight) return -1;
            else return Integer.compare(this.cost, o.cost);
        }
    }

    private static void bubbleSort(Item[] items) {
        Item temp;
        boolean flag = true;
        int right = items.length - 1;

        while (flag) {
            flag = false;
            int left = 1;

            while (left <= right) {
                if (items[left - 1].compareTo(items[left]) < 0) {
                    flag = true;
                    while (items[left - 1].compareTo(items[left]) < 0) {
                        temp = items[left - 1];
                        items[left - 1] = items[left];
                        items[left] = temp;

                    }
                }
                left++;
            }

            right--;
        }
    }
}