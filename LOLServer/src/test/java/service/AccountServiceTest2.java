package service;

import com.lol.fwk.exception.ServiceException;
import com.lol.fwk.service.IAccountService;
import com.lol.fwk.service.impl.AccountServiceImpl;
import com.lol.fwk.util.jdbc.JdbcHelper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class AccountServiceTest2 {


    private IAccountService acountService = null;
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/env/spring/config.xml");
        acountService = applicationContext.getBean(AccountServiceImpl.class);
    }

    @Test
    public void registerAcount() throws ServiceException {
        System.out.println(new StandardPasswordEncoder().encode("123456"));
        acountService.create("test1", "123456");
    }

    @Test
    public void registerAcount2() throws Exception {
        JdbcHelper.insertWithReturnPrimeKey("insert into acount VALUES(2,'yzh','123456')");
    }



}