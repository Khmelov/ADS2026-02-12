package by.it.group551004.kruk.lesson13;

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

        // --- Свои тесты ---

        // одна вершина без рёбер невозможна по формату, но изолированные вершины
        // появляются как «сток»/«исток» через рёбра; проверяем «веер» из одного истока
        run("A -> B, A -> C, A -> D, A -> E", true).include("A B C D E");

        // «перевёрнутый веер» — несколько истоков, один сток
        run("A -> E, B -> E, C -> E, D -> E", true).include("A B C D E");

        // цепочка с буквами
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");

        // ромб с разными путями
        run("A -> B, A -> C, B -> D, C -> D, D -> E", true).include("A B C D E");

        // лексикографический порядок при равнозначности: доступны B и C,
        // сначала должен выйти B
        run("A -> C, A -> B", true).include("A B C");

        // несколько независимых компонент
        run("A -> B, C -> D", true).include("A B C D");

        // «бабочка»: две параллельные цепочки
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F", true).include("A B C D E F");

        // рёбра, заданные в «неудобном» порядке — алгоритм всё равно даёт корректный порядок
        run("C -> D, A -> B, B -> D, A -> C", true).include("A B C D");

        // дубликаты рёбер не должны ломать степень входа
        run("A -> B, A -> B, A -> B, B -> C", true).include("A B C");

        // граф с числовыми вершинами, где лексикографический порядок строк:
        // "10" < "2" < "3" < "4" как строк, но это ожидаемое поведение
        run("10 -> 2, 10 -> 3, 2 -> 4, 3 -> 4", true).include("10 2 3 4");

        // большая цепочка с ветвлением в середине
        run("0 -> 1, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");

        // порядок: сначала все истоки, затем внутренние
        run("A -> C, B -> C, C -> D, C -> E", true).include("A B C D E");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        // --- Свои тесты ---

        // петля
        run("A -> A", true).include("yes").exclude("no");

        // ромб без цикла
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");

        // цикл длины 4
        run("A -> B, B -> C, C -> D, D -> A", true).include("yes").exclude("no");

        // цикл в одной из ветвей, остальная часть ациклична
        run("A -> B, B -> C, C -> B", true).include("yes").exclude("no");

        // две независимые компоненты, цикл только во второй
        run("A -> B, C -> D, D -> E, E -> C", true).include("yes").exclude("no");

        // длинная цепочка — цикла нет
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("no").exclude("yes");

        // параллельные пути без цикла
        run("A -> B, A -> C, B -> D, C -> D, D -> E", true).include("no").exclude("yes");

        // цикл с «хвостом» перед ним
        run("X -> A, A -> B, B -> C, C -> A", true).include("yes").exclude("no");

        // дубликаты рёбер не создают ложного цикла
        run("A -> B, A -> B, B -> C", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        // --- Свои тесты ---

        // единственный цикл из трёх вершин — один компонент
        run("A->B, B->C, C->A", true).include("ABC");

        // две изолированные петли — два компонента, порядок по истоку/стоку
        // (компоненты не связаны, топологическая сортировка конденсации
        //  выдаст их в лексикографическом порядке)
        run("A->A, B->B", true).include("A\nB");

        // цепочка SCC: {A,B} -> {C,D} -> {E}
        run("A->B, B->A, B->C, C->D, D->C, D->E", true).include("AB\nCD\nE");

        // несколько SCC с ветвлением в конденсации
        run("A->B, B->A, A->C, C->D, D->C", true).include("AB\nCD");

        // две независимые цепочки SCC
        run("A->B, B->A, C->D, D->C", true).include("AB\nCD");

        // большая цепочка: каждый узел — свой SCC
        run("A->B, B->C, C->D, D->E", true).include("A\nB\nC\nD\nE");
    }
}