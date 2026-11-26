package by.it.group551001.bochilo.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("B -> A", true).include("B A");
        run("C -> A, B -> A", true).include("B C A");
        run("3 -> 2, 2 -> 1, 1 -> 0", true).include("3 2 1 0");
        run("0 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("5 -> 0, 4 -> 0, 5 -> 2, 2 -> 3, 3 -> 1, 4 -> 1", true).include("4 5 0 2 3 1");
        run("A -> D, B -> D, C -> D, D -> E", true).include("A B C D E");
        run("D -> C, C -> B, B -> A", true).include("D C B A");
        run("A -> C, B -> C, C -> D, C -> E, D -> F, E -> F", true).include("A B C D E F");
        run("X -> Y, A -> B", true).include("A B X Y");
        run("0 -> 1, 0 -> 1, 0 -> 1", true).include("0 1");
        run("1 -> 0, 2 -> 0, 2 -> 1", true).include("2 1 0");
        run("E -> A, D -> B, C -> A, B -> A", true).include("C D B E A");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("1 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B", true).include("A\nB");
        run("A->B, B->A", true).include("AB");
        run("1->2, 2->1, 2->3, 3->4, 4->3", true).include("12\n34");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D, F->G", true).include("ABC\nDEF\nG");
        run("X->Y, Y->Z, Z->X", true).include("XYZ");
        run("1->2, 2->3, 3->4, 4->2, 4->5", true).include("1\n234\n5");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}