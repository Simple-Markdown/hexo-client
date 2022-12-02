package live.midreamsheep.hexo.client.monitor;

import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
import live.midreamsheep.hexo.client.data.TextPostfix;
import live.midreamsheep.hexo.client.net.NetToolApi;
import live.midreamsheep.hexo.client.tool.patch.PatchTool;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileListener extends FileAlterationListenerAdaptor {
    public static final String FilePath = new File(Config.nativeHexoPath + Constant.blogPath).getAbsolutePath();
    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        if(Config.isPulling){
            System.out.println("正在拉取，不进行操作");
            return;
        }
        File file = new File(directory.getAbsolutePath().replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
        boolean aFolder = NetToolApi.createAFolder(directory.getAbsolutePath().replace(FilePath, ""));
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void onDirectoryDelete(File directory) {
        if(Config.isPulling){
            return;
        }
        File file = new File(directory.getAbsolutePath().replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
        boolean aFolder = NetToolApi.deleteAFolder(directory.getAbsolutePath().replace(FilePath, ""));
        if(file.exists()){
            boolean delete = file.delete();
            while (!delete){
                delete = file.delete();
            }
        }
    }

    @Override
    public void onFileCreate(File file) {
        if(Config.isPulling){
            System.out.println("正在拉取，不进行操作");
            return;
        }
        String compressedPath = file.getAbsolutePath();
        try {
            NetToolApi.createAFile(compressedPath.replace(FilePath,""), Files.readAllBytes(file.toPath()));
            File cacheFile = new File(compressedPath.replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
            cacheFile.createNewFile();
            Files.copy(file.toPath(),cacheFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFileChange(File blogFile) {
        if(Config.isPulling){
            System.out.println("正在拉取，不进行操作");
            return;
        }
        String compressedPath = blogFile.getAbsolutePath();
        File sourceFile = new File(compressedPath.replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
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

    @Override
    public void onFileDelete(File file) {
        if(Config.isPulling){
            return;
        }
        boolean b = NetToolApi.deleteAFile(file.getAbsolutePath().replace(FilePath, ""));
        if(b){
            new File(file.getAbsolutePath().replace(FilePath,"")).delete();
        }
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}
