package live.midreamsheep.hexo.client.monitor;


import live.midreamsheep.hexo.netapi.data.Constant;
import live.midreamsheep.hexo.netapi.data.TextPostfix;
import live.midreamsheep.hexo.netapi.hand.net.ConnectorConfig;
import live.midreamsheep.hexo.netapi.hand.net.NetToolApi;
import live.midreamsheep.hexo.netapi.tool.patch.PatchTool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ListenerApi {
    public static final String FilePath = new File(ConnectorConfig.nativeHexoPath + Constant.blogPath).getAbsolutePath();

    public static void directoryCreate(File directory,boolean isPush) {
        if(ConnectorConfig.isPulling&&!isPush){
            return;
        }
        File file = new File(directory.getAbsolutePath().replace(FilePath, ConnectorConfig.nativeHexoPath + Constant.cachePath));
        boolean aFolder = NetToolApi.createAFolder(directory.getAbsolutePath().replace(FilePath, ""));
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void directoryDelete(File directory,boolean isPush) {
        if(ConnectorConfig.isPulling&&!isPush){
            return;
        }
        File file = new File(directory.getAbsolutePath().replace(FilePath, ConnectorConfig.nativeHexoPath + Constant.cachePath));
        boolean aFolder = NetToolApi.deleteAFolder(directory.getAbsolutePath().replace(FilePath, ""));
        if(file.exists()){
            boolean delete = file.delete();
            while (!delete){
                delete = file.delete();
            }
        }
    }

    public static void fileCreate(File file,boolean isPush) {
        if(ConnectorConfig.isPulling&&!isPush){
            return;
        }
        String compressedPath = file.getAbsolutePath();
        try {
            NetToolApi.createAFile(compressedPath.replace(FilePath,""), Files.readAllBytes(file.toPath()));
            File cacheFile = new File(compressedPath.replace(FilePath, ConnectorConfig.nativeHexoPath + Constant.cachePath));
            if(!cacheFile.getParentFile().exists()){
                cacheFile.getParentFile().mkdirs();
            }
            cacheFile.createNewFile();
            Files.copy(file.toPath(),cacheFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fileChange(File blogFile,boolean isPush) {
        if(ConnectorConfig.isPulling&&!isPush){
            return;
        }
        String compressedPath = blogFile.getAbsolutePath();
        File sourceFile = new File(compressedPath.replace(FilePath, ConnectorConfig.nativeHexoPath + Constant.cachePath));
        //原始文件
        if(sourceFile.exists()){
            if(TextPostfix.set.contains(compressedPath.substring(compressedPath.lastIndexOf(".")))&&sourceFile.length()>0) {
                List<String> compare = PatchTool.compare(blogFile.toPath(), sourceFile.toPath());
                //获取了差异文件
                if (compare.size() > 0) {
                    //上传文件
                    boolean b = NetToolApi.updateAFile(compressedPath.replace(FilePath, ""), compare);
                    //更新缓存文件
                }
            }else {
                try {
                    NetToolApi.deleteAFile(compressedPath.replace(FilePath, ""));
                    NetToolApi.createAFile(compressedPath.replace(FilePath, ""), Files.readAllBytes(blogFile.toPath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Files.copy(blogFile.toPath(),sourceFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void fileDelete(File file,boolean isPush) {
        if(ConnectorConfig.isPulling&&!isPush){
            return;
        }
        boolean b = NetToolApi.deleteAFile(file.getAbsolutePath().replace(FilePath, ""));
        new File(file.getAbsolutePath().replace(FilePath,ConnectorConfig.nativeHexoPath+File.separator+Constant.cachePath)).delete();
    }

}
