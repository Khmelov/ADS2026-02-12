package by.it.group510902.paleychik.lesson07;

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
    Итерационно вычислить расстояние редактирования двух данных непустых строк

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

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Получаем длины строк
        int lenOne = one.length();   // длина первой строки
        int lenTwo = two.length();   // длина второй строки

        // Создаем таблицу DP размером (lenOne+1) x (lenTwo+1)
        // matrix[i][j] = минимальное расстояние редактирования
        // для первых i символов строки one
        // и первых j символов строки two
        int[][] matrix = new int[lenOne + 1][lenTwo + 1];

        // Инициализация первого столбца (j = 0)
        // Чтобы из строки one получить пустую строку,
        // нужно удалить все символы
        for (int i = 0; i <= lenOne; i++) {
            matrix[i][0] = i;
        }

        // Инициализация первой строки (i = 0)
        // Чтобы из пустой строки получить строку two,
        // нужно вставить все символы
        for (int j = 0; j <= lenTwo; j++) {
            matrix[0][j] = j;
        }

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= lenOne; i++) {

            for (int j = 1; j <= lenTwo; j++) {

                // Проверяем совпадение символов
                int changeCost;

                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    changeCost = 0;
                } else {
                    changeCost = 1;
                }

                // Возможные операции

                // 1. Удаление символа
                int remove = matrix[i - 1][j] + 1;

                // 2. Вставка символа
                int add = matrix[i][j - 1] + 1;

                // 3. Замена символа или совпадение
                int change = matrix[i - 1][j - 1] + changeCost;

                // Выбираем минимальную стоимость
                matrix[i][j] = Math.min(remove, Math.min(add, change));
            }
        }

        // Ответ находится в последней ячейке таблицы
        int answer = matrix[lenOne][lenTwo];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return answer;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);

        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}