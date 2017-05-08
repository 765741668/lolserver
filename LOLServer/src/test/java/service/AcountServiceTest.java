package service;

import com.lol.dao.IAcountDAO;
import com.lol.dao.bean.Acount;
import com.lol.db.DAOException;
import com.lol.db.ServiceException;
import com.lol.service.IAcountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:env/spring/config.xml"})
public class AcountServiceTest {
    @Autowired
    private IAcountService acountService;

    @Autowired
    private IAcountDAO acountDAO;

    @Test
    public void registerAcount() throws ServiceException {
        System.out.println(new StandardPasswordEncoder().encode("123456"));
        acountService.create("test1", new StandardPasswordEncoder().encode("123456"));
    }

    @Test
    public void registerAcount2() throws DAOException {
        Acount acount = new Acount();
        acount.setAcount("test2");
        acount.setPassword(new StandardPasswordEncoder().encode("123456"));
        acountDAO.saveAcount(acount);
    }

    @Test
    public void getAcount() throws DAOException {
        Map map = new HashMap<>();
        map.put("acount","yzh");
        Acount acount = acountDAO.getAcountByCondiction(map);
        System.out.println(acount.toString());
    }

}