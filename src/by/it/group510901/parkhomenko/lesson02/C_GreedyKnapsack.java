package by.it.group510901.parkhomenko.lesson02;
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

        double result = 0;

        // 1. Сортируем массив предметов.
        // Логика сортировки (по убыванию цены за 1 кг) описана в методе compareTo класса Item.
        Arrays.sort(items);

        // 2. Начинаем заполнять рюкзак
        int currentWeight = 0; // Текущий занятый вес в рюкзаке

        for (Item item : items) {
            // Если рюкзак уже полон, прерываем цикл
            if (currentWeight == W) {
                break;
            }

            // Проверяем, влезет ли предмет целиком
            if (currentWeight + item.weight <= W) {
                // Забираем предмет целиком
                result += item.cost;
                currentWeight += item.weight;
            } else {
                // Предмет не влезает целиком, берем только нужную часть
                int remain = W - currentWeight; // Сколько еще места осталось
                // Высчитываем стоимость кусочка (приводим к double, чтобы не потерять точность при делении)
                result += (double) item.cost * remain / item.weight;
                currentWeight = W; // Рюкзак заполнен
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
            // Вычисляем удельную стоимость (цену за единицу веса) текущего предмета
            double pricePerWeightThis = (double) this.cost / this.weight;
            // Вычисляем удельную стоимость предмета 'o', с которым сравниваем
            double pricePerWeightOther = (double) o.cost / o.weight;

            // Нам нужно отсортировать предметы по УБЫВАНИЮ их удельной стоимости.
            // Поэтому мы сравниваем 'other' с 'this', а не наоборот.
            return Double.compare(pricePerWeightOther, pricePerWeightThis);
        }
    }
}