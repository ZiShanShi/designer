package designer.cache;

import org.apache.lucene.util.RamUsageEstimator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class LruCache<K, V> implements Cache<K, V> {

    /**
     * The flag represents remove all entries in the cache.
     */
    private static final int REMOVE_ALL = -1;

    private static final int DEFAULT_CAPACITY = 10;

    private final Map<K, V> map;

    private final Long maxMemorySize;

    private Long memorySize;

    private CacheManager manager;

    private LruCache() {
        this(DEFAULT_CAPACITY);
    }

    private LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity <= 0");
        }
        this.map = new LruHashMap<>(capacity);
        maxMemorySize = capacity * 1024 * 1024L;
        memorySize = 0l;
    }

    public LruCache(CacheManager manager,int capacity) {
        this(capacity);
        this.manager = manager;
    }
    public LruCache(CacheManager manager) {
        this(DEFAULT_CAPACITY);
        this.manager = manager;
    }

    @Override
    public final V get(K key) {
        Objects.requireNonNull(key, "key == null");
        synchronized (this) {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Override
    public final V put(K key, V value) {
        Objects.requireNonNull(key, "key == null");
        Objects.requireNonNull(value, "value == null");
        V previous;
        synchronized (this) {
            previous = map.put(key, value);
            memorySize += safeSizeOf(key, value);
            if (previous != null) {
                memorySize -= safeSizeOf(key, value);
            }
            trimToSize(maxMemorySize);
        }
        return previous;
    }

    @Override
    public final V remove(K key) {
        Objects.requireNonNull(key, "key == null");
        V previous;
        synchronized (this) {
            previous = map.remove(key);
            if (previous != null) {
                memorySize -= safeSizeOf(key, previous);
            }
        }
        return previous;
    }

    @Override
    public synchronized final void clear() {
        trimToSize(REMOVE_ALL);
    }

    @Override
    public synchronized final long getMaxMemorySize() {
        return maxMemorySize;
    }

    @Override
    public synchronized final long getMemorySize() {
        return memorySize;
    }

    /**
     * Returns a copy of the current contents of the cache.
     */
    public synchronized final Map<K, V> snapshot() {
        return new LinkedHashMap<>(map);
    }

    /**
     * Returns the class name.
     * <p>
     * This method should be overridden to debug exactly.
     *
     * @return class name.
     */
    protected String getClassName() {
        return LruCache.class.getName();
    }

    /**
     * Returns the size of the entry.
     * <p>
     * The default implementation returns 1 so that max size is the maximum number of entries.
     * <p>
     * <em>Note:</em> This method should be overridden if you control memory size correctly.
     *
     * @param value value
     * @return the size of the entry.
     */

    private long safeSizeOf(K key, V value) {
        long result = sizeOf(value);
        if (result < 0) {
            throw new IllegalStateException("Negative size: " + key + "=" + value);
        }
        return result;
    }


    protected long sizeOf(V value) {
        long l = RamUsageEstimator.sizeOf(value);

        return  l;
    }

    /**
     * Remove the eldest entries.
     * <p>
     * <em>Note:</em> This method has to be called in synchronized block.
     *
     * @param maxSize max size
     */
    private void trimToSize(long maxSize) {
        while (true) {
            if (memorySize <= maxSize || map.isEmpty()) {
                break;
            }
            if (memorySize < 0 || (map.isEmpty() && memorySize != 0)) {
                throw new IllegalStateException(getClassName() + ".getValueSize() is reporting inconsistent results");
            }
            Map.Entry<K, V> toRemove = map.entrySet().iterator().next();
            K key = toRemove.getKey();
            V value = toRemove.getValue();
            map.remove(key);

            manager.remove((String) key);
            memorySize -= safeSizeOf(key, value);
        }
    }

    @Override
    public synchronized final String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append(entry.getKey())
                    .append('=')
                    .append(entry.getValue())
                    .append(",");
        }
        sb.append("maxMemory=")
                .append(maxMemorySize)
                .append(",")
                .append("memorySize=")
                .append(memorySize);
        return sb.toString();
    }

}
