
package hk.hkjc.yzh.schema.test.jibx.common;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/Common" xmlns:xs="http://www.w3.org/2001/XMLSchema" abstract="true" name="absOrder">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="updateTime"/>
 *     &lt;xs:element type="xs:boolean" name="deleted"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public abstract class AbsOrder {
    private String updateTime;
    private boolean deleted;

    /**
     * Get the 'updateTime' element value.
     *
     * @return value
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Set the 'updateTime' element value.
     *
     * @param updateTime
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Get the 'deleted' element value.
     *
     * @return value
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Set the 'deleted' element value.
     *
     * @param deleted
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
