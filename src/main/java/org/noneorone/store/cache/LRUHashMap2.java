package org.noneorone.store.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Gailufeng
 */
public class LRUHashMap2 <K, V> implements Map<K, V> {

    static class LRUNode <K, V>{
        LRUNode<K, V> before, after;
        HashEntry<K, V> entry;
        
        LRUNode(LRUNode<K, V> before, LRUNode<K, V> after){
            this.before = before;
            this.after = after;
            if(before != null){
                before.after = this;
            }
            if(after != null){
                after.before = this;
            }
        }
    }

    static class HashEntry<K, V> implements Entry<K, V>{
        final K key;
        final int hash;
        volatile V value;
        final HashEntry<K, V> next;
        final LRUNode<K, V> node;

        HashEntry(K key, int hash, HashEntry<K, V> next, V value, LRUNode<K, V> node) {
            this.key = key;
            this.hash = hash;
            this.next = next;
            this.value = value;
            this.node = node;
            node.entry = this;
        }

        @SuppressWarnings("unchecked")
        static final <K, V> HashEntry<K, V>[] newArray(int i) {
            return new HashEntry[i];
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = value;
            this.value = value;
            return oldValue;
        }
    }
    
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_MAX_CAPACITY = 1 << 30;
    final int maxCapacity;
    final float loadFactor;
    final float extinctFactor;
    transient int threshold;
    transient volatile int count;
    transient int modCount;
    transient volatile HashEntry<K, V>[] table;
    transient final LRUNode<K, V> header;
    final Lock lock = new ReentrantLock();
    final Lock lruLock = new ReentrantLock();

    public LRUHashMap2(){
        this(16, DEFAULT_MAX_CAPACITY, 0.75f, 0.01f);
    }

    public LRUHashMap2(int maxCapacity, float ef){
        this(16, maxCapacity, 0.75f, ef);
    }

