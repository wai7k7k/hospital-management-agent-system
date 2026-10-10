package xian.woniuxy.customer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author: song
 * @date: 2022-08-22 17:35:19
 * @desc: TODO
 */
@EnableDubbo
@SpringBootApplication
@MapperScan("xian.woniuxy.customer.mapper")
public class CustomerApp {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CustomerApp.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  病人服务模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
