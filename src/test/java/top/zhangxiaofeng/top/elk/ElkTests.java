package top.zhangxiaofeng.top.elk;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhangxiaofeng
 * @Date 2021/8/19
 */
@SpringBootTest
public class ElkTests {

    private final static Logger logger = LoggerFactory.getLogger(ElkTests.class);

    @Test
    public void log() {
        //日志的级别
        //从上到下---由低到高
        logger.trace("我是一个trace");
        logger.debug("我是一个debug");
        logger.info("我是一个info");
        logger.warn("我是一个warn");
        logger.error("我是一个error");
    }

}
