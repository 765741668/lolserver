package com.lol.fwk.service.bean.cache;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class CacheLogBean implements Serializable {
    /**
     * ID
     */
    private String uuid;
    /**
     * key的前缀
     */
    private String prefix;
    /**
     * key值
     */
    private String cacheKey;
    /**
     * 创建时间
     */
    private String createTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
