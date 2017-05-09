
package com.lol.jibx.shiporderv1;

import com.lol.jibx.common.AbsOrder;
import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/ShipOrder_V1" xmlns:ns1="http://www.yzh.hkjc.hk/Schema/Test/Jibx/Common" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="CTshiporder">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns1:absOrder">
 *       &lt;xs:sequence>
 *         &lt;xs:element type="xs:string" name="orderperson"/>
 *         &lt;xs:element type="ns:shiptotype" name="shipto"/>
 *         &lt;xs:element type="ns:itemtype" name="item" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:attribute type="xs:string" use="required" name="orderid"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class CTshiporder extends AbsOrder
{
    private String orderperson;
    private Shiptotype shipto;
    private List<Itemtype> itemList = new ArrayList<Itemtype>();
    private String orderid;

    /** 
     * Get the 'orderperson' element value.
     * 
     * @return value
     */
    public String getOrderperson() {
        return orderperson;
    }

    /** 
     * Set the 'orderperson' element value.
     * 
     * @param orderperson
     */
    public void setOrderperson(String orderperson) {
        this.orderperson = orderperson;
    }

    /** 
     * Get the 'shipto' element value.
     * 
     * @return value
     */
    public Shiptotype getShipto() {
        return shipto;
    }

    /** 
     * Set the 'shipto' element value.
     * 
     * @param shipto
     */
    public void setShipto(Shiptotype shipto) {
        this.shipto = shipto;
    }

    /** 
     * Get the list of 'item' element items.
     * 
     * @return list
     */
    public List<Itemtype> getItemList() {
        return itemList;
    }

    /** 
     * Set the list of 'item' element items.
     * 
     * @param list
     */
    public void setItemList(List<Itemtype> list) {
        itemList = list;
    }

    /** 
     * Get the 'orderid' attribute value.
     * 
     * @return value
     */
    public String getOrderid() {
        return orderid;
    }

    /** 
     * Set the 'orderid' attribute value.
     * 
     * @param orderid
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }
}
