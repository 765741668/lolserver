/******************************************************************************
 *
 * Module Name:  com.lol.com.lol.demo.encode.protobuf - TestV3.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Jun 16, 2016
 * Last Updated By: java
 * Last Updated Date: Jun 16, 2016
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
package com.lol.demo.encode.protobuf;

import com.google.protobuf.ByteString;
import com.lol.demo.encode.protobuf.TestV3Proto.TestV3;
import com.lol.demo.encode.protobuf.TestV3Proto.TestV3.Humour;

public class Test_V3 {

    public static void main(String[] args) {
        //如前所述，不能直接构造该消息类对象，只能通过他的内部Builder类构造并完成所有字段的初始化。
        TestV3Proto.TestV3.Builder tBuilder = TestV3Proto.TestV3.newBuilder();
        TestV3Proto.TestV3Array.Builder taBuilder = TestV3Proto.TestV3Array.newBuilder();

        tBuilder.setName("YZH");
        tBuilder.setHilarity(Humour.BILL_BAILEY);
        tBuilder.setHeightInCm(123);
        tBuilder.setData(ByteString.copyFromUtf8("data"));
        tBuilder.setResultCount(123L);
        tBuilder.setTrueScotsman(false);
        tBuilder.setScore(123F);
        tBuilder.addKey(123L).addKey(1234L);
        tBuilder.getMutableEnummap().put("1", Humour.UNKNOWN);


        taBuilder.addEnumlist(tBuilder.build());
        //builder对象初始化完毕后，再通过build方法生成与之对应的消息类对象。
        TestV3 t3 = tBuilder.build();
        taBuilder.getMutableObjmmap().put("TestV3_Map", t3);

    }

}
