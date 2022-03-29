package jemeter;

import com.lol.LoginProtocol;
import com.lol.Protocol;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * Description : lol客户端 jemeter java 登陆采样器
 * Created by YangZH on 2017/5/30
 * 15:02
 */



public class LOLLoginBenchmark extends AbstractJavaSamplerClient {

    private static final Logger log = LoggerFactory.getLogger(LOLLoginBenchmark.class);

    /**
     * setupTest与teardownTest方法，在一个线程只被执行一次，无论此Thread Group配置的Loop Count是多少，每个线程只运行一次这两个方法
     * 为了达到真正的性能测试目的，所有的数据准备工作，应在这里完成。 需要注意的是，这个方法在JMeter初始化的每个线程都会执行一遍，而不是全局仅一遍。
     * @param context
     */
    @Override
    public void setupTest(JavaSamplerContext context)
    {
        log.info(String.format("-----------------从JMeter获取参数-----------------"));
        Iterator<String> it = context.getParameterNamesIterator();
        while (it.hasNext())
        {
            String paramName =  it.next();
            log.info(String.format("-----------------参数名: %s, 参数值: %s-----------------",
                    paramName,
                    context.getParameter(paramName)));
        }

        log.info(String.format("-----------------当前线程Id: %s, 开始初始化 'setupTest(JavaSamplerContext context)'--------------------",
                Thread.currentThread().getId()));
    }

    /**
     * JUnit的tearDown一样，但据我测试的效果来看，这玩意全局只调用一次，而非跟setupTest那样每个线程都来一次。
     * @param context
     */
    @Override
    public void teardownTest(JavaSamplerContext context)
    {
        log.info(String.format("-----------------当前线程Id: %s, 开始初始化 'teardownTest(JavaSamplerContext context)'--------------------",
                Thread.currentThread().getId()));
    }

    /**.
     * runTest方法，如果一个线程配置了多次的Loop Count，那么此runTest就会运行多次
     * @param context
     * 从 context 参数可以获得参数值；
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sp = new SampleResult(); //采样结果
        MessageDownProto.MessageDown resp;
        sp.sampleStart();                                    //采用开始时间

        //下面是客户端逻辑
//        try {
//            if("PC2-ID".equalsIgnoreCase(benchmark_mode)){
//                resp = client.sendMatchRequest(
//                        key,
//                        ENGINE.code(),
//                        pc2Image,
//                        sourcePositions,
//                        targetIdentifiers,
//                        ImageType.PC2.imageCode());
//            }else{
//                log.error("当前模块不是登陆模块");
//                throw new java.lang.IllegalArgumentException("当前模块不是登陆模块");
//            }
//
//            sp.sampleEnd(); //采用结束
//
////            this.printResponse(resp);
//
//            //set success flag to true but need to verify later
//            sp.setSuccessful(true);
//
//            //下面进一步分析服务器端返回的信息，以判断本次交易是否成功
//            int matchResults = resp.getBody().getLogin().getResult();
//            if(matchResults == 0){
//                sp.setSuccessful(true);
//            }else{
//                sp.setSuccessful(false);
//            }
//        } catch (Exception e) {
//            sp.sampleEnd();
//            sp.setSuccessful(false);
//            e.printStackTrace();
//            return sp;
//        }

        return sp;  //返回采用结果，这个结果将会被JMeter使用，并反馈到GUI/报告中，方便明了
    }

    private void printResponse(MessageDownProto.MessageDown resp) {
        String respStr = "";
        switch (resp.getHeader().getCmd()){
            case LoginProtocol.REG_SRES:
                switch (resp.getBody().getLogin().getResult()){
                    case 1:
                        respStr = "账号已存在，创建新账号失败";
                        break;
                    default:
                        respStr = "创建新账号成功";
                        break;
                }
                log.info("收到服务器注册响应: {}",respStr);
                break;
            case LoginProtocol.LOGIN_SRES:
                switch (resp.getBody().getLogin().getResult()){
                    case -4:
                        respStr = "账号或密码为空，输入不合法";
                        break;
                    case -1:
                        respStr = "账号不存在，拒绝登陆";
                        break;
                    case -2:
                        respStr = "当前账号已经在线";
                        break;
                    case -3:
                        respStr = "账号或密码不正确";
                        break;
                    default:
                        respStr = "登陆成功";
                }

                log.info("收到服务器登陆响应: {}",respStr);
                break;
        }
    }


    /**
     * 在这个方法里，可以设置各种参数，这些参数可以从GUI处获取，也可以设置合适的默认值。有意思的是，
     * 这些默认值在GUI扫描的时候，会自动出现在界面上，以供必要的调整和优化。
     * @return
     */
    @Override
    public Arguments getDefaultParameters() {

        Arguments params = new Arguments();

        params.addArgument("msgType", Protocol.TYPE_LOGIN+"");
        params.addArgument("area", Protocol.TYPE_LOGIN+"");
        params.addArgument("cmd", LoginProtocol.LOGIN_CREQ+"");
        params.addArgument("acount", "yzh");
        params.addArgument("password", "123456");

        return params;
    }

}