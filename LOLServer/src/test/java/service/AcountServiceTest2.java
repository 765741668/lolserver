package service;

import com.lol.fwk.db.ServiceException;
import com.lol.fwk.util.jdbc.JdbcHelper;
import com.lol.fwk.service.acount.IAcountService;
import com.lol.fwk.service.acount.impl.AcountServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class AcountServiceTest2 {


    private IAcountService acountService = null;
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/env/spring/config.xml");
        acountService = applicationContext.getBean(AcountServiceImpl.class);
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