import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListMetricsTest {
    @Test
    void headAndMiddleCounters() {
        MyLinkedList list = new MyLinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.metrics().reset();
        assertEquals(30, list.get(2));
        assertEquals(2, list.metrics().steps);
        list.metrics().reset();
        list.add(0, 5);
        assertEquals(0, list.metrics().steps);
        assertEquals(2, list.metrics().moves);
        assertEquals(5, list.remove(0));
        assertEquals(3, list.metrics().moves);
        list.metrics().reset();
        list.add(2, 25);
        assertEquals(1, list.metrics().steps);
        assertEquals(2, list.metrics().moves);
        list.metrics().reset();
        assertEquals(25, list.remove(2));
        assertEquals(2, list.metrics().steps);
        assertEquals(1, list.metrics().moves);
        list.metrics().reset();
        assertFalse(list.contains(-1));
        assertEquals(3, list.metrics().steps);
        assertEquals(3, list.metrics().comparisons);
        assertEquals(0, list.metrics().moves);
    }

    @Test
    void tailAfterRemovingEverything() {
        MyLinkedList list = new MyLinkedList();
        list.add(1);
        list.add(2);
        assertEquals(2, list.remove(1));
        list.add(3);
        assertEquals(3, list.get(1));
        list.remove(0);
        list.remove(0);
        list.metrics().reset();
        list.add(0, 9);
        assertEquals(2, list.metrics().moves);
        assertEquals(9, list.remove(0));
        assertEquals(4, list.metrics().moves);
        list.add(10);
        assertEquals(10, list.get(0));
    }
}
