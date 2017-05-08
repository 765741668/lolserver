package com.lol.dao.bean;/**
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
@Table(name = "acount")
public class Acount {

    @Id
//    @GeneratedValue(generator = "customIdGenerate")
//    @GenericGenerator(name = "customIdGenerate" ,strategy = "uuid")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true,nullable = false)
    private String acount;
    private String password;

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
