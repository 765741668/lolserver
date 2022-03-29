package com.lol.fwk.entity.acount;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:30
 */

import com.lol.fwk.util.Utils;

/**
 * Description :
 * Created by YangZH on 2017/4/17
 * 19:30
 */
public class Acount {

    private int id;
    private String acount;
    private String password;

    public Acount(String account, String password) {
        this.id = Utils.getAccountIncrId();
        this.acount = account;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
