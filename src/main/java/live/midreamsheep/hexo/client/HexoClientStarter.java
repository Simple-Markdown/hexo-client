package live.midreamsheep.hexo.client;

import live.midreamsheep.command.ApplicationStarter;
import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
import live.midreamsheep.hexo.client.message.queue.QueueApi;
import live.midreamsheep.hexo.client.message.queue.Task;
import live.midreamsheep.hexo.client.monitor.FileListener;
import live.midreamsheep.hexo.client.monitor.MonitorStarter;
import live.midreamsheep.hexo.client.net.Connector;
import live.midreamsheep.hexo.client.tool.io.SIO;

import java.io.File;

/**
 * @author midreamsheep
 * 启动器
 * */
public class HexoClientStarter {
    public static void main(String[] args) throws Exception {
        //配置文件读取
        Config.setConfig(SIO.read(System.getProperty("user.dir") + "\\config.properties"));
        //连接建立
        Connector.init();
        //开始监听
        MonitorStarter monitorStarter = new MonitorStarter(1000);
        monitorStarter.monitor(new File(Config.nativeHexoPath+ Constant.blogPath).getPath(), new FileListener());
        monitorStarter.start();
        //开启守护线程监听任务
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!QueueApi.isEmpty()) {
                    Task task = QueueApi.getTask();
                    task.getHandler().handle(task.getDatas());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        //命令注入
        CommandStarter.start();
        //命令行启动
        ApplicationStarter.main(args);
    }
}