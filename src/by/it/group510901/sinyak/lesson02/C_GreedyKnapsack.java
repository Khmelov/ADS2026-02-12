package by.it.group510901.sinyak.lesson02;
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
        //тут реализуйте алгоритм сбора рюкзака
        //будет особенно хорошо, если с собственной сортировкой
        //кроме того, можете описать свой компаратор в классе Item

        //ваше решение.

        sort(items, 0, n);

        int currentW = 0;

        for (Item item:items) {
            if (item.weight <= W-currentW) { //если предмет вмещается целиком
                result += item.cost; //добавляем цену товара к общей стоимости
                currentW += item.weight;
            }
            else { //если целиком не влезат(по частям)
                result += item.coeff() * (W-currentW); //добавляем к стоимости ровно столько сколько влезает
                currentW = W;
                break;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    static Item[] sort(Item[] items, int left, int right) {

        if (left >= right - 1) { return null;} //базовый случай

        int basepoint = (right+left)/2;
        Item base = items[basepoint];

        int pointer = left;

        for (int i = left; i < right; i++){
            if (items[i].compareTo(base) < 0) {
                Item temp = items[pointer];
                items[pointer] = items[i];
                items[i] = temp;
                if (pointer == basepoint) {basepoint = i;}
                pointer++;
            }
        }
        Item temp = items[pointer];
        items[pointer] = items[basepoint];
        items[basepoint] = temp;

        sort(items, left, pointer);
        sort(items, pointer+1, right);
        return items;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        double coeff() {
            return (double)cost/weight;
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
            return Double.compare(o.coeff(), this.coeff());
        }
    }
}