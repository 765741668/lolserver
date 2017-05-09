
package com.lol.jibx.shiporderv1;

import java.math.BigDecimal;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/ShipOrder_V1" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="itemtype">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:string" name="title"/>
 *     &lt;xs:element type="xs:string" name="note" minOccurs="0"/>
 *     &lt;xs:element type="xs:string" name="quantity"/>
 *     &lt;xs:element type="xs:decimal" name="price"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class Itemtype
{
    private String title;
    private String note;
    private String quantity;
    private BigDecimal price;

    /** 
     * Get the 'title' element value.
     * 
     * @return value
     */
    public String getTitle() {
        return title;
    }

    /** 
     * Set the 'title' element value.
     * 
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** 
     * Get the 'note' element value.
     * 
     * @return value
     */
    public String getNote() {
        return note;
    }

    /** 
     * Set the 'note' element value.
     * 
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /** 
     * Get the 'quantity' element value.
     * 
     * @return value
     */
    public String getQuantity() {
        return quantity;
    }

    /** 
     * Set the 'quantity' element value.
     * 
     * @param quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /** 
     * Get the 'price' element value.
     * 
     * @return value
     */
    public BigDecimal getPrice() {
        return price;
    }

    /** 
     * Set the 'price' element value.
     * 
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
