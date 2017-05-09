package com.lol.fwk.dao.acount.impl;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:28
 */

import com.lol.fwk.dao.acount.IAcountDAO;
import com.lol.fwk.dao.bean.acount.Acount;
import com.lol.fwk.db.BaseDao;
import com.lol.fwk.db.DAOException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description :
 * Created by YangZH on 2017/4/17
 * 19:28
 */
@Repository("acountDAOImpl")
public class AcountDAOImpl extends BaseDao<Acount> implements IAcountDAO {

    @Override
    public Acount getAcountByCondiction(Map condiction) throws DAOException {
        StringBuilder hql = new StringBuilder();
        hql.append("from Acount a where 1 = 1 ");
        Object[] param = new Object[1];
        if (!condiction.isEmpty()) {
            String acount = (String) condiction.get("acount");
            if (!StringUtils.isEmpty(acount)) {
                hql.append(" and acount = ? ");
                param[0] = acount;
            }

            String id = (String) condiction.get("id");
            if (!StringUtils.isEmpty(id)) {
                hql.append(" and id = ? ");
                param[0] = id;
            }
        }

        List<Acount> result = this.findByHql(hql.toString(), param);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Acount> queryAcountsByCondiction(Map condiction) throws DAOException {
        StringBuilder hql = new StringBuilder();
        hql.append("from Acount a where 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (!condiction.isEmpty()) {
            String acount = (String) condiction.get("acount");
            if (!StringUtils.isEmpty(acount)) {
                hql.append(" and acount  = ?");
                params.add(acount);
            }
        }

        return this.findByHql(hql.toString(), params.toArray());
    }

    @Override
    public void saveAcount(Acount acount) throws DAOException {
        this.save(acount);
    }

    @Override
    public void updateAcount(Acount acount) throws DAOException {
        this.update(acount);
    }

    @Override
    public int deleteAcount(int id) throws DAOException {
        return this.deleteByHql("delete Acount where id = ?", new Object[]{id});
    }
}
