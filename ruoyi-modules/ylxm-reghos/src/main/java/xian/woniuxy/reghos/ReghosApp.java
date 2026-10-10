package xian.woniuxy.reghos;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @author: song
 * @date: 2022-08-22 11:47:05
 * @desc: TODO
 */
@EnableDubbo
@SpringBootApplication
@MapperScan("xian.woniuxy.reghos.mapper")
public class ReghosApp {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ReghosApp.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  就诊挂号启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
