package live.midreamsheep.hexo.client.config;

/**
 * @author midreamsheep
 * 配置文件
 * */
public class Config {
    //本地markdown路径
    public static String nativeHexoPath;
    //服务器地址
    public static String toIp;
    //服务器端口
    public static int toPort;
    //服务器通行密码
    public static String password;

    public static void setConfig(String config){
        for (String s : config.split("\n")) {
            if(s.trim().startsWith("#")){
                continue;
            }
            String[] split = s.split("=");
            switch (split[0]){
                case "nativeHexoPath":
                    nativeHexoPath = split[1].trim();
                    break;
                case "toIp":
                    toIp = split[1].trim();
                    break;
                case "toPort":
                    toPort = Integer.parseInt(split[1].trim());
                    break;
                case "password":
                    password = split[1].trim();
                    break;
                default:
                    System.err.println("无效的配置项"+split[0]);
                    break;
            }
        }
    }
}
