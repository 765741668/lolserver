package com.lol.fwk.dao.player.impl;/**
 * Description : 
 * Created by YangZH on 2017/4/17
 *  19:27
 */

import com.lol.fwk.dao.bean.player.Player;
import com.lol.fwk.dao.player.IPlayerDAO;
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
 * 19:27
 */
@Repository("playerDAOImpl")
public class PlayerDAOImpl extends BaseDao<Player> implements IPlayerDAO {

    @Override
    public Player getPlayerByCondiction(Map condiction) throws DAOException {
        StringBuilder hql = new StringBuilder();
        hql.append("from Player a where 1 = 1 ");
        Object[] param = new Object[1];
        if (!condiction.isEmpty()) {
            Object acountId = condiction.get("acountId");
            if (acountId != null) {
                hql.append(" and acountId = ? ");
                param[0] = acountId;
            }

            Object id = condiction.get("id");
            if (id != null) {
                hql.append(" and id = ? ");
                param[0] = id;
            }
        }

        List<Player> result = this.findByHql(hql.toString(), param);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<Player> queryPlayersByCondiction(Map condiction) throws DAOException {
        StringBuilder hql = new StringBuilder();
        hql.append("from Player a where 1 = 1 ");
        List<Object> params = new ArrayList<>();
        if (!condiction.isEmpty()) {
            String acountId = (String) condiction.get("acountId");
            if (!StringUtils.isEmpty(acountId)) {
                hql.append(" and acountId  = ?");
                params.add(acountId);
            }

            String id = (String) condiction.get("id");
            if (!StringUtils.isEmpty(id)) {
                hql.append(" and id  = ?");
                params.add(id);
            }
        }

        return this.findByHql(hql.toString(), params.toArray());
    }

    @Override
    public void savePlayer(Player player) throws DAOException {
        this.save(player);
    }

    @Override
    public void updatePlayer(Player player) throws DAOException {
        this.update(player);
    }

    @Override
    public void deletePlayer(int id) throws DAOException {
        this.deleteByHql("delete Player where id = ?", new Object[]{id});
    }
}
