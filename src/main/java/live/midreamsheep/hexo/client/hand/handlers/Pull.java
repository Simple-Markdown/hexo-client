package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.net.Connector;
import live.midreamsheep.hexo.client.net.NetToolApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Pull implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        Config.isPulling = true;
        try {
            NetToolApi.sendMeta(HandlerEnum.PULL.getId(),0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(3);
                //读取元数据 3个字节 0为是否存在文件 1 2为长度
                Connector.socketChannel.read(byteBuffer);
                byte[] bytes = byteBuffer.array();
                if (bytes[0] != 1) {
                    break;
                }
                //接受文件元数据
                int length = bytes[1] << 8 | (bytes[2] & 0xff);
                ByteBuffer allocate = ByteBuffer.allocate(length);
                //读取元数据 前4个字节为文件长度 后面为文件名
                Connector.socketChannel.read(allocate);

                byte[] array = allocate.array();
                int fileLength = array[0] << 24 | (array[1] & 0xff) << 16 | (array[2] & 0xff) << 8 | (array[3] & 0xff);
                String fileName = new String(array, 4, length - 4);

                //本地文件大小比较 大小相同则是同一个文件
                File nativeFile = new File(Config.nativeHexoPath+File.separator+ Constant.blogPath +File.separator+fileName);
                System.out.println(nativeFile.length()+" "+fileLength);
                byte[] bytes1 = new byte[1];
                if(nativeFile.length() == fileLength&&nativeFile.exists()){
                    //写出文件相同命令
                    bytes1[0] = 0x01;
                    System.out.println(nativeFile.getAbsolutePath()+"文件相同");
                    Connector.socketChannel.write(ByteBuffer.wrap(bytes1));
                    continue;
                }
                System.out.println(Config.nativeHexoPath+File.separator+fileName);
                //写出文件不相同命令
                bytes1[0] = 2;
                Connector.socketChannel.write(ByteBuffer.wrap(bytes1));
                System.out.println(fileLength);
                System.out.println("数据开始读取");
                //读取文件数据
                ByteBuffer fileBuffer = ByteBuffer.allocate(1024);
                int readLength = 0;
                if(!nativeFile.getParentFile().exists()){
                    nativeFile.getParentFile().mkdirs();
                }
                Path path = nativeFile.toPath();
                try (OutputStream outputStream = Files.newOutputStream(path)) {
                    while (readLength < fileLength) {
                        int read = Connector.socketChannel.read(fileBuffer);
                        readLength += read;
                        fileBuffer.flip();
                        byte[] array1 = fileBuffer.array();
                        fileBuffer.clear();
                        outputStream.write(array1, 0, read);
                    }
                }

                //输出
                //输出到workplace
                File file = new File(Config.nativeHexoPath+File.separator+Constant.cachePath + File.separator + fileName);
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                Files.copy(path,file.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("数据写出 总大小"+(fileLength+length+3));
                byteBuffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Config.isPulling = false;
    }
}
