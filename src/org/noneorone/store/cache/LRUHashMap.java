package org.noneorone.store.cache;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Gailufeng
 */
public class LRUHashMap<K, V> extends ReentrantLock implements Serializable {

    static class HashEntry<K, V> {

        final K key;
        final int hash;
        volatile V value;
        final HashEntry<K, V> next;

        HashEntry<K, V> before, after;

        HashEntry(K key, int hash, HashEntry<K, V> next, V value) {
            this.key = key;
            this.hash = hash;
            this.next = next;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        static final <K, V> HashEntry<K, V>[] newArray(int i) {
            return new HashEntry[i];
        }
    }
    
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_CAPACITY = 1 << 30;
    final int maxCapacity;
    final float loadFactor;
    transient int threshold;
    transient volatile int count;
    transient int modCount;
    transient volatile HashEntry<K, V>[] table;
    transient final HashEntry<K, V> header;

    public LRUHashMap(){
        this(16, DEFAULT_MAX_CAPACITY, 0.75f);
    }
    public LRUHashMap(int initialCapacity, int maxCapacity, float lf) {
        this.maxCapacity = maxCapacity;
        loadFactor = lf;
        header = new HashEntry<K, V>(null, -1, null, null);
        header.before = header.after = header;
        setTable(HashEntry.<K,V>newArray(initialCapacity));
    }

    /**
     * Sets table to new HashEntry array.
     * Call only while holding lock or in constructor.
     */
    void setTable(HashEntry<K, V>[] newTable) {
        threshold = (int) (newTable.length * loadFactor);
        table = newTable;
    }

    /**
     * Returns properly casted first entry of bin for given hash.
     */
    HashEntry<K, V> getFirst(int hash) {
        HashEntry<K, V>[] tab = table;
        return tab[hash & (tab.length - 1)];
    }

    /**
     * Reads value field of an entry under lock. Called if value
     * field ever appears to be null. This is possible only if a
     * compiler happens to reorder a HashEntry initialization with
     * its table assignment, which is legal under memory model
     * but is not known to ever occur.
     */
    V readValueUnderLock(HashEntry<K, V> e) {
        lock();
        try {
            return e.value;
        } finally {
            unlock();
        }
    }

    /**Specialized implementations of map methods */
    V get(Object key) {
        if (count != 0) { // read-volatile
            int hash = hash(key.hashCode());
            HashEntry<K, V> e = getFirst(hash);
            while (e != null) {
                if (e.hash == hash && key.equals(e.key)) {
                    moveToHeader(e);
                    V v = e.value;
                    if (v != null) {
                        return v;
                    }
                    return readValueUnderLock(e); // recheck
                }
                e = e.next;
            }
        }
        return null;
    }

    void moveToHeader(HashEntry<K, V> e) {
        lock();
        try {
            if (e.before == header) {
                return;
            }
            HashEntry<K, V> beforeEntry = e.before;
            HashEntry<K, V> afterEntry = e.after;
            if (beforeEntry != null) {
                beforeEntry.after = afterEntry;
            }
            if (afterEntry != null) {
                afterEntry.before = beforeEntry;
            }
            addToHeader(e);
        } finally {
            unlock();
        }
    }

    void addToHeader(HashEntry<K, V> e){
        HashEntry<K, V> afterEntry = header.after;
        e.after = afterEntry;
        afterEntry.before = e;
        e.before = header;
        header.after = e;
        if(header.before == header){//环形链
            header.before = e;
        }
    }

    boolean containsKey(Object key, int hash) {
        if (count != 0) { // read-volatile
            HashEntry<K, V> e = getFirst(hash);
            while (e != null) {
                if (e.hash == hash && key.equals(e.key)) {
                    return true;
                }
                e = e.next;
            }
        }
        return false;
    }

