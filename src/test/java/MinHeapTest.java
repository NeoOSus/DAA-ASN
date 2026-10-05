import org.junit.jupiter.api.Test;
import java.util.PriorityQueue;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {
    private void checkHeap(MinHeap heap) {
        for (int i = 1; i < heap.size; i++) {
            assertTrue(heap.data[(i - 1) / 2] <= heap.data[i]);
        }
    }

    @Test
    void randomOperationsAndSortedOutput() {
        MinHeap heap = new MinHeap();
        PriorityQueue<Integer> expected = new PriorityQueue<>();
        Random random = new Random(42);
        for (int i = 0; i < 2000; i++) {
            if (expected.isEmpty() || random.nextInt(3) != 0) {
                int value = random.nextInt(100) - 50;
                heap.insert(value);
                expected.add(value);
            } else {
                assertEquals(expected.remove().intValue(), heap.extractMin());
            }
            checkHeap(heap);
            assertEquals(expected.size(), heap.size);
            if (!expected.isEmpty()) assertEquals(expected.peek().intValue(), heap.peekMin());
        }
        int previous = Integer.MIN_VALUE;
        while (!expected.isEmpty()) {
            int value = heap.extractMin();
            assertEquals(expected.remove().intValue(), value);
            assertTrue(previous <= value);
            previous = value;
            checkHeap(heap);
        }
        assertEquals(0, heap.size);
    }

    @Test
    void emptySingleDuplicatesAndExtremeValues() {
        MinHeap heap = new MinHeap();
        assertThrows(IllegalStateException.class, heap::peekMin);
        assertThrows(IllegalStateException.class, heap::extractMin);
        heap.insert(7);
        checkHeap(heap);
        assertEquals(7, heap.peekMin());
        assertEquals(7, heap.extractMin());
        checkHeap(heap);
        assertThrows(IllegalStateException.class, heap::extractMin);
        int[] values = {0, Integer.MAX_VALUE, -3, Integer.MIN_VALUE, -3};
        for (int value : values) {
            heap.insert(value);
            checkHeap(heap);
        }
        int[] sorted = {Integer.MIN_VALUE, -3, -3, 0, Integer.MAX_VALUE};
        for (int value : sorted) {
            assertEquals(value, heap.extractMin());
            checkHeap(heap);
        }
    }

    @Test
    void exactCounters() {
        MinHeap heap = new MinHeap();
        heap.insert(2);
        heap.metrics().reset();
        heap.insert(1);
        assertEquals(4, heap.metrics().steps);
        assertEquals(2, heap.metrics().moves);
        assertEquals(1, heap.metrics().comparisons);
        heap.metrics().reset();
        assertEquals(1, heap.peekMin());
        assertEquals(1, heap.metrics().steps);
        heap.metrics().reset();
        assertEquals(1, heap.extractMin());
        assertEquals(2, heap.metrics().steps);
        assertEquals(1, heap.metrics().moves);
        assertEquals(0, heap.metrics().comparisons);
    }
}
