package by.it.group510902.shust.lesson13;

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
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
        run("Z -> Y, Y -> X", true).include("Z Y X");
        run("C -> B, B -> A", true).include("C B A");
        run("1 -> 4, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("4 -> 1, 4 -> 2, 4 -> 3", true).include("4 1 2 3");
        run("5 -> 2, 5 -> 0, 4 -> 0, 4 -> 1, 2 -> 3, 3 -> 1", true).include("4 5 0 2 3 1");
        run("A -> B", true).include("A B");
        run("B -> A", true).include("B A");
        run("A -> Z, B -> Z, C -> Z, D -> Z", true).include("A B C D Z");
        run("Z -> A, Z -> B, Z -> C, Z -> D", true).include("Z A B C D");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("3 -> 1, 2 -> 1, 1 -> 0", true).include("2 3 1 0");
        run("A -> C, B -> C", true).include("A B C");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 2", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("X -> Y, Y -> X", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 1", true).include("yes").exclude("no");
        run("A -> B, C -> D, D -> E", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("Z -> X, X -> Y, Y -> W", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
        run("A->B, B->C, C->A, D->E, E->F, F->D, C->D", true)
                .include("ABC\nDEF");
        run("A->B, B->A, A->C, C->D, D->C", true)
                .include("AB\nCD");
        run("A->B, B->C, C->A", true)
                .include("ABC");
        run("X->Y, Y->Z, Z->W", true)
                .include("X\nY\nZ\nW");
        run("1->2, 2->1, 2->3, 3->4, 4->3", true)
                .include("12\n34");
        run("Z->Y, Y->X, X->Z, A->B, B->A, A->Z", true)
                .include("AB\nXYZ");
        run("A->B, A->C, B->D, C->D, D->A", true)
                .include("ABCD");
    }


}