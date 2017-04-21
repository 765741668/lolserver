package com.lol.procesors;/**
 * Description : 
 * Created by YangZH on 2017/4/6
 *  20:11
 */

import com.lol.LoginProtocol;
import com.lol.buffer.GameDownBuffer;
import com.lol.buffer.GameUpBuffer;
import com.lol.dto.AcountInfoDTO;
import com.lol.handler.GameProcessor;
import com.lol.protobuf.MessageDownProto;
import com.lol.service.IAcountService;
import com.lol.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description :
 * Created by YangZH on 2017/4/6
 * 20:11
 */
@Component
public class LoginProcesor implements GameProcessor {

    @Autowired
    private IAcountService acountService;

    @Override
    public void process(GameUpBuffer buffer) throws Exception {
        AcountInfoDTO acountDTO = buffer.getAcountInfo();
        int result = 0;
        String acount = acountDTO.getacount();
        String password = acountDTO.getPassword();
        switch (buffer.getCmd()) {
            case LoginProtocol.LOGIN_CREQ:
                result = acountService.login(buffer.getConnection(), acount, password);
                break;
            case LoginProtocol.REG_CREQ:
                result = acountService.create(buffer.getConnection(), acount, password);
                break;
            case LoginProtocol.LOGIN_FAIL:
                acountService.close(buffer.getConnection(), acount);
                break;
        }

        MessageDownProto.LoginDownBody.Builder login = MessageDownProto.LoginDownBody.newBuilder();
        login.setResult(result);

        GameDownBuffer message = Utils.packgeDownData(buffer.getMsgType(), buffer.getArea(), LoginProtocol.LOGIN_SRES, login);
        buffer.getConnection().writeDown(message);
    }
}
