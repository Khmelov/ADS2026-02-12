package by.it.group551001.zlotnikov.lesson13;

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
        run("1 -> 2, 3 -> 4", true).include("1 2 3 4");
        run("3 -> 4, 1 -> 2", true).include("1 2 3 4");
        run("B -> C, A -> C", true).include("A B C");
        run("D -> E, A -> B, B -> C, C -> E", true).include("A B C D E");
        run("0 -> 1, 0 -> 2, 1 -> 4, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("5 -> 2, 5 -> 0, 4 -> 0, 4 -> 1, 2 -> 3, 3 -> 1", true).include("4 5 0 2 3 1");
        run("A -> D, B -> D, C -> D, D -> E, D -> F", true).include("A B C D E F");
        run("X -> Y, W -> Z, A -> B", true).include("A B W X Y Z");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("0 -> 3, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("M -> N, K -> L, K -> M, L -> N", true).include("K L M N");
        run("2 -> 1, 3 -> 1, 4 -> 2, 4 -> 3, 5 -> 4", true).include("5 4 2 3 1");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        run("1 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, A -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 4 -> 5, 5 -> 6, 6 -> 4", true).include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5, 5 -> 6", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1, 3 -> 4", true).include("yes").exclude("no");
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
        run("A->B", true).include("A\nB");
        run("A->B, B->A", true).include("AB");
        run("1->2, 2->1, 2->3, 3->4, 4->3", true).include("12\n34");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D, F->G", true).include("ABC\nDEF\nG");
        run("1->2, 2->3, 3->1, 4->5, 5->4, 1->4", true).include("123\n45");
        run("A->B, B->C, C->D, D->B, D->E, E->F, F->E, A->G", true).include("A\nBCD\nEF\nG");
    }


}