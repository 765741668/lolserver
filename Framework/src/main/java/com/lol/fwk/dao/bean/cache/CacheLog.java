package com.lol.fwk.dao.bean.cache;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_CACHELOG")
public class CacheLog {
    /**
     * ID
     */
    @Id
    @Column
    private String uuid;
    /**
     * key的前缀
     */
    @Column
    private String prefix;
    /**
     * key值
     */
    @Column
    private String cacheKey;
    /**
     * 创建时间
     */
    @Column
    private String creatTime;

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

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }
}