    boolean containsValue(Object value) {
        if (count != 0) { // read-volatile
            HashEntry<K, V>[] tab = table;
            int len = tab.length;
            for (int i = 0; i < len; i++) {
                for (HashEntry<K, V> e = tab[i]; e != null; e = e.next) {
                    V v = e.value;
                    if (v == null) // recheck
                    {
                        v = readValueUnderLock(e);
                    }
                    if (value.equals(v)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean replace(K key, int hash, V oldValue, V newValue) {
        lock();
        try {
            HashEntry<K, V> e = getFirst(hash);
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            boolean replaced = false;
            if (e != null && oldValue.equals(e.value)) {
                replaced = true;
                e.value = newValue;
            }
            return replaced;
        } finally {
            unlock();
        }
    }

    V replace(K key, int hash, V newValue) {
        lock();
        try {
            HashEntry<K, V> e = getFirst(hash);
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            V oldValue = null;
            if (e != null) {
                oldValue = e.value;
                e.value = newValue;
            }
            return oldValue;
        } finally {
            unlock();
        }
    }

    V put(K key, V value) {
        lock();
        try {
            int hash = hash(key.hashCode());
            int c = count;
            if (c++ > threshold) { // ensure capacity
                rehash();
            }

            HashEntry<K, V>[] tab = table;
            int index = hash & (tab.length - 1);
            HashEntry<K, V> first = tab[index];
            
            HashEntry<K, V> e = first;
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            V oldValue;
            if (e != null) {
                oldValue = e.value;
                e.value = value;
                moveToHeader(e);
            } else {
                oldValue = null;
                ++modCount;
                count = c; // write-volatile
                table[index] = new HashEntry<K, V>(key, hash, first, value);
                addToHeader(table[index]);
            }
            return oldValue;
        } finally {
            unlock();
        }
    }

    void rehash() {
        HashEntry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= maxCapacity) {
              HashEntry<K, V> e = header.before;//环形链的尾部
              int c = oldCapacity - maxCapacity + 5;
              for(int i = 0; i < c; i++){
                  HashEntry<K, V> temp = e.before;
                  remove(e.key, e.hash, null);
                  e = temp;
              }
        }

        HashEntry<K, V>[] newTable = HashEntry.newArray(oldCapacity << 1);
        threshold = (int) (newTable.length * loadFactor);
        int sizeMask = newTable.length - 1;
        for (int i = 0; i < oldCapacity; i++) {
            // We need to guarantee that any existing reads of old Map can
            //  proceed. So we cannot yet null out each bin.
            HashEntry<K, V> e = oldTable[i];

            if (e != null) {
                HashEntry<K, V> next = e.next;
                int idx = e.hash & sizeMask;

                //  Single node on list
                if (next == null) {
                    newTable[idx] = e;
                } else {
                    // Reuse trailing consecutive sequence at same slot
                    HashEntry<K, V> lastRun = e;
                    int lastIdx = idx;
                    for (HashEntry<K, V> last = next;
                            last != null;
                            last = last.next) {
                        int k = last.hash & sizeMask;
                        if (k != lastIdx) {
                            lastIdx = k;
                            lastRun = last;
                        }
                    }
                    newTable[lastIdx] = lastRun;

                    // Clone all remaining nodes
                    for (HashEntry<K, V> p = e; p != lastRun; p = p.next) {
                        int k = p.hash & sizeMask;
                        HashEntry<K, V> n = newTable[k];
                        newTable[k] = new HashEntry<K, V>(p.key, p.hash,
                                n, p.value);
                    }
                }
            }
        }
        table = newTable;
    }

    /**
     * Remove; match on key only if value null, else match both.
     */
    V remove(Object key, int hash, Object value) {
        lock();
        try {
            int c = count - 1;
            HashEntry<K, V>[] tab = table;
            int index = hash & (tab.length - 1);
            HashEntry<K, V> first = tab[index];
            HashEntry<K, V> e = first;
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            //Remove from double list
            HashEntry<K, V> beforeEntry = e.before;
            HashEntry<K, V> afterEntry = e.after;
            if(beforeEntry != null){
                beforeEntry.after = afterEntry;
            }
            if(afterEntry != null){
                afterEntry.before = beforeEntry;
            }

            V oldValue = null;
            if (e != null) {
                V v = e.value;
                if (value == null || value.equals(v)) {
                    oldValue = v;
                    // All entries following removed node can stay
                    // in list, but all preceding ones need to be
                    // cloned.
                    ++modCount;
                    HashEntry<K, V> newFirst = e.next;
                    for (HashEntry<K, V> p = first; p != e; p = p.next) {
                        newFirst = new HashEntry<K, V>(p.key, p.hash,
                                newFirst, p.value);
                    }
                    tab[index] = newFirst;
                    count = c; // write-volatile
                }
            }
            return oldValue;
        } finally {
            unlock();
        }
    }

    void clear() {
        if (count != 0) {
            lock();
            try {
                HashEntry<K, V>[] tab = table;
                for (int i = 0; i < tab.length; i++) {
                    tab[i] = null;
                }
                ++modCount;
                count = 0; // write-volatile
                header.before = header.after = header;
                } finally {
                unlock();
            }
        }
    }

    private static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
}