package com.lol.db;

import java.io.Serializable;

public enum AuditStateEnum implements Serializable {
    Default("未提交审核", "00"), Audit("审核中", "01"), OK("通过审核", "02"), Fail("未通过审核",
            "03");
    private String name;
    private String value;

    private AuditStateEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static AuditStateEnum getAuditState(String value) {
        if ("00".equals(value)) {
            return Default;
        } else if ("01".equals(value)) {
            return Audit;
        } else if ("02".equals(value)) {
            return OK;
        } else {
            return Fail;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
