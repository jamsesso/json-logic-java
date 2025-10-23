package io.github.jamsesso.jsonlogic.utils;

import java.util.*;

public class LocalContext implements Map<String,Object>{
    private final Map<String, Object> delegate = new HashMap<>();
    private final Map<?, ?> parent;

    public LocalContext(Object parent) {
        if( parent instanceof Map) {
            this.parent = (Map<?, ?>) parent;
        }else{
            this.parent = new HashMap<>();
        }
    }


    @Override
    public int size() {
        return delegate.size() + parent.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty() && parent.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key) || parent.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value) || parent.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return delegate.get(key) != null ? delegate.get(key) : parent.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return delegate.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<String> keySet() {
        final Set<String> keys = new HashSet<>(delegate.keySet());
        for( Object key : parent.keySet()){
            keys.add(""+key);
        }
        return keys;
    }

    @Override
    public Collection<Object> values() {
        final List<Object> values = new ArrayList<>(delegate.values());
        values.addAll(parent.values());
        return values;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        final Set<Entry<String, Object>> entries = new HashSet<>(delegate.entrySet());
        for( Map.Entry<?, ?> entry : parent.entrySet()){
            entries.add(new AbstractMap.SimpleEntry<>(""+entry.getKey(), entry.getValue()));
        }
        return entries;
    }
}
