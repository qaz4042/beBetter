package bebetter.basejpa.util;

import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;

import java.util.Collection;

/**
 *
 */
public class CacheUtil {
    /**
     * 设置某缓存项种一个key对应的值
     *
     * @param region 例如:param
     * @param key    例如:projectName         当缓存是以spring注解@Cachable创建的时,一般key都是以冒号开头 例如periodOpenList缓存中的 :user2
     */
    public static void set(String region, String key, Object val) {
        if (null == val) {
            SpringUtils.getBean(CacheChannel.class).evict(region, key);
        } else {
            SpringUtils.getBean(CacheChannel.class).set(region, key, val);
        }
    }

    /**
     * 获取某缓存项种一个key对应的值
     *
     * @param region 例如:param
     * @param key    例如:projectName         当缓存是以spring注解@Cachable创建的时,一般key都是以冒号开头 例如periodOpenList缓存中的 :user2
     */
    public static <T> T get(String region, String key) {
        CacheObject cacheObject = SpringUtils.getBean(CacheChannel.class).get(region, key);
        if (null == cacheObject) {
            return null;
        }
        //noinspection unchecked
        return (T) cacheObject.getValue();
    }

    public static Collection<String> keys(String region) {
        return SpringUtils.getBean(CacheChannel.class).keys(region);
    }

    /**
     * 清理一整个缓存项
     *
     * @param region 例如:param
     */
    public static void clear(String region) {
        SpringUtils.getBean(CacheChannel.class).clear(region);
    }

    /**
     * 清理缓存项中某几个key
     *
     * @param region 例如:param
     * @param key    例如:projectName
     */
    public static void evict(String region, String... key) {
        SpringUtils.getBean(CacheChannel.class).evict(region, key);
    }
}
