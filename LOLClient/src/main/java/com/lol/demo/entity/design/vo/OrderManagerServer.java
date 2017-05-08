/******************************************************************************
 *
 * Module Name:  entity.design.vo - OrderManagerServer.java
 * Version: 1.0.0
 * Original Author: randyzhyang
 * Created Date: May 4, 2016
 * Last Updated By: randyzhyang
 * Last Updated Date: May 4, 2016
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
package com.lol.demo.entity.design.vo;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class OrderManagerServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            IOrderManger userManager = new OrderManager();
            try {
                Naming.rebind("OrderManager", userManager);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("OrderManager Server is ready!");
        } catch (Exception e) {
            System.err.println("OrderManager Server failed: " + e);
        }

    }
}
