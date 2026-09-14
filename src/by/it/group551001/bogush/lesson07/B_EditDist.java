package by.it.group551001.bogush.lesson07;

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
        int rows = two.length() + 1;
        int cols = one.length() + 1;
        int[][] lev_matrix = new int[rows][cols];
        lev_matrix[0][0] = 0;
        for (int i = 1; i < cols; i++)
            lev_matrix[0][i] = lev_matrix[0][i - 1] + 1;
        for (int j = 1; j < rows; j++)
            lev_matrix[j][0] = lev_matrix[j - 1][0] + 1;
        String new_one = "#" + one;
        String new_two = "#" + two;

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (new_one.charAt(j) == new_two.charAt(i))
                    lev_matrix[i][j] = lev_matrix[i - 1][j - 1];
                else {
                    lev_matrix[i][j] = 1 + Math.min(lev_matrix[i][j - 1],Math.min(lev_matrix[i - 1][j - 1],lev_matrix[i - 1][j]));
                }
            }
        }

        int result = lev_matrix[rows - 1][cols - 1];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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