package io.github.jamsesso.jsonlogic.ast;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class JsonLogicArray implements JsonLogicNode, List<JsonLogicNode> {
  private final List<JsonLogicNode> delegate;
  private volatile Set<String> stringIndex;

  public JsonLogicArray(List<JsonLogicNode> delegate) {
    this.delegate = delegate;
  }

  void indexStringLiterals() {
    if (delegate.size() < 16) {
      return;
    }
    Set<String> strings = new HashSet<>();
    for (JsonLogicNode node : delegate) {
      if (!(node instanceof JsonLogicString)) {
        return;
      }
      strings.add(((JsonLogicString) node).getValue());
    }
    stringIndex = strings;
  }

  /** Returns null when membership requires evaluating the array normally. */
  public Boolean indexedStringMatch(Object value) {
    Set<String> index = stringIndex;
    if (index == null || (value != null && !(value instanceof String))) {
      return null;
    }
    return index.contains(value);
  }

  private List<JsonLogicNode> mutableView() {
    // An iterator or sublist can change the literals after it has been returned.
    if (stringIndex != null) {
      stringIndex = null;
    }
    return delegate;
  }

  @Override
  public JsonLogicNodeType getType() {
    return JsonLogicNodeType.ARRAY;
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
  public Iterator<JsonLogicNode> iterator() {
    return mutableView().iterator();
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
  public boolean add(JsonLogicNode node) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return delegate.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends JsonLogicNode> c) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public boolean addAll(int index, Collection<? extends JsonLogicNode> c) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public JsonLogicNode get(int index) {
    return delegate.get(index);
  }

  @Override
  public JsonLogicNode set(int index, JsonLogicNode element) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public void add(int index, JsonLogicNode element) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
  }

  @Override
  public JsonLogicNode remove(int index) {
    throw new UnsupportedOperationException("json-logic arrays are immutable");
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
  public ListIterator<JsonLogicNode> listIterator() {
    return mutableView().listIterator();
  }

  @Override
  public ListIterator<JsonLogicNode> listIterator(int index) {
    return mutableView().listIterator(index);
  }

  @Override
  public List<JsonLogicNode> subList(int fromIndex, int toIndex) {
    return mutableView().subList(fromIndex, toIndex);
  }
}
