package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.net.Connector;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Server implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        //启动实时预览功能
        try {
            Connector.socketChannel.write(ByteBuffer.wrap(new byte[]{(byte) HandlerEnum.SERVER.getId()}));
            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            Connector.socketChannel.read(byteBuffer);
            byte[] array = byteBuffer.array();
            if(array[0]==(byte) 0x01){
                System.out.println("服务端已经启动了实时预览功能");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
