
package com.lol.jibx.common;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/Common" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="maptype">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="key"/>
 *     &lt;xs:element type="xs:boolean" name="value"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Maptype
{
    private String key;
    private boolean value;

    /** 
     * Get the 'key' element value.
     * 
     * @return value
     */
    public String getKey() {
        return key;
    }

    /** 
     * Set the 'key' element value.
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /** 
     * Get the 'value' element value.
     * 
     * @return value
     */
    public boolean isValue() {
        return value;
    }

    /** 
     * Set the 'value' element value.
     * 
     * @param value
     */
    public void setValue(boolean value) {
        this.value = value;
    }
}
