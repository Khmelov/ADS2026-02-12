package by.it.group551001.evtushenko.lesson13;

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
			run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
			run("A -> B, A -> C, B -> D, C -> D, D -> E, D -> F", true).include("A B C D E F");
			run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 2 -> 3, 4 -> 5", true).include("1 2 3 4 5");
			run("Root -> A, Root -> B, Root -> C, A -> A1, B -> B1, C -> C1", true).include("Root A A1 B B1 C C1");
			run("0 -> 1, 1 -> 2, A -> B, B -> C", true).include("0 1 2 A B C");
			run("X -> Y, 8 -> 9, M -> N", true).include("8 9 M N X Y");
			run("A -> B, B -> C, 1 -> 2, 2 -> 3, First -> Second", true).include("1 2 3 A B C First Second");
			run("Start -> Middle, Middle -> End, Ready -> Go", true).include("Ready Go Start Middle End");
			run("A -> B, A -> C, B -> D, C -> E", true).include("A B C D E");
			run("In1 -> Center, In2 -> Center, In3 -> Center, In4 -> Center", true).include("In1 In2 In3 In4 Center");
			run("Hub -> Out1, Hub -> Out2, Hub -> Out3, Hub -> Out4, Hub -> Out5", true).include("Hub Out1 Out2 Out3 Out4 Out5");
			run("A -> B, A -> C, A -> D, B -> C, B -> D, C -> D", true).include("A B C D");
			
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
			run("0 -> 1", true).include("no").exclude("yes");
			run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
			run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
			run("A -> B, B -> C, C -> D, D -> E, E -> A", true).include("yes").exclude("no");
			run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
			run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
			run("X -> Y, Y -> Z, A -> B, B -> C, C -> A", true).include("yes").exclude("no");
			run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 6", true).include("no").exclude("yes");
			run("A -> B, B -> A", true).include("yes").exclude("no");
			run("A -> A", true).include("yes").exclude("no");
			run("Hub -> Out1, Hub -> Out2, Out1 -> Target, Out2 -> Target", true).include("no").exclude("yes");
			run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
			
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
run("A->B, B->C, C->A, D->E, E->F, F->D", true)
			.include("ABC\nDEF");
run("1->2, 2->3, 3->4, 4->5", true)
			.include("1\n2\n3\n4\n5");
run("A->B, B->C, C->B, C->D, D->E, E->D, E->F, F->G, G->F", true)
			.include("A\nBC\nDE\nFG");
run("X->Y, Y->X, Y->Z, Z->W, W->Z", true)
			.include("XY\nWZ");
run("Center->A, A->Center, Center->B, B->Center, Center->C, C->Center", true)
			.include("ABCCenter");
run("1->2, 2->1, 3->4, 4->3, 5->6, 6->5, 1->3, 3->5", true)
			.include("12\n34\n56");

        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}