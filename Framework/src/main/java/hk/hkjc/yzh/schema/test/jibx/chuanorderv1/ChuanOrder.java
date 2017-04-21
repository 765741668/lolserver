
package hk.hkjc.yzh.schema.test.jibx.chuanorderv1;

import hk.hkjc.yzh.schema.test.jibx.common.AbsOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/ChuanOrder_v1" xmlns:ns1="http://www.yzh.hkjc.hk/Schema/Test/Jibx/Common" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="chuanOrder">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns1:absOrder">
 *       &lt;xs:sequence>
 *         &lt;xs:element type="xs:string" name="orderperson"/>
 *         &lt;xs:element type="ns:postaddr" name="chuanto"/>
 *         &lt;xs:element type="ns:chuanItemType" name="item" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:attribute type="xs:string" use="required" name="orderid"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ChuanOrder extends AbsOrder {
    private String orderperson;
    private Postaddr chuanto;
    private List<ChuanItemType> itemList = new ArrayList<ChuanItemType>();
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
     * Get the 'chuanto' element value.
     *
     * @return value
     */
    public Postaddr getChuanto() {
        return chuanto;
    }

    /**
     * Set the 'chuanto' element value.
     *
     * @param chuanto
     */
    public void setChuanto(Postaddr chuanto) {
        this.chuanto = chuanto;
    }

    /**
     * Get the list of 'item' element items.
     *
     * @return list
     */
    public List<ChuanItemType> getItemList() {
        return itemList;
    }

    /**
     * Set the list of 'item' element items.
     *
     * @param list
     */
    public void setItemList(List<ChuanItemType> list) {
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
