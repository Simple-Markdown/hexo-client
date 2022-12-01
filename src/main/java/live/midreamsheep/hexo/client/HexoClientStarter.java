package live.midreamsheep.hexo.client;

import live.midreamsheep.command.ApplicationStarter;
import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
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
        //命令注入
        //命令行启动
        ApplicationStarter.main(args);
    }
}