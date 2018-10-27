package io.github.jamsesso.jsonlogic.utils;

import com.google.gson.JsonArray;

import java.lang.reflect.Array;
import java.util.*;

public class ArrayLike implements List<Object> {
  private final List<Object> delegate;

  @SuppressWarnings("unchecked")
  public ArrayLike(Object data) {
    if (data instanceof List) {
      delegate = (List) data;
    }
    else if (data != null && data.getClass().isArray()) {
      delegate = new ArrayList<>();

      for (int i = 0; i < Array.getLength(data); i++) {
        delegate.add(i, Array.get(data, i));
      }
    }
    else if (data instanceof JsonArray) {
      delegate = (List) JsonValueExtractor.extract((JsonArray) data);
    }
    else if (data instanceof Iterable) {
      delegate = new ArrayList<>();

      for (Object item : (Iterable) data) {
        delegate.add(item);
      }
    }
    else {
      throw new IllegalArgumentException("ArrayLike only works with lists, iterables, arrays, or JsonArray");
    }
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return delegate.contains(o);
  }

  @Override
  public Iterator<Object> iterator() {
    return delegate.iterator();
  }

  @Override
  public Object[] toArray() {
    return delegate.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return delegate.toArray(a);
  }

  @Override
  public boolean add(Object o) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return delegate.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<?> c) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public boolean addAll(int index, Collection<?> c) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public Object get(int index) {
    return delegate.get(index);
  }

  @Override
  public Object set(int index, Object element) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public void add(int index, Object element) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public Object remove(int index) {
    throw new UnsupportedOperationException("ArrayLike is immutable");
  }

  @Override
  public int indexOf(Object o) {
    return delegate.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return delegate.lastIndexOf(o);
  }

  @Override
  public ListIterator<Object> listIterator() {
    return delegate.listIterator();
  }

  @Override
  public ListIterator<Object> listIterator(int index) {
    return delegate.listIterator(index);
  }

  @Override
  public List<Object> subList(int fromIndex, int toIndex) {
    return delegate.subList(fromIndex, toIndex);
  }

  @Override
  public String toString() {
    return Arrays.toString(delegate.toArray());
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other instanceof Iterable) {
      int i = 0;

      for (Object item : (Iterable) other) {
        if (i >= delegate.size()) {
          return false;
        }
        else if (!Objects.equals(item, delegate.get(i))) {
          return false;
        }

        i++;
      }

      if (i != delegate.size()) {
        return false;
      }

      return false;
    }

    return false;
  }

  public static boolean isEligible(Object data) {
    return data != null && (data instanceof List || data instanceof Iterable || data.getClass().isArray());
  }
}
