package by.it.group551004.kozhedub.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/


public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int mergeSort(int[] arr,int n,int count) {
        {
            if (n==1) {return count;}
            int mid = n / 2;
            int[] l = new int[mid];
            int[] r = new int[n - mid];
            for (int i = 0; i < mid; i++) {
                l[i] = arr[i];
            }
            for (int i = mid; i < n - mid; i++) {
                r[i] = arr[i];
            }
            mergeSort(l, mid, count);
            mergeSort(r, n - mid, count);
            count=merge(arr, l, r, mid, n - mid, count);
        }
        return count;
    }

    int merge(int[]arr,int[]l,int[]r,int left,int right,int count){
        {
            int i=0; int j=0;int k=0;
            while ((i<left) && (j<right)){
                if (l[i]<=r[j]) {
                    arr[k++]=l[i++];
                }
                else {
                    arr[k++]=r[j++];
                    count=left-i+1;};
            }
            while (i<left) {
                arr[k++]=l[i++];
            }
            while (j<right) {
                arr[k++]=r[j++];
            }
        }
        return count;
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int result = 0;
        int count=0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        result=mergeSort(a,n,count);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}
