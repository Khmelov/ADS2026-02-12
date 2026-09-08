package by.it.group510902.mikhnovets.lesson07;

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
        int[][] n = new int[one.length()+2][two.length()+2];
        for(int i = 2; i < one.length() + 2; i++) {
            n[i][0] = one.charAt(i - 2);
        }
        for(int j = 2; j < two.length() + 2; j++) {
            n[0][j] = two.charAt(j - 2);
        }
        for (int j = 1; j < two.length() + 2; j++) {
            n[1][j] = j - 1;
        }
        for (int i = 1; i < one.length() + 2; i++) {
            n[i][1] = i - 1;
        }
        for(int i = 2; i < one.length() + 2; i++) {
            for(int j = 2; j < two.length() + 2; j++) {
                if(n[0][j] == n[i][0]) {
                    n[i][j] = Math.min((n[i][j -1] + 1), Math.min((n[i-1][j] + 1), n[i-1][j-1]));
                } else {
                    n[i][j] = Math.min((n[i][j -1] + 1), Math.min((n[i-1][j] + 1), (n[i-1][j-1] + 1)));
                }
            }
        }
        int i = one.length() + 1;
        int j = two.length() + 1;
        while (i > 1 || j > 1) {
            if (i > 1 && j > 1) {
                if (n[i][0] == n[0][j] && n[i][j] == n[i-1][j-1]) {
                    result = "#," + result;
                    i--; j--;
                    continue;
                }
                if (n[i][j] == n[i-1][j-1] + 1) {
                    result = "~" + (char)n[0][j] + "," + result;
                    i--; j--;
                    continue;
                }
            }
            if (j > 1 && n[i][j] == n[i][j-1] + 1) {
                result = "+" + (char)n[0][j] + "," + result;
                j--;
                continue;
            }
            if (i > 1 && n[i][j] == n[i-1][j] + 1) {
                result = "-" + (char)n[i][0] + "," + result;
                i--;
                continue;
            }
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