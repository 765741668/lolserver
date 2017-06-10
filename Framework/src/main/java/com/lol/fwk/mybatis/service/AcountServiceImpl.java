package com.lol.fwk.mybatis.service;/**
 * Description : 
 * Created by YangZH on 2017/6/8
 *  23:05
 */

import com.lol.fwk.mybatis.bean.Acount;
import com.lol.fwk.mybatis.dao.AcountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description :
 * Created by YangZH on 2017/6/8
 * 23:05
 */
@Service("acountService")
public class AcountServiceImpl implements IAcountService {
    @Autowired
    private AcountMapper acountMapper;

    @Override
    public Acount getAcount(int id) {
        return acountMapper.selectByPrimaryKey(id);
    }
}
