package com.lol.fwk.dao.bean.player;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:30
 */

import javax.persistence.*;

/**
 * Description :
 * Created by YangZH on 2017/4/17
 * 19:30
 */
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true,nullable = false)
    private String name;
    private int level;
    private int exp;
    private int winCount;
    private int loseCount;
    private int ranCount;
    private int acountid;
    //mappedBy:用于双向关联时使用，否则会引起数据不一致的问题。
    //fetch:可取的值有FetchType.EAGER和FetchType.LAZY，前者表示主类被加载时加载，后者表示被访问时才会加载
    //cascade：CascadeType.PERSIST（级联新建）、CascadeType.REMOVE（级联删除）、
    // CascadeType.REFRESH（级联刷新）、CascadeType.MERGE（级联更新）、CascadeType.ALL（选择全部）
//    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,mappedBy="player")
//    private Set<Hero> herolist;
    //TODO: 设计英雄表进行级联
    private String herolist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }

    public int getRanCount() {
        return ranCount;
    }

    public void setRanCount(int ranCount) {
        this.ranCount = ranCount;
    }

    public int getAcountid() {
        return acountid;
    }

    public void setAcountid(int acountid) {
        this.acountid = acountid;
    }

    public String getHerolist() {
        return herolist;
    }

    public void setHerolist(String herolist) {
        this.herolist = herolist;
    }
}
