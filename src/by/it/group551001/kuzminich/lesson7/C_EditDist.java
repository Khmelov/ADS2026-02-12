package by.it.group551001.kuzminich.lesson7;

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
    int get_min(int a, int b, int c){
        if (a<b){
            return (a<c) ? a : c;
        }else{
            return (b<c) ? b : c;
        }
    }

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // Create table ======================
        int l1 = one.length();
        int l2 = two.length();
        int [][] D = new int [l1+1][l2+1];

        for (int i = 0; i<=l1; i++){
            D[i][0] = i;
        }

        for (int j = 0; j<=l2; j++){
            D[0][j] = j;
        }
        for (int i = 1; i<=l1; i++){
            for (int j = 1; j<=l2; j++){
                int cost = (one.charAt(i-1) == two.charAt(j-1)) ? 0 : 1;
                D[i][j] = get_min(D[i][j-1] + 1, D[i-1][j] + 1, D[i-1][j-1] + cost) ;
            }
        }
        //=========================================

        StringBuilder res = new StringBuilder();

        int i = l1;
        int j = l2;
        while (i>0 || j>0){
            //буквы совпали
            if (i>0 && j>0 && one.charAt(i-1) == two.charAt(j-1)){
                res.insert(0, ',');
                res.insert(0, '#');
                i--;
                j--;
            }
            //замена элементов (диагональ)
            else if (i>0 && j>0 && D[i][j] == D[i-1][j-1] + 1){
                res.insert(0, ',');
                res.insert(0, two.charAt(j-1));
                res.insert(0, '~');
                i--;
                j--;
            }
            //удаление было (ход наверх)
            else if (i > 0 && D[i][j] == D[i-1][j] + 1){
                res.insert(0, ',');
                res.insert(0, one.charAt(i-1));
                res.insert(0, '-');
                i--;
            }
            // вставка (ход влево)
            else {//if (j>0 && D[i][j] == D[i][j-1] + 1)
                res.insert(0, ',');
                res.insert(0, two.charAt(j-1));
                res.insert(0, '+');
                j--;
            }
        }


        String result = res.toString();
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