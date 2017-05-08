/******************************************************************************
 *
 * Module Name:  com.lol.jibx - ObjectFactory.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 26, 2016
 * Last Updated By: java
 * Last Updated Date: May 26, 2016
 * Description:
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
package com.lol.demo.jibx;


import com.lol.jibx.chuanorderv1.ChuanItemType;
import com.lol.jibx.chuanorderv1.ChuanOrder;
import com.lol.jibx.chuanorderv1.Country;
import com.lol.jibx.chuanorderv1.Postaddr;
import com.lol.jibx.shiporderv1.CTshiporder;
import com.lol.jibx.shiporderv1.CountryType;
import com.lol.jibx.shiporderv1.Itemtype;
import com.lol.jibx.shiporderv1.Shiptotype;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class ObjectFactory {
    public static CTshiporder createCTshiporder(String orderid) {
        Shiptotype st = new Shiptotype();
        st.setAddress("Shiptotype_address01");
        st.setCountry(CountryType.CH);
        st.setCity("Shiptotype_city01");
        st.setName("Shiptotype_name01");

        Itemtype t1 = new Itemtype();
        t1.setNote("Itemtype_note01");
        t1.setPrice(new BigDecimal(100));
        t1.setQuantity("Itemtype_quantity01");
        t1.setTitle("Itemtype_title01");
        Itemtype t2 = new Itemtype();
        t2.setNote("Itemtype_note02");
        t2.setPrice(new BigDecimal(200));
        t2.setQuantity("Itemtype_quantity02");
        t2.setTitle("Itemtype_title02");

        CTshiporder order = new CTshiporder();
        order.setOrderid(orderid);
        order.setOrderperson("CTshiporder_orderperson01");
        order.setShipto(st);
        order.setItemList(Arrays.asList(t1, t2));
        order.setUpdateTime(new Date("2016-05-31"));

        return order;
    }

    public static ChuanOrder createChuanOrder(String orderid) {
        Postaddr st = new Postaddr();
        st.setAddress("Postaddr_address01");
        st.setCountry(Country.CHINA);
        st.setCity("Postaddr_city01");
        st.setName("Postaddr_name01");

        ChuanItemType t1 = new ChuanItemType();
        t1.setNote("ChuanItemTy_penote01");
        t1.setPrice(new BigDecimal(100));
        t1.setQuantity("ChuanItemType_quantity01");
        t1.setTitle("ChuanItemType_title01");
        ChuanItemType t2 = new ChuanItemType();
        t2.setNote("ChuanItemType_note02");
        t2.setPrice(new BigDecimal(200));
        t2.setQuantity("ChuanItemType_quantity02");
        t2.setTitle("ChuanItemType_title02");

        ChuanOrder order = new ChuanOrder();
        order.setOrderid(orderid);
        order.setOrderperson("ChuanOrderorder_person01");
        order.setChuanto(st);
        order.setItemList(Arrays.asList(t1, t2));
        order.setUpdateTime(new Date("2016-05-31"));

        return order;
    }
}
