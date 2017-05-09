
package com.lol.jibx.chuanorderv1;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/ChuanOrder_v1" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="country">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="China"/>
 *     &lt;xs:enumeration value="English"/>
 *     &lt;xs:enumeration value="JaPan"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum Country {
    CHINA("China"), ENGLISH("English"), JA_PAN("JaPan");
    private final String value;

    private Country(String value) {
        this.value = value;
    }

    public String xmlValue() {
        return value;
    }

    public static Country convert(String value) {
        for (Country inst : values()) {
            if (inst.xmlValue().equals(value)) {
                return inst;
            }
        }
        return null;
    }
}
