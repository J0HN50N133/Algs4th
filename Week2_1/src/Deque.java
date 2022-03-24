import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item val;
        private Node next;
        private Node prev;

        private Node(Item val, Node next, Node prev) {
            this.val = val;
            this.next = next;
            this.prev = prev;
        }

        private Node() {
            this.prev = this;
            this.next = this;
        }

        // NewItem <---> this
        private void addPrev(Item item) {
            this.prev.addNext(item);
        }

        // this <---> NewItem
        private void addNext(Item item) {
            Node node = new Node(item, this.next, this);
            this.next.prev = node;
            this.next = node;
        }

        // prev <--->this <--->next
        private Item remove() {
            this.prev.next = this.next;
            this.next.prev = this.prev;
            return this.val;
        }

        // toRemove <---> this
        private Item removePrev() {
            return this.prev.remove();
        }

        // this <---> toRemove
        private Item removeNext() {
            return this.next.remove();
        }
    }

    // dummy head
    private final Node dummy;
    private int size;

    // construct an empty deque
    public Deque() {
        dummy = new Node();
        size = 0;
    }


    private void validateNotNull(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    private void validateNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateNotNull(item);
        size++;
        dummy.addNext(item);
    }

    // add the item to the back
    public void addLast(Item item) {
        validateNotNull(item);
        size++;
        dummy.addPrev(item);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateNotEmpty();
        size--;
        return dummy.removeNext();
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateNotEmpty();
        size--;
        return dummy.removePrev();
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new Iter(dummy.next);
    }

    private static void printErr(String methodName, Object expect, Object actual) {
        System.err.printf("%s Error, Expected: %s, Actual: %s\n",
                methodName,
                expect == null ? "null" : expect.toString(),
                actual == null ? "null" : actual.toString());
    }

    private void PrintNodeSize() {
        Node node = new Node();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        if (!deque.isEmpty()) {
            printErr("isEmpty", true, deque.isEmpty());
        }
        int size = StdRandom.uniform(10, 10000);
        // 头插size个数字
        Integer first = null;
        for (int i = 0; i < size; i++) {
            first = StdRandom.uniform(Integer.MIN_VALUE / 3, Integer.MAX_VALUE / 3);
            deque.addFirst(first);
        }
        if (deque.isEmpty()) {
            printErr("isEmpty", false, deque.isEmpty());
        }
        if (deque.size() != size) {
            printErr("size", size, deque.size());
        }

        // 尾插size个数字
        Integer last = null;
        for (int i = 0; i < size; i++) {
            last = StdRandom.uniform(Integer.MIN_VALUE / 3, Integer.MAX_VALUE / 3);
            deque.addLast(last);
        }
        size *= 2;
        if (deque.size() != size) {
            printErr("size", size, deque.size());
        }

        Iterator<Integer> it = deque.iterator();
        int count = 0;
        while (it.hasNext() && count++ < 100) {
            System.out.println(it.next());
        }

        Integer actualFirst = deque.removeFirst();
        if (actualFirst == null || !actualFirst.equals(first)) {
            printErr("removeFirst", first, actualFirst);
        }

        Integer actualLast = deque.removeLast();
        if (actualLast == null || !actualLast.equals(last)) {
            printErr("removeLast", last, actualLast);
        }

    }

    private class Iter implements Iterator<Item> {

        Node ptr;

        public Iter(Node ptr) {
            this.ptr = ptr;
        }

        @Override
        public boolean hasNext() {
            return this.ptr != dummy;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            ptr = ptr.next;
            return ptr.prev.val;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}