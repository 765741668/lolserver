
package hk.hkjc.yzh.schema.test.jibx.shiporderv1;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:simpleType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/ShipOrder_V1" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="countryType">
 *   &lt;xs:restriction base="xs:string">
 *     &lt;xs:enumeration value="Ch"/>
 *     &lt;xs:enumeration value="Us"/>
 *     &lt;xs:enumeration value="Jp"/>
 *   &lt;/xs:restriction>
 * &lt;/xs:simpleType>
 * </pre>
 */
public enum CountryType {
    CH("Ch"), US("Us"), JP("Jp");
    private final String value;

    private CountryType(String value) {
        this.value = value;
    }

    public static CountryType convert(String value) {
        for (CountryType inst : values()) {
            if (inst.xmlValue().equals(value)) {
                return inst;
            }
        }
        return null;
    }

    public String xmlValue() {
        return value;
    }
}
