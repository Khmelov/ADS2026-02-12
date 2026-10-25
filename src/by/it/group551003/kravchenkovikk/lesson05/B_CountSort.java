package by.it.group551003.kravchenkovikk.lesson05;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Первая строка содержит число 1<=n<=10000, вторая - n натуральных чисел, не превышающих 10.
Выведите упорядоченную по неубыванию последовательность этих чисел.

При сортировке реализуйте метод со сложностью O(n)

Пример: https://karussell.wordpress.com/2010/03/01/fast-integer-sorting-algorithm-on/
Вольный перевод: http://programador.ru/sorting-positive-int-linear-time/
*/

public class B_CountSort {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_CountSort.class.getResourceAsStream("dataB.txt");
        B_CountSort instance = new B_CountSort();
        int[] result = instance.countSort(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] countSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        int[] points = new int[n];

        //читаем точки
        for (int i = 0; i < n; i++) {
            points[i] = scanner.nextInt();
        }
        //тут реализуйте логику задачи с применением сортировки подсчетом

        int numOfElems = 10 - 1 + 1;
        int[] numbers = new int[numOfElems];
        for (int i = 0; i < numOfElems; i++) {
            numbers[i] = 0;
        }
        for(int i = 0; i < n; i++){
            numbers[points[i]-1]++;
        }
        int j = 0;
        for (int i = 0; i < n; i++) {
            while(numbers[j] == 0) j++;
            points[i] = j+1;
            numbers[j]--;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return points;
    }

}
