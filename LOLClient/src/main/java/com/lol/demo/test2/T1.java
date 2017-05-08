package com.lol.demo.test2;

import com.lol.demo.parenttest.Ftest;

import java.util.List;

public class T1 extends Ftest {
    private static final long serialVersionUID = -2905760901225809291L;
    private String id;
    private String name;
    private List<TestLists> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TestLists> getList() {
        return list;
    }

    public void setList(List<TestLists> list) {
        this.list = list;
    }


}