    @SuppressWarnings("unchecked")
	public LRUHashMap2(int initialCapacity, int maxCapacity, float lf, float ef) {
        this.maxCapacity = maxCapacity;
        loadFactor = lf;
        extinctFactor = ef;
        header = new LRUNode(null, null);
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
        lock.lock();
        try {
            return e.value;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return table.length == 0;
    }

    void moveToHeader(LRUNode<K, V> node) {
        if (node == header.after) {
            return;
        }
        lruLock.lock();
        try {
            LRUNode<K, V> beforeNode = node.before;
            LRUNode<K, V> afterNode = node.after;
            if(beforeNode != null){
                beforeNode.after = afterNode;
            }
            if (afterNode != null) {
                afterNode.before = beforeNode;
            }
            addToHeader(node);
        }finally{
            lruLock.unlock();
        }
    }

    void addToHeader(LRUNode<K, V> node){
        LRUNode<K, V> afterNode = header.after;
        node.after = afterNode;
        afterNode.before = node;
        node.before = header;
        header.after = node;
        if(header.before == header){//构成环形链
            header.before = node;
        }
    }
    
    public boolean containsKey(Object key) {
        int hash = hash(key.hashCode());
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

    public boolean containsValue(Object value) {
        if (count != 0) { // read-volatile
            HashEntry<K, V>[] tab = table;
            int len = tab.length;
            for (int i = 0; i < len; i++) {
                for (HashEntry<K, V> e = tab[i]; e != null; e = e.next) {
                    V v = e.value;
                    if (v == null) {// recheck
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

    public V get(Object key) {
        if (count != 0) { // read-volatile
            int hash = hash(key.hashCode());
            HashEntry<K, V> e = getFirst(hash);

            while (e != null) {
                if (e.hash == hash && key.equals(e.key)) {
                    moveToHeader(e.node);
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

    public V put(K key, V value) {
        if (value == null)
            throw new NullPointerException();
         int hash = hash(key.hashCode());
         return put(key, hash, value, false);
    }

    public V putIfAbsent(K key, V value) {
        if (value == null)
            throw new NullPointerException();
        int hash = hash(key.hashCode());
        return put(key, hash, value, true);
    }
    
    V put(K key, int hash, V value, boolean onlyIfAbsent){
        lock.lock();
        try {
            if(count  > maxCapacity - 1){
                removeLastNodes();
            }

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
                moveToHeader(e.node);
            } else {
                oldValue = null;
                ++modCount;
                count = c; // write-volatile

                LRUNode<K, V> node = new LRUNode<K, V>(null, null);
                table[index] = new HashEntry<K, V>(key, hash, first, value, node);
                moveToHeader(node);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    protected void rehash() {
        HashEntry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
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
                    for (HashEntry<K, V> last = next; last != null; last = last.next) {
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
                        newTable[k] = new HashEntry<K, V>(p.key, p.hash, n, p.value, p.node);
                    }
                }
            }
        }
        table = newTable;
    }

    protected void removeLastNodes(){
       int nodesCount = (int)(maxCapacity * extinctFactor);
       nodesCount = nodesCount > 0 ? nodesCount : 1;
       for (int i = 0; i < nodesCount; i++) {
           if (removeLastNode() == null) {
               break;
           }
       }
    }

    public V removeLastNode(){
        LRUNode<K, V> node = header.before;
        if (node != null && node != header) {
            return remove(header.before.entry.key);
        }
        return null;
    }

    public V remove(Object key) {
        lock.lock();
        try {
            int hash = hash(key.hashCode());
            int c = count - 1;
            HashEntry<K, V>[] tab = table;
            int index = hash & (tab.length - 1);
            HashEntry<K, V> first = tab[index];
            HashEntry<K, V> e = first;
            while (e != null && (e.hash != hash || !key.equals(e.key))) {
                e = e.next;
            }

            V oldValue = null;
            if (e != null) {
                V v = e.value;
                if (key.equals(e.key)) {
                    lruLock.lock();
                    try{
                       LRUNode<K, V> beforeNode = e.node.before;
                       LRUNode<K, V> afterNode = e.node.after;
                       beforeNode.after = afterNode;
                       afterNode.before = beforeNode;
                    }finally{
                        lruLock.unlock();
                    }

                    oldValue = v;
                    // All entries following removed node can stay
                    // in list, but all preceding ones need to be
                    // cloned.
                    ++modCount;
                    HashEntry<K, V> newFirst = e.next;
                    for (HashEntry<K, V> p = first; p != e; p = p.next) {
                        newFirst = new HashEntry<K, V>(p.key, p.hash, newFirst, p.value, p.node);
                    }
                    tab[index] = newFirst;
                    count = c; // write-volatile
                }
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
         if (count != 0) {
            lock.lock();
            try {
                HashEntry<K, V>[] tab = table;
                for (int i = 0; i < tab.length; i++) {
                    tab[i] = null;
                }
                ++modCount;
                count = 0; // write-volatile
                lruLock.lock();
                try{
                    header.before = header.after = header;
                }finally{
                     lruLock.unlock();
                }

            }finally {
                lock.unlock();
            }
        }
    }

    public Set<K> keySet() {
        if(table.length == 0){
            return null;
        }
        Set<K> s = new HashSet<K>();
        for(HashEntry<K, V> e : table){
            s.add(e.key);
        }
        return s;
    }

    public Collection<V> values() {
        if(table.length == 0){
            return null;
        }
        Set<V> s = new HashSet<V>();
        for(HashEntry<K, V> e : table){
            s.add(e.value);
        }
        return s;
    }

    public Set<Entry<K, V>> entrySet() {
        if(table.length == 0){
            return null;
        }
        Set<Entry<K, V>> s = new HashSet<Entry<K, V>>();
        for(HashEntry<K, V> e : table){
            s.add(e);
        }
        return s;
    }

    private static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

}