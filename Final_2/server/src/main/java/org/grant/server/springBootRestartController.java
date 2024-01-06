package org.grant.server;
import org.grant.server.dto.serverConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@RestController
@CrossOrigin(origins = "*")
public class springBootRestartController {
    private ConfigurableApplicationContext context;

    public springBootRestartController(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @PostMapping("/restart")
    public String restartApp() {
        Thread restartThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // 等待一秒以响应请求
                context.close();
                Thread.sleep(5000); // 等待一秒以响应请求

                serverConfiguration.getPublicIP();
                SpringApplication.run(ServerApplication.class);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        restartThread.setDaemon(false);
        restartThread.start();
        return "应用正在重启";
    }

}
