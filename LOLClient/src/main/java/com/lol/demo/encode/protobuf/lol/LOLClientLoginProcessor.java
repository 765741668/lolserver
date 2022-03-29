package com.lol.demo.encode.protobuf.lol;

import com.lol.LoginProtocol;
import com.lol.demo.encode.protobuf.MessageDownProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LOLClientLoginProcessor extends BaseProcessor {

    private Logger logger = LoggerFactory.getLogger(LOLClientLoginProcessor.class);

    @Override
    public void process(MessageDownProto.MessageDown resp) {
        String respStr = "";
        switch (resp.getHeader().getCmd()){
            case LoginProtocol.REG_SRES:
                switch (resp.getBody().getLogin().getResult()){
                    case 1:
                        respStr = "账号已存在，创建新账号失败";
                        break;
                    default:
                        respStr = "创建新账号成功";
                        break;
                }
                logger.info("收到服务器注册响应: {}",respStr);
                break;
            case LoginProtocol.LOGIN_SRES:
                switch (resp.getBody().getLogin().getResult()){
                    case -4:
                        respStr = "账号或密码为空，输入不合法";
                        break;
                    case -1:
                        respStr = "账号不存在，拒绝登陆";
                        break;
                    case -2:
                        respStr = "当前账号已经在线";
                        break;
                    case -3:
                        respStr = "账号或密码不正确";
                        break;
                    default:
                        respStr = "登陆成功";
                }

                logger.info("收到服务器登陆响应: {}",respStr);
                break;
        }
    }
}