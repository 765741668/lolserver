/******************************************************************************
 *
 * Module Name:  com.lol.jibx - ClazzGenerate.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: May 30, 2016
 * Last Updated By: java
 * Last Updated Date: May 30, 2016
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

import com.lol.jibx.shiporderv1.CTshiporder;
import org.jibx.binding.BindingGenerator;

public class ClazzGenerate {
    public static void main(String[] args) {
        String[] arg = {"-b", "binding_test.xml", CTshiporder.class.toString()};
        BindingGenerator.main(arg);
    }

}
