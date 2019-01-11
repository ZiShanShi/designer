package designer.cache;

import designer.DesignerConstant;
import designer.widget.Widget;
import designer.widget.WidgetManager;
import foundation.config.Configer;
import foundation.util.Util;

public class CacheManager {
    private static CacheManager ourInstance;
    private LruCache<String, Widget> lruCache;

    public static CacheManager getInstance() {
        if (ourInstance == null) {
            synchronized (CacheManager.class) {
                if (ourInstance == null) {
                    ourInstance = new CacheManager();
                }
            }
        }
        return ourInstance;
    }

    private CacheManager () {
        _initLruCache();
    }

    private void _initLruCache() {
        String cacheSzie = Configer.getParam(DesignerConstant.CACHE_DEFAULT_SIZE);
        int size = Util.stringToInt(cacheSzie, -1);
        if (size != -1) {
            lruCache = new LruCache<>(this, size);
        }
        else {
            lruCache = new LruCache<>(this);
        }
    }

    public void put(String key, Widget value) {
        if (lruCache == null) {
            getInstance();
        }
        lruCache.put(key, value);
    }
    public Widget get(String key) {
        if (lruCache == null) {
            getInstance();
            return null;
        }
        return lruCache.get(key);
    }

    public  void remove(String key) {
        if (lruCache == null) {
            getInstance();
        }

        Widget widget = get(key);
        if (widget != null) {
            Widget remove = lruCache.remove(key);
            WidgetManager.getInstance().remove(key, remove);
        }

    }
}
