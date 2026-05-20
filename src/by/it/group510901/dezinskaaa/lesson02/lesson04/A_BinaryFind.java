package by.it.group510901.dezinskaaa.lesson02.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
в первой строке источника данных даны:
        - целое число 1<=n<=100000 (размер массива)
        - сам массив A[1...n] из n различных натуральных чисел,
          не превышающих 10E9, в порядке возрастания,
во второй строке
        - целое число 1<=k<=10000 (сколько чисел нужно найти)
        - k натуральных чисел b1,...,bk не превышающих 10E9 (сами числа)
для каждого i от 1 до kk необходимо вывести индекс 1<=j<=n,
для которого A[j]=bi, или -1, если такого j нет.

        sample input:
        5 1 5 8 12 13
        5 8 1 23 1 11

        sample output:
        3 1 -1 1 -1

(!) обратите внимание на смещение начала индекса массивов java относительно условий задачи
*/

public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt"); // получаем поток ввода из файла dataa.txt
        A_BinaryFind instance = new A_BinaryFind(); // создаём экземпляр текущего класса
        //long startTime = System.currentTimeMillis();
        int[] result = instance.findIndex(stream);// вызываем метод поиска индексов, получаем массив результатов
        //long finishTime = System.currentTimeMillis();
        for (int index : result) { // выводим каждый найденный индекс через пробел
            System.out.print(index + " ");
        }
    }

    public int[] findIndex(InputStream stream) throws FileNotFoundException {
        // подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     начало задачи     !!!!!!!!!!!!!!!!!!!!!!!!!


        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 1; i <= n; i++) {
            a[i - 1] = scanner.nextInt();
        }

        // размер массива индексов
        int k = scanner.nextInt();
        int[] result = new int[k];
        for (int i = 0; i < k; i++) { // заполняем массив: читаем следующие n чисел из первой строки
            int value = scanner.nextInt();

            int left = 0;
            int right = n - 1;
            int idx = -1;


            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (a[mid] == value) {
                    idx = mid + 1;
                    break;
                } else if (a[mid] < value) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            result[i] = idx;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     конец задачи     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}