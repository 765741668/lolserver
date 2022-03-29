package com.lol.rpc;

public class RpcClient {
    public static void main(String [] args){  
        HelloRpc helloRpc = new HelloRpcImpl();
        helloRpc = RPCProxy.create(helloRpc);
        System.err.println(helloRpc.hello("rpc"));
    }
}  