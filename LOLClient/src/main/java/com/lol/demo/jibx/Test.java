/******************************************************************************
 *
 * Module Name:  com.lol.jibx - Test.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 25, 2016
 * Last Updated By: java
 * Last Updated Date: May 25, 2016
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

import com.lol.demo.nio.NioUtil;
import com.lol.jibx.chuanorderv1.ChuanOrder;
import com.lol.jibx.common.AbsOrder;
import com.lol.jibx.shiporderv1.CTshiporder;
import org.jibx.runtime.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Paths;

public class Test {
    private IBindingFactory ibf = null;

    public static void main(String[] args) throws JiBXException, IOException {
        Test test = new Test();
        CTshiporder order = ObjectFactory.createCTshiporder("CTshiporder_orderid01");
        System.out.println("original : " + order);
        String xmlbody = test.encode2Xml(order);
        CTshiporder decode_order = (CTshiporder) test.decode2Xml(xmlbody);
        System.out.println("decode : " + decode_order);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        ChuanOrder order2 = ObjectFactory.createChuanOrder("ChuanOrder_orderid02");
        System.out.println("original2 : " + order2);
        String xmlbody2 = test.encode2Xml(order2);
        ChuanOrder decode_order2 = (ChuanOrder) test.decode2Xml(xmlbody2);
        System.out.println("decode2 : " + decode_order2);

    }

    private String encode2Xml(AbsOrder order) throws JiBXException, IOException {
        ibf = BindingDirectory.getFactory(order.getClass());
        StringWriter sw = new StringWriter();
        IMarshallingContext ic = ibf.createMarshallingContext();
        ic.setIndent(2);
        ic.marshalDocument(order, "UTF-8", null, sw);
        String xmlbody = sw.toString();
        sw.close();
        String path = new File("").getAbsolutePath() + "/src/main/resources/schema/xml";
        new NioUtil().writeByFilesAeadAllLines(Paths.get(path), order.getClass().getSimpleName() + ".xml", xmlbody.split("\n"));
        System.out.println(xmlbody);
        return xmlbody;
    }

    private AbsOrder decode2Xml(String xmlbody) throws JiBXException {
        StringReader sr = new StringReader(xmlbody);
        IUnmarshallingContext uc = ibf.createUnmarshallingContext();
        return (AbsOrder) uc.unmarshalDocument(sr);
    }

}