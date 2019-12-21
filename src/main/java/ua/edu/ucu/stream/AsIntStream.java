package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.ArrayList;
import java.util.Iterator;

public class AsIntStream implements IntStream {
    private Iterator<Integer> iterator;

    public AsIntStream() {
    }

    private AsIntStream(Iterator<Integer> iterator) {
        this.iterator = iterator;
    }

    public static IntStream of(int... values) {
        return new AsIntStream(new SimpleIter(values));
    }

    private void checkEmpty() {
        if (!iterator.hasNext()){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Double average() {
        checkEmpty();
        int sum = 0;
        int ind = 0;
        for (Iterator<Integer> it = iterator; it.hasNext(); ) {
            Integer number = it.next();
            sum += number;
            ind += 1;
        }
        return ((double) sum) / ind;
    }

    @Override
    public Integer max() {
        checkEmpty();
        int max = Integer.MIN_VALUE;
        for (Iterator<Integer> it = iterator; it.hasNext(); ) {
            Integer number = it.next();
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        checkEmpty();
        int min = Integer.MAX_VALUE;
        for (Iterator<Integer> it = iterator; it.hasNext(); ) {
            Integer number = it.next();
            if (number < min) {
                min = number;
            }
        }
        return min;    }

    @Override
    public long count() {
        long counter = 0;
        while (iterator.hasNext()) {
            counter++;
            iterator.next();
        }
        return counter;
    }

    @Override
    public Integer sum() {
        checkEmpty();
        Integer sum = 0;
        while (iterator.hasNext()) {
            sum+= iterator.next();
        }
        return sum;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        return new AsIntStream(new FilterDecorator(iterator, predicate));    }

    @Override
    public void forEach(IntConsumer action) {
        for (Iterator<Integer> it = iterator; it.hasNext(); ) {
            Integer number = it.next();
            action.accept(number);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        return new AsIntStream(new MapDecorator(iterator, mapper));
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        return new AsIntStream(new FlatMapDecorator(iterator, func));
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int result = identity;
        for (Iterator<Integer> it = iterator; it.hasNext(); ) {
            Integer number = it.next();
            result = op.apply(result, number);
        }
        return result;    }

    @Override
    public int[] toArray() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (Iterator<Integer> it = iterator; it.hasNext(); ) {
            Integer number = it.next();
            arrayList.add(number);
        }
        int[] result = new int[arrayList.size()];
        int i = 0;
        for (Integer el: arrayList) {
            result[i++] = el;
        }
        return result;
    }

    private static class SimpleIter implements Iterator<Integer> {
        private ArrayList list = new ArrayList();
        private int index = 0;

        public SimpleIter(int... values) {
            for (Integer value: values) {
                list.add(value);
            }
        }

        @Override
        public boolean hasNext() {
            return list.size() > index;
        }

        @Override
        public Integer next() {
            return (Integer) list.get(index++);
        }
    }

    private static class FilterDecorator implements Iterator<Integer>{
        private Iterator<Integer> iterator;
        private IntPredicate predicate;
        private int number;

        public FilterDecorator(Iterator<Integer> iterator, IntPredicate predicate) {
            this.iterator = iterator;
            this.predicate = predicate;

        }

        @Override
        public boolean hasNext() {
            while (iterator.hasNext()) {
                number = iterator.next();
                if (predicate.test(number)) {
                    return true;
                }
            }
            return false;        }

        @Override
        public Integer next() {
            return number;
        }
    }

    private class MapDecorator implements Iterator<Integer> {
        private Iterator<Integer> iterator;
        private IntUnaryOperator func;

        protected MapDecorator(Iterator<Integer> iterator,
                               IntUnaryOperator func) {
            this.iterator = iterator;
            this.func = func;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Integer next() {
            return func.apply(iterator.next());
        }
    }

    private class FlatMapDecorator implements Iterator<Integer>{
        private Iterator<Integer> iterator;
        private IntToIntStreamFunction func;
        private SimpleIter simpleIterator;

        public FlatMapDecorator(Iterator<Integer> iterator, IntToIntStreamFunction func){
            this.iterator = iterator;
            this.func = func;
            this.simpleIterator = new SimpleIter();
        }

        @Override
        public boolean hasNext() {
            if(!simpleIterator.hasNext()){
                if (iterator.hasNext()){
                    simpleIterator=  new SimpleIter(func.applyAsIntStream(iterator.next()).toArray()) ;
                    return true;
                }
                else {
                    return false;
                }
            }
            else{
                return true;

            }
        }

        @Override
        public Integer next() {
            return simpleIterator.next();
        }
    }

}
