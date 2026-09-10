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
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        String result = "";
        String new_one = "#" + one;
        String new_two = "#" + two;

        int rows = new_two.length();
        int cols = new_one.length();

        int[][] lev_matrix = new int[rows][cols];
        lev_matrix[0][0] = 0;

        for (int i = 1; i < rows; i++)
            lev_matrix[i][0] = lev_matrix[i - 1][0] + 1;
        for (int j = 1; j < cols; j++)
            lev_matrix[0][j] = lev_matrix[0][j - 1] + 1;

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (new_one.charAt(j) == new_two.charAt(i)) {
                    lev_matrix[i][j] = lev_matrix[i - 1][j - 1];
                } else {
                    int prev_col = lev_matrix[i][j - 1];
                    int prev_diag = lev_matrix[i - 1][j - 1];
                    int prev_row = lev_matrix[i - 1][j];
                    int min = Math.min(prev_col, Math.min(prev_diag,prev_row));
                    lev_matrix[i][j] = 1 + min;
                }
            }
        }

        int i = rows - 1;
        int j = cols - 1;

        StringBuilder str = new StringBuilder();

        while (i > 0 || j > 0) {
            if (i == 0) {
                str.insert(0,"-" + new_one.charAt(j) + ",");
                j--;
            }
            else if (j == 0) {
                str.insert(0,"+" + new_two.charAt(i) + ",");
                i--;
            } else if (new_one.charAt(j) == new_two.charAt(i)) {
                str.insert(0,"#" + ",");
                i--;
                j--;
            } else {
                int prev_col = lev_matrix[i][j - 1];
                int prev_diag = lev_matrix[i - 1][j - 1];
                int prev_row = lev_matrix[i - 1][j];
                int min = Math.min(prev_col, Math.min(prev_diag,prev_row));
                if (min == prev_diag) {
                    str.insert(0,"~" + new_one.charAt(j) + ",");
                    i--;
                    j--;
                } else if (min == prev_row) {
                    str.insert(0,"+" + new_two.charAt(i) + ",");
                    i--;
                } else if (min == prev_col) {
                    str.insert(0,"-" + new_one.charAt(j) + ",");
                    j--;
                }
            }
        }
        result = str.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}