package live.midreamsheep.hexo.client.net;

import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class NetToolApi {
    public static void sendMeta(int id,int length) throws IOException {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) id;
        bytes[1] = (byte) (length >> 24);
        bytes[2] = (byte) (length >> 16);
        bytes[3] = (byte) (length >> 8);
        bytes[4] = (byte) (length);
        Connector.socketChannel.write(ByteBuffer.wrap(bytes));
    }
    public static boolean updateAFile(String path, List<String> patch){
        StringBuilder sb = new StringBuilder();
        sb.append(path);
        sb.append("\n");
        for (String s : patch) {
            sb.append(s);
            sb.append("\n");
        }
        try {
            NetToolApi.sendMeta(HandlerEnum.UPDATE_FILE.getId(),sb.toString().getBytes().length);
            Connector.socketChannel.write(ByteBuffer.wrap(sb.toString().getBytes()));
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean deleteAFile(String path){
        HandlerMapper.handlerMap.get(HandlerEnum.DELETE_FILE.getId()).handle(path.getBytes());
        return true;
    }
    public static boolean createAFile(String path, byte[] content){
        String sb = path + "\n";
        byte[] pathBytes = sb.getBytes();
        byte[] bytes = new byte[pathBytes.length + content.length];
        System.arraycopy(pathBytes,0,bytes,0,pathBytes.length);
        System.arraycopy(content,0,bytes,pathBytes.length,content.length);
        HandlerMapper.handlerMap.get(HandlerEnum.ADD_FILE.getId()).handle(bytes);
        return true;
    }
    public static boolean createAFolder(String path){
        HandlerMapper.handlerMap.get(HandlerEnum.ADD_DIR.getId()).handle(path.getBytes());
        return true;
    }
    public static boolean deleteAFolder(String path){
        HandlerMapper.handlerMap.get(HandlerEnum.DELETE_DIR.getId()).handle(path.getBytes());
        return true;
    }
}
