
package hk.hkjc.yzh.schema.test.jibx.shiporderv1;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/ShipOrder_V1" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="shiptotype">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="name"/>
 *     &lt;xs:element type="xs:string" name="address"/>
 *     &lt;xs:element type="xs:string" name="city"/>
 *     &lt;xs:element type="ns:countryType" name="country"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Shiptotype {
    private String name;
    private String address;
    private String city;
    private CountryType country;

    /**
     * Get the 'name' element value.
     *
     * @return value
     */
    public String getName() {
        return name;
    }

    /**
     * Set the 'name' element value.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the 'address' element value.
     *
     * @return value
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the 'address' element value.
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the 'city' element value.
     *
     * @return value
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the 'city' element value.
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the 'country' element value.
     *
     * @return value
     */
    public CountryType getCountry() {
        return country;
    }

    /**
     * Set the 'country' element value.
     *
     * @param country
     */
    public void setCountry(CountryType country) {
        this.country = country;
    }
}
