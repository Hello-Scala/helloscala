package com.helloscala;

import cn.dev33.satoken.SaManager;
import com.dtflys.forest.springboot.annotation.ForestScan;
import com.helloscala.web.im.WebSocketChanneInitializer;
import com.helloscala.web.im.WebSocketConstant;
import com.helloscala.web.im.WebSocketInfoService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.dromara.easyes.starter.register.EsMapperScan;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EnableAsync
@EnableFileStorage
@RestController
@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = {"com.helloscala.common.mapper"})
@ForestScan(basePackages = "com.helloscala.web.service.client")
@EsMapperScan("com.helloscala.common.esmapper")
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        LOGGER.info("\n----------------------------------------------------------\n\t" +
            "blog is running! Access URLs:\n\t" +
            "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
            "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
            "Api documents: \thttp://" + ip + ":" + port + path + "/swagger-ui/index.html\n\t" +
            "----------------------------------------------------------");
        LOGGER.info("Sa-token config:" + SaManager.getConfig());
//        startNettyMsgServer();
    }

    private static void startNettyMsgServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new WebSocketChanneInitializer());
            System.out.println("Server started, waiting for client to connect....");
            Channel ch = b.bind(WebSocketConstant.WEB_SOCKET_PORT).sync().channel();

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
            WebSocketInfoService webSocketInfoService = new WebSocketInfoService();
            executorService.scheduleAtFixedRate(webSocketInfoService::scanNotActiveChannel,
                3, 60, TimeUnit.SECONDS);

            executorService.scheduleAtFixedRate(webSocketInfoService::sendPing,
                3, 10, TimeUnit.SECONDS);

            ch.closeFuture().sync();


        } catch (Exception e) {
            LOGGER.error("Failed to start netty server!", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @GetMapping("/")
    public String home() {
        return "Server started!";
    }
}
