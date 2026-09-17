package by.it.group551001.belozorchik.lesson07;

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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int result = 0;
        int n = one.length();
        int m = two.length();

        //создаю матрицу расстояний
        //d[i][j] - расстояние между префиксом one длины i и префиксом two длины j
        int [][] d = new int [n+1][m+1];

        //пустая стр превращ в другую путём вставок
        for (int i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        //заполенеие матрицы
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                //если совпадают - оставляем
                int value = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                d[i][j] = Math.min ( Math.min(d[i - 1][j] + 1,      // сверху - удаление
                                            d[i][j - 1] + 1),       // слева - вставка
                                            d[i - 1][j - 1] + value // диагональ - замена
                );
            }
        }
        result = d[n][m];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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
