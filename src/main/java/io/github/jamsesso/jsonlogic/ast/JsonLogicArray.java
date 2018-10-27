package io.github.jamsesso.jsonlogic.ast;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JsonLogicArray implements JsonLogicNode, List<JsonLogicNode> {
  private final List<JsonLogicNode> delegate;

  public JsonLogicArray(List<JsonLogicNode> delegate) {
    this.delegate = delegate;
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
    return delegate.listIterator();
  }

  @Override
  public ListIterator<JsonLogicNode> listIterator(int index) {
    return delegate.listIterator(index);
  }

  @Override
  public List<JsonLogicNode> subList(int fromIndex, int toIndex) {
    return delegate.subList(fromIndex, toIndex);
  }
}
