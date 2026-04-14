package by.it.group551004.pichkin_stas.lesson02;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class Lesson02Test {

    @Test(timeout = 2000)
    public void A_VideoRegistrator() {
        var instance = new A_VideoRegistrator();
        var events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        // рассчитаем моменты старта, с длиной сеанса 1
        var starts = instance.calcStartTimes(events, 1);
        var ok = starts.toString().equals("[1.0, 2.2, 3.7, 5.5, 8.1]");
        assertTrue("A_VideoRegistrator failed", ok);
    }

    @Test(timeout = 2000)
    public void B_Sheduler() {
        var instance = new B_Sheduler();
        var events = new B_Sheduler.Event[]{
                new B_Sheduler.Event(0, 3), new B_Sheduler.Event(0, 1),
                new B_Sheduler.Event(1, 2), new B_Sheduler.Event(3, 5),
                new B_Sheduler.Event(1, 3), new B_Sheduler.Event(1, 3),
                new B_Sheduler.Event(1, 3), new B_Sheduler.Event(3, 6),
                new B_Sheduler.Event(2, 7), new B_Sheduler.Event(2, 3),
                new B_Sheduler.Event(2, 7), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(3, 5), new B_Sheduler.Event(2, 4),
                new B_Sheduler.Event(2, 3), new B_Sheduler.Event(3, 7),
                new B_Sheduler.Event(4, 5), new B_Sheduler.Event(6, 7),
                new B_Sheduler.Event(6, 9), new B_Sheduler.Event(7, 9),
                new B_Sheduler.Event(8, 9), new B_Sheduler.Event(4, 6),
                new B_Sheduler.Event(8, 10), new B_Sheduler.Event(7, 10)
        };

        // рассчитаем оптимальное заполнение аудитории
        var starts = instance.calcStartTimes(events, 0, 10);
        var ok = starts.toString().equals("[(0:1), (1:2), (2:3), (3:5), (6:7), (7:9)]");
        assertTrue("B_Sheduler failed", ok);
    }

    @Test(timeout = 2000)
    public void C_GreedyKnapsack() throws Exception {
        var root = System.getProperty("user.dir") + "/src/";
        var file = new File(root + "by/it/a_khmelev/lesson02/greedyKnapsack.txt");
        var costFinal = new C_GreedyKnapsack().calc(file);
        var ok = costFinal == 200;
        assertTrue("C_GreedyKnapsack failed", ok);
    }

}
