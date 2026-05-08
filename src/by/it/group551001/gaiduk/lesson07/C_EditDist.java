package by.it.group551001.gaiduk.lesson07;

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
        int oneNewSize = one.length() + 1;
        int twoNewSize = two.length() + 1;
        int[][] temp = new int[oneNewSize][twoNewSize];
        for (int i = 0; i < oneNewSize; i++) {
            temp[i][0] = i;
        }
        for (int i = 1; i < twoNewSize; i++) {
            temp[0][i] = i;
        }
        for (int i = 1; i < oneNewSize; i++) {
            for (int j = 1; j < twoNewSize; j++) {
                temp[i][j] = Math.min(Math.min(temp[i-1][j] + 1,temp[i][j-1] + 1),
                        (temp[i-1][j-1] + ((one.charAt(i-1) != two.charAt(j-1)) ? 1 : 0)));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        int i = oneNewSize - 1;
        int j = twoNewSize - 1;
        while (i > 0 && j > 0)
        {
            int change = temp[i-1][j-1] + ((one.charAt(i-1) != two.charAt(j-1)) ? 1 : 0);
            int delete = temp[i-1][j] + 1;
            int insert = temp[i][j-1] + 1;
            int min = Math.min(Math.min(change,delete),insert);
            if (min == change){
                if (two.charAt(j-1) == one.charAt(i-1)) {
                    stringBuilder.append(",#");
                }
                else {
                    stringBuilder.append(",").append(two.charAt(j - 1)).append("~");
                }
                i--;
                j--;
            }
            else if (delete == min){
                stringBuilder.append(",").append(one.charAt(i - 1)).append("-");
                i--;
            }
            else{
                stringBuilder.append(",").append(two.charAt(j - 1)).append("+");
                j--;
            }
        }
        if (i != 0){
            stringBuilder.append(",").append(one.charAt(i - 1)).append("-");
        }
        else if (j != 0) {
            stringBuilder.append(",").append(two.charAt(j - 1)).append("+");
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return stringBuilder.reverse().toString();
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