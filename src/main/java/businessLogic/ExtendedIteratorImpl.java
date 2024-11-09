package businessLogic;

import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ExtendedIteratorImpl<T> implements ExtendedIterator<T> {
    private List<T> list;
    private int currentPosition;

    public ExtendedIteratorImpl(List<T> list) {
        this.list = list;
        this.currentPosition = -1; // Inicialmente antes del primer elemento
    }

    @Override
    public boolean hasNext() {
        return currentPosition < list.size() - 1;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentPosition++;
        return list.get(currentPosition);
    }

    @Override
    public boolean hasPrevious() {
        return currentPosition > 0;
    }

    @Override
    public T previous() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        currentPosition--;
        return list.get(currentPosition);
    }

    @Override
    public void goFirst() {
        currentPosition = 0;
    }

    @Override
    public void goLast() {
        currentPosition = list.size() - 1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
