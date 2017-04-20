package com.lol.dao;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:26
 */

import com.lol.dao.bean.Acount;
import com.lol.db.DAOException;

import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 2017/4/17
 * 19:26
 */

public interface IAcountDAO {

    /**
     * 根据条件集合得到一个账户实体对象
     *
     * @param condiction
     * @return
     */
    Acount getAcountByCondiction(Map condiction) throws DAOException;

    /**
     * 根据条件集合得到 账户对象列表
     *
     * @param condiction
     * @return
     */
    List<Acount> queryAcountsByCondiction(Map condiction) throws DAOException;

    /**
     * 保存账户对象
     *
     * @param acount
     */
    void saveAcount(Acount acount) throws DAOException;

    /**
     * 更新账户对象
     *
     * @param acount
     * @return
     */
    void updateAcount(Acount acount) throws DAOException;

    /**
     * 根据ID删除账户对象
     *
     * @param id
     * @return
     */
    int deleteAcount(int id) throws DAOException;
}
