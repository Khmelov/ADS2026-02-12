package by.it.group551003.tereshko.lesson02;
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
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    void sortItems(Item[] items, int low, int high) {
        if (low < high) {
            int pivotIndex;
            pivotIndex = partition(items, low, high);
            sortItems(items, low, pivotIndex - 1);
            sortItems(items, pivotIndex + 1, high);
        }
    }

    int partition(Item[] items, int low, int high) {
        int pivot, left;
        Item temp;

        pivot = items[high].cost / items[high].weight;
        left = low - 1;

        for (int i = low; i < high; i++) {
            if (items[i].cost / items[i].weight >= pivot) {
                left++;
                temp = items[left];
                items[left] = items[i];
                items[i] = temp;
            }
        }
        temp = items[left + 1];
        items[left + 1] = items[high];
        items[high] = temp;
        return left + 1;
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
        //итогом является максимально возможная стоимость вещей в рюкзаке
        //вещи можно резать на кусочки (непрерывный рюкзак)
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item
        //ваше решение.
        double sum = 0;
        int remainder, i;
        remainder = W;
        i = 0;
        sortItems(items, 0, items.length - 1);
        while (remainder > 0 && i < items.length) {
            if (remainder <= items[i].weight) {
                sum += items[i].cost / items[i].weight * remainder;
                remainder = 0;
            } else {
                sum += items[i].cost;
                remainder -= items[i].weight;
            }
            i++;
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", sum);
        return sum;
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