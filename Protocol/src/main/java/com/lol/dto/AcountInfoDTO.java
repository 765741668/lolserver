package com.lol.dto;

public class AcountInfoDTO {
    private String acount;
    private String password;

    public AcountInfoDTO(String acount, String password) {
        this.acount = acount;
        this.password = password;
    }

    public String getacount() {
        return acount;
    }

    public void setacount(String acount) {
        this.acount = acount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}