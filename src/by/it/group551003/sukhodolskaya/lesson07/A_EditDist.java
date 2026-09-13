package by.it.group551003.sukhodolskaya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {

    //матрица для мемоизации
    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        //инициализируем таблицу мемоизации значениями -1
        memo = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        int result = calculate(one, two, n, m);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    //рекурсивная функция с мемоизацией
    private int calculate(String s1, String s2, int i, int j) {
        //базовые случаи
        //если одна из строк пуста
        if (i == 0) return j;
        if (j == 0) return i;

        //если значение уже вычислено, возвращаем его
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        int res;
        //если символы совпадают, стоимость операции 0
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            res = calculate(s1, s2, i - 1, j - 1);
        } else {
            //иначе выбираем минимум из трех операций:
            res = 1 + Math.min(
                    calculate(s1, s2, i - 1, j),    //удаление
                    Math.min(
                            calculate(s1, s2, i, j - 1),    //вставка
                            calculate(s1, s2, i - 1, j - 1) //замена
                    )
            );
        }

        return memo[i][j] = res;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
