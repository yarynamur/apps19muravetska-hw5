package ua.edu.ucu;

import ua.edu.ucu.stream.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.util.ArrayList;

public class AsIntStreamTest {
    private IntStream intStream;
    private IntStream emptyStream;

    @Before
    public void init(){
        int[] intArr = {-1, 0, 1, 2, 3};
        intStream = AsIntStream.of(intArr);
        int[] emptyArr = {};
        emptyStream = AsIntStream.of(emptyArr);
    }

    @Test
    public void testAverage() {
        double expResult = 1.0;
        double result = intStream.average();
        assertEquals(expResult, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAverageEmpty(){
        emptyStream.average();
    }

    @Test
    public void testMax() {
        int expResult = 3;
        int result = intStream.max();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxEmpty(){
        emptyStream.max();
    }

    @Test
    public void testMin() {
        int expResult = -1;
        int result = intStream.min();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMinEmpty(){
        emptyStream.min();
    }

    @Test
    public void testCount() {
        int expResult = 5;
        long result = intStream.count();
        assertEquals(expResult, result);
    }

    @Test
    public void testSum() {
        int expResult = 5;
        long result = intStream.sum();
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSumEmpty(){
        emptyStream.sum();
    }

    @Test
    public void testFilter() {
        int[] expResult = {1, 2, 3};
        intStream = intStream.filter(x -> x > 0);
        assertArrayEquals(expResult, intStream.toArray());
    }

    @Test
    public void testForEach(){
        ArrayList<Integer> check = new ArrayList<>();
        intStream.forEach(check::add);
        assertArrayEquals(new Object[]{-1, 0, 1, 2, 3}, check.toArray());
    }

    @Test
    public void testMap() {
        int[] expResult = {1, 0, -1, -2, -3};
        intStream = intStream.map(x -> x * -1);
        assertArrayEquals(expResult, intStream.toArray());
    }

    @Test
    public void testFlatMap() {
        int[] expResult = {1, 1, 0, 2, 1, 3, 4, 4, 9, 5};
        intStream = intStream.flatMap(x -> AsIntStream.of(x*x, x+2));
        assertArrayEquals(expResult, intStream.toArray());
    }

    @Test
    public void testReduce() {
        int expResult = 5;
        assertEquals(expResult, intStream.reduce(0, (idelity, x) -> idelity += x));
    }

    @Test
    public void testToArray() {
        int[] expResult = {-1, 0, 1, 2, 3};
        int[] result = intStream.toArray();
        assertArrayEquals(expResult, result);
    }
}
