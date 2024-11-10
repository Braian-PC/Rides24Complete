package businessLogic;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ExtendedIteratorCities<T> implements ExtendedIterator<T> {
    private List<T> list;
    private int position;

    public ExtendedIteratorCities(List<T> list) {
        // Clonar la lista para evitar modificar la original
        this.list = List.copyOf(list);
        this.position = 0; // Inicializar en la primera posición
    }

    @Override
    public boolean hasNext() {
        return position < list.size();
    }

    @Override
    public T next() {
        if (hasNext()) {
            return list.get(position++);
        }
        throw new NoSuchElementException("No more elements");
    }

    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public T previous() {
        if (hasPrevious()) {
            return list.get(--position);
        }
        throw new NoSuchElementException("No previous elements");
    }

    @Override
    public void goFirst() {
        position = 0;
    }

    @Override
    public void goLast() {
        position = list.size(); // Colocar después del último elemento
    }
}
