package businessLogic;

import java.util.List;
import java.util.ListIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ExtendedIteratorCities<T> implements ExtendedIterator<T> {
    private List<T> list;
    private ListIterator<T> iterator;

    public ExtendedIteratorImpl(List<T> list) {
        this.list = list;
        this.iterator = list.listIterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public T previous() {
        return iterator.previous();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public void goFirst() {
        this.iterator = list.listIterator();
    }

    @Override
    public void goLast() {
        this.iterator = list.listIterator(list.size());
    }
}
