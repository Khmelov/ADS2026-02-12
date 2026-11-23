package by.it.group510902.kudan.lesson13;

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
        //одно ребро, начало ребра идет первым хоть оно и позже по алфавиту
        run("B -> A", true).include("B A");
        //из одной вершины выходят два ребра, дальше соседи по алфавиту
        run("C -> A, C -> B", true).include("C A B");
        run("0 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        //цепочка в обратном порядке
        run("3 -> 2, 2 -> 1, 1 -> 0", true).include("3 2 1 0");
        run("A -> D, B -> D, C -> D, D -> E", true).include("A B C D E");
        //несвязный граф из трех кусков
        run("E -> F, A -> B, C -> D", true).include("A B C D E F");
        //две цепочки, каждый раз берем минимальную доступную вершину
        run("5 -> 4, 4 -> 3, 5 -> 3, 2 -> 1, 1 -> 0", true).include("2 1 0 5 4 3");
        //повторное ребро не должно ломать степени
        run("X -> Y, Y -> Z, X -> Z, X -> Z", true).include("X Y Z");
        run("D -> C, D -> B, D -> A, A -> B, B -> C", true).include("D A B C");
        run("a -> c, b -> c, c -> d, c -> e, d -> f, e -> f", true).include("a b c d e f");
        run("K -> M, L -> M, M -> N, K -> L, N -> O, L -> O", true).include("K L M N O");
        run("1 -> 2, 3 -> 4, 2 -> 4, 0 -> 3, 0 -> 1", true).include("0 1 2 3 4");
        run("B -> C, A -> C, C -> D, B -> E, A -> E, D -> F, E -> F", true).include("A B C D E F");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        //петля в саму себя тоже цикл
        run("1 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        //пример из условия, ромб без цикла
        run("1 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        //цикл в отдельной компоненте графа
        run("0 -> 1, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E, E -> A", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z, Z -> W, W -> Y", true).include("yes").exclude("no");
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
        //каждая вершина отдельная компонента
        run("A->B", true).include("A\nB");
        run("A->B, B->A", true).include("AB");
        run("1->2, 2->1, 2->3, 3->4, 4->3", true).include("12\n34");
        run("A->B, B->C, C->A, C->D", true).include("ABC\nD");
        //цепочка, исток идет первым
        run("D->C, C->B, B->A", true).include("D\nC\nB\nA");
        //петля не объединяет вершину ни с кем
        run("1->1, 1->2", true).include("1\n2");
        run("E->F, F->G, G->E, A->E, A->B, B->A", true).include("AB\nEFG");
        run("X->Y, Y->Z, Z->X, Z->Q, Q->W, W->Q, Q->R", true).include("XYZ\nQW\nR");
    }


}