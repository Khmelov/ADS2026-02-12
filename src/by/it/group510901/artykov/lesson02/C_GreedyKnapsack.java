package by.it.group510901.artykov.lesson02;

/*
 * Задача о непрерывном рюкзаке.
 *
 * Дано:
 * - вместимость рюкзака
 * - набор предметов
 *
 * Каждый предмет имеет:
 * - стоимость
 * - вес
 *
 * Предметы можно делить на части,
 * поэтому используется жадный алгоритм.
 *
 * Идея:
 * Сначала берем предметы
 * с максимальной стоимостью за 1 кг.
 */

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();

        InputStream inputStream =
                C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");

        double costFinal = new C_GreedyKnapsack().calc(inputStream);

        long finishTime = System.currentTimeMillis();

        System.out.printf(
                "Общая стоимость %f (время %d)",
                costFinal,
                finishTime - startTime
        );
    }

    /*
     * Метод вычисляет максимальную стоимость,
     * которую можно поместить в рюкзак.
     */
    double calc(InputStream inputStream) throws FileNotFoundException {

        Scanner input = new Scanner(inputStream);

        // Количество предметов
        int n = input.nextInt();

        // Вместимость рюкзака
        int W = input.nextInt();

        // Массив предметов
        Item[] items = new Item[n];

        // Чтение предметов из файла
        for (int i = 0; i < n; i++) {

            items[i] = new Item(
                    input.nextInt(),
                    input.nextInt()
            );
        }

        // Вывод предметов
        for (Item item : items) {

            System.out.println(item);
        }

        System.out.printf(
                "Всего предметов: %d. Рюкзак вмещает %d кг.\n",
                n,
                W
        );

        /*
         * Сортировка предметов:
         * по убыванию стоимости за 1 кг
         */
        Arrays.sort(items);

        // Итоговая стоимость
        double result = 0;

        // Остаток свободного места
        int currentWeight = W;

        // Проходим по всем предметам
        for (Item item : items) {

            // Если рюкзак уже заполнен
            if (currentWeight == 0) {
                break;
            }

            /*
             * Берём:
             * либо весь предмет,
             * либо только часть
             */
            int takeWeight = Math.min(currentWeight, item.weight);

            /*
             * Добавляем стоимость
             * пропорционально взятому весу
             */
            result += takeWeight * item.valuePerKg();

            // Уменьшаем свободное место
            currentWeight -= takeWeight;
        }

        System.out.printf(
                "Удалось собрать рюкзак на сумму %f\n",
                result
        );

        return result;
    }

    /*
     * Класс предмета.
     *
     * Cost - стоимость
     * weight - вес
     */
    private static class Item implements Comparable<Item> {

        int cost;
        int weight;

        Item(int cost, int weight) {

            this.cost = cost;
            this.weight = weight;
        }

        /*
         * Стоимость за 1 кг.
         */
        double valuePerKg() {

            return (double) cost / weight;
        }

        /*
         * Красивый вывод предмета.
         */
        @Override
        public String toString() {

            return "Item{" +
                    "cost=" + cost +
                    ", weight=" + weight +
                    '}';
        }

        /*
         * Сортировка по убыванию
         * стоимости за килограмм.
         */
        @Override
        public int compareTo(Item o) {

            return Double.compare(
                    o.valuePerKg(),
                    this.valuePerKg()
            );
        }
    }
}