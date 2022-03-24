import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    final static private int DEFAULT_CAPACITY = 10;

    private Item[] array;
    private int capacity;
    private int size;

    public RandomizedQueue() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        this.array = (Item[]) new Object[DEFAULT_CAPACITY];
    }

    private void validateNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    private void validateNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        validateNotNull(item);
        this.array[size] = item;
        size++;
        if (size == capacity) grow(capacity << 1);
    }

    private void grow(int newCapacity) {
        Item[] newArr = (Item[]) new Object[newCapacity];
        if (size >= 0) System.arraycopy(this.array, 0, newArr, 0, size);
        this.array = newArr;
        this.capacity = newCapacity;
    }

    // remove and return a random item
    public Item dequeue() {
        validateNotEmpty();
        if (capacity >= (size << 2)) grow(capacity >> 1);
        int i = size - 1;
        int j = StdRandom.uniform(size);
        Item tmp = array[j];
        array[j] = array[i];
        array[i] = null;
        size--;

        return tmp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        validateNotEmpty();
        return this.array[StdRandom.uniform(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new Iter();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        int size = 30;
//        int size = StdRandom.uniform(30, 50);
        for (int i = 0; i < size; i++) {
            queue.enqueue(i);
        }
        if (queue.size() != size) {
            System.err.printf("size() error, Expect: %d, Actual: %d\n", size, queue.size());
        }
        Iterator<Integer> iter1 = queue.iterator();
        Iterator<Integer> iter2 = queue.iterator();
        while (iter1.hasNext()) {
            StdOut.println("iter1: " + iter1.next() + " iter2: " + iter2.next() + " sample: " + queue.sample());
        }
        while (!queue.isEmpty()) {
            StdOut.println(queue.dequeue());
        }
    }

    private class Iter implements Iterator<Item> {
        private final int[] indices;
        int visited;

        private Iter() {
            this.indices = new int[size];
            this.visited = 0;
            for (int i = 0; i < indices.length; i++) {
                indices[i] = i;
            }
            StdRandom.shuffle(indices);
        }

        @Override
        public boolean hasNext() {
            return visited < indices.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            visited++;
            return array[indices[visited - 1]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}