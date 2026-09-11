package by.it.group551002.rybik.lesson07;

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

        int[][] mtx = new int[one.length()+1][two.length()+1];

        for(int i = 0; i < one.length()+1; i++){
            mtx[i][0] = i;
        }
        for(int i = 0; i < two.length()+1; i++){
            mtx[0][i] = i;
        }

        for(int i = 1; i < one.length() + 1; i++){
            for (int j = 1; j < two.length() + 1; j++){
                int m = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;
                mtx[i][j] = Math.min(Math.min(mtx[i][j-1]+1, mtx[i-1][j]+1), mtx[i-1][j-1]+m);
            }
        }

        String result = "";

        int i = one.length(), j = two.length();

        while (i != 0 && j != 0) {
            int m = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

            if (mtx[i][j] == mtx[i - 1][j - 1] + m) {
                if (m == 0) {
                    result = "#," + result;
                } else {
                    result = "~" + two.charAt(j - 1) + "," + result;
                }
                i--;
                j--;
            } else if (mtx[i][j] == mtx[i - 1][j] + 1) {
                result = "-" + one.charAt(i - 1) + "," + result;
                i--;
            } else {
                result = "+" + two.charAt(j - 1) + "," + result;
                j--;
            }
        }

        while (i != 0) {
            result = "-" + one.charAt(i - 1) + "," + result;
            i--;
        }

        while (j != 0) {
            result = "+" + two.charAt(j - 1) + "," + result;
            j--;
        }



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