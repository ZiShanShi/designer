package designer.cache;

import designer.IDesiginerConstant;
import designer.engine.DesignerUtil;
import designer.options.IOption;
import foundation.config.Configer;
import foundation.util.Util;

public class CacheManager {
    private static CacheManager ourInstance;
    private LruCache<String, IOption> lruCache;

    public static CacheManager getInstance() {
        if (ourInstance != null) {
            synchronized (CacheManager.class) {
                if (ourInstance != null) {
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
        String cacheSzie = Configer.getParam(IDesiginerConstant.CACHE_DEFAULT_SIZE);
        int size = Util.stringToInt(cacheSzie, -1);
        if (size != -1) {
            lruCache = new LruCache<>(size);
        }
        else {
            lruCache = new LruCache<>();
        }
    }

    public void put(String key, IOption value) {
        if (lruCache == null) {
            getInstance();
        }
        lruCache.put(key, value);
    }


    public void remove(String key) {
        if (lruCache == null) {
            getInstance();
        }
        IOption removedOptions = lruCache.remove(key);

        String id = removedOptions.getId();
        String optionFilePath = DesignerUtil.getOptionFilePath(id);
        //TODO
    }
}
