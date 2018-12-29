package designer.cache;

/**
 * @author kimi
 * @description combine时检测是否需要测算最小修改量的包装类
 * @date 2018-12-25 16:10
 */


public class CheckCacheObject {
    private Boolean mayCache;
    private Object combineTargetObj;
    private Object combineSourceObj;

    public CheckCacheObject() {
        this.mayCache = false;
    }

    public CheckCacheObject(Boolean mayCache) {
        this.mayCache = mayCache;
    }

    public CheckCacheObject(Boolean mayCache, Object combineTargetObj, Object combineSourceObj) {
        this.mayCache = mayCache;
        this.combineTargetObj = combineTargetObj;
        this.combineSourceObj = combineSourceObj;
    }

    public CheckCacheObject(Boolean mayCache, Object combineTargetObj) {
        this.mayCache = mayCache;
        this.combineTargetObj = combineTargetObj;
    }

    public Boolean getMayCache() {
        return mayCache;
    }

    public CheckCacheObject setMayCache(Boolean mayCache) {
        this.mayCache = mayCache;
        return this;
    }

    public Object getCombineTargetObj() {
        return combineTargetObj;
    }

    public CheckCacheObject setCombineTargetObj(Object combineTargetObj) {
        this.combineTargetObj = combineTargetObj;
        return this;
    }

    public Object getCombineSourceObj() {
        return combineSourceObj;
    }

    public CheckCacheObject setCombineSourceObj(Object combineSourceObj) {
        this.combineSourceObj = combineSourceObj;
        return this;
    }
}
