/******************************************************************************
 *
 * Module Name:  entity.xiangyuan - XiangyuanFactory.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: Apr 22, 2016
 * Last Updated By: randyzhyang
 * Last Updated Date: Apr 22, 2016
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
package com.lol.demo.entity.design.xiangyuan;

import java.util.HashMap;
import java.util.Map;

public class XiangyuanReportFactory {
    Map<String, IXiangYuan> reportManager = new HashMap<>();
    Map<String, IXiangYuan> reportManager2 = new HashMap<>();

    public IXiangYuan getReportManager(String tenantId) {
        IXiangYuan r = reportManager.get(tenantId);
        if (r == null) {
            r = new XiangYuanReportImpl(tenantId);
            reportManager.put(tenantId, r);
            System.out.println("new a reportManager");
        }
        return r;
    }

    public IXiangYuan getReportManager2(String tenantId) {
        IXiangYuan r = reportManager2.get(tenantId);
        if (r == null) {
            r = new XiangYuanReportImpl2(tenantId);
            reportManager2.put(tenantId, r);
            System.out.println("new a reportManager2");
        }
        return r;
    }
}
