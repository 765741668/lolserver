package service;

import com.lol.fwk.exception.ServiceException;
import com.lol.fwk.service.IAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:env/spring/config.xml"})
public class AcountServiceTest {
    @Autowired
    private IAccountService acountService;

    @Test
    public void registerAcount() throws ServiceException {
        System.out.println(new StandardPasswordEncoder().encode("123456"));
        acountService.create("test1", new StandardPasswordEncoder().encode("123456"));
    }

}