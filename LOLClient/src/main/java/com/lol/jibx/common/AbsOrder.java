
package com.lol.jibx.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="http://www.yzh.hkjc.hk/Schema/Test/Jibx/Common" xmlns:xs="http://www.w3.org/2001/XMLSchema" abstract="true" name="absOrder">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:dateTime" name="updateTime"/>
 *     &lt;xs:element type="xs:boolean" name="deleted"/>
 *     &lt;xs:element type="xs:int" name="id"/>
 *     &lt;xs:element type="ns:maptype" name="map" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public abstract class AbsOrder
{
    private Date updateTime;
    private boolean deleted;
    private int id;
    private List<Maptype> mapList = new ArrayList<Maptype>();

    /** 
     * Get the 'updateTime' element value.
     * 
     * @return value
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /** 
     * Set the 'updateTime' element value.
     * 
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
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

    /** 
     * Get the 'id' element value.
     * 
     * @return value
     */
    public int getId() {
        return id;
    }

    /** 
     * Set the 'id' element value.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /** 
     * Get the list of 'map' element items.
     * 
     * @return list
     */
    public List<Maptype> getMapList() {
        return mapList;
    }

    /** 
     * Set the list of 'map' element items.
     * 
     * @param list
     */
    public void setMapList(List<Maptype> list) {
        mapList = list;
    }
}
