package mybatis;/**
 * Description : 
 * Created by YangZH on 2017/6/8
 *  22:56
 */

import com.lol.fwk.mybatis.bean.Acount;
import com.lol.fwk.mybatis.dao.AcountMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description :
 * Created by YangZH on 2017/6/8
 * 22:56
 */

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath*:env/spring/config.xml"})
public class TestMyBatis {
    @Autowired
    private AcountMapper acountService;

    @Test
    public void test1() {
        Acount user = acountService.selectByPrimaryKey(1);
        System.out.println(user.getAcount());
    }
}
