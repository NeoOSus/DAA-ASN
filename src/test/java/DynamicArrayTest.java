import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {
    @Test
    void randomOperations() {
        IntList actual = new DynamicArray();
        ArrayList<Integer> expected = new ArrayList<>();
        Random random = new Random(42);
        for (int t = 0; t < 2000; t++) {
            int value = random.nextInt(30) - 15;
            if (expected.isEmpty() || random.nextBoolean()) {
                int index = random.nextInt(expected.size() + 1);
                actual.add(index, value);
                expected.add(index, value);
            } else {
                int index = random.nextInt(expected.size());
                assertEquals(expected.remove(index).intValue(), actual.remove(index));
            }
            assertEquals(expected.size(), actual.size());
            assertEquals(expected.contains(value), actual.contains(value));
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).intValue(), actual.get(i));
            }
        }
    }

    @Test
    void edgesAndGrowth() {
        IntList list = new DynamicArray();
        checkInvalid(list);
        assertFalse(list.contains(1));
        list.add(7);
        assertEquals(7, list.get(0));
        assertEquals(7, list.remove(0));
        assertEquals(0, list.size());
        for (int i = 0; i < 1000; i++) list.add(i);
        for (int i = 0; i < 1000; i++) assertEquals(i, list.get(i));
        list.add(0, -1);
        list.add(list.size(), -1);
        assertEquals(-1, list.remove(list.size() - 1));
        assertEquals(-1, list.remove(0));
        checkInvalid(list);
    }

    private void checkInvalid(IntList list) {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(list.size()));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(list.size()));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(list.size() + 1, 2));
    }

    @Test
    void exactCounters() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 4; i++) a.add(i);
        a.metrics().reset();
        a.add(4);
        assertEquals(4, a.metrics().steps);
        assertEquals(4, a.metrics().moves);
        a.metrics().reset();
        a.add(1, 8);
        assertEquals(4, a.metrics().steps);
        assertEquals(4, a.metrics().moves);
        a.metrics().reset();
        assertEquals(8, a.remove(1));
        assertEquals(5, a.metrics().steps);
        assertEquals(4, a.metrics().moves);
        a.metrics().reset();
        assertEquals(2, a.get(2));
        assertEquals(1, a.metrics().steps);
        a.metrics().reset();
        assertFalse(a.contains(-1));
        assertEquals(5, a.metrics().steps);
        assertEquals(5, a.metrics().comparisons);
        assertEquals(0, a.metrics().moves);
    }
}
