/******************************************************************************
 *
 * Module Name:  entity.design.vo - OrderManager.java
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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OrderManager extends UnicastRemoteObject implements IOrderManger {

    private static final long serialVersionUID = -6369936343408955088L;

    protected OrderManager() throws RemoteException {
        super();
    }

    @Override
    public Order getOrder(int id) {
        return null;
    }


}
