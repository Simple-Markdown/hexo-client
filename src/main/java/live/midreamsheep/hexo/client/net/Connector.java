package live.midreamsheep.hexo.client.net;

import live.midreamsheep.hexo.client.config.Config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Connector {
    public static SocketChannel socketChannel;
    public static void init(){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(Config.toIp, Config.toPort));
            //传输密码
            byte[] bytes = new byte[2];
            byte[] password = Config.password.getBytes();
            //密码长度
            bytes[0] = (byte) (password.length >> 8);
            bytes[1] = (byte) (password.length);
            socketChannel.write(ByteBuffer.wrap(bytes));
            socketChannel.write(ByteBuffer.wrap(password));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
