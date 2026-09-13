package by.it.group551002.loiko.lesson02;
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
        // Сортируем предметы по удельной стоимости (cost/weight) в порядке убывания
        // Используем собственный компаратор через лямбда-выражение
        java.util.Arrays.sort(items, (item1, item2) -> {
            double ratio1 = (double) item1.cost / item1.weight;
            double ratio2 = (double) item2.cost / item2.weight;
            return Double.compare(ratio2, ratio1); // убывание: от большего к меньшему
        });

        double totalWeight = 0;  // сколько веса уже набрали в рюкзаке

        // Проходим по всем предметам от самых "дорогих" к дешёвым
        for (Item item : items) {
            if (totalWeight + item.weight <= W) {
                // Предмет помещается целиком
                result += item.cost;
                totalWeight += item.weight;
            } else {
                // Предмет помещается только частично
                double remainingWeight = W - totalWeight; // сколько свободного места осталось
                double fraction = remainingWeight / item.weight; // какую часть предмета можем взять
                result += item.cost * fraction; // добавляем стоимость части предмета
                totalWeight = W; // рюкзак полностью заполнен
                break; // выходим из цикла, так как дальше ничего не поместится
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
            // Сравниваем по удельной стоимости (cost/weight) в порядке убывания
            double ratioThis = (double) this.cost / this.weight;
            double ratioOther = (double) o.cost / o.weight;

            // Возвращаем отрицательное, если this должен идти раньше (выше стоимость за кг)
            if (ratioThis > ratioOther) {
                return -1;  // this дороже → должен быть раньше
            } else if (ratioThis < ratioOther) {
                return 1;   // other дороже → this позже
            } else {
                return 0;   // удельная стоимость равна
            }
        }
    }
}