package live.midreamsheep.hexo.client.tool.io;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SIO {
    public static String read(String path){
        try (InputStream is = Files.newInputStream(Paths.get(path))) {
            StringBuilder sb = new StringBuilder();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return sb.toString();
        }catch (Exception e){
            System.err.println("文件读取失败");
            throw new RuntimeException(e);
        }
    }
}
