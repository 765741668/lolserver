/******************************************************************************
 *
 * Module Name:  entity - XiangYuanImpl.java
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

public class XiangYuanReportImpl2 implements IXiangYuan {
    private String tenantId = null;

    public XiangYuanReportImpl2(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String createReport() {
        return "this is a financial report2 , tenantId = " + tenantId;
    }

}
