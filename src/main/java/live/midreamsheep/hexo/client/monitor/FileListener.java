package live.midreamsheep.hexo.client.monitor;

import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
import live.midreamsheep.hexo.client.net.NetToolApi;
import live.midreamsheep.hexo.client.tool.patch.PatchTool;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

public class FileListener extends FileAlterationListenerAdaptor {
    public static final String FilePath = new File(Config.nativeHexoPath + Constant.blogPath).getAbsolutePath();
    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        File file = new File(directory.getAbsolutePath().replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
        boolean aFolder = NetToolApi.createAFolder(directory.getAbsolutePath().replace(FilePath, ""));
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void onDirectoryDelete(File directory) {
        File file = new File(directory.getAbsolutePath().replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
        boolean aFolder = NetToolApi.deleteAFolder(directory.getAbsolutePath().replace(FilePath, ""));
        if(file.exists()){
            boolean delete = file.delete();
            if(!delete){
                throw new RuntimeException("删除文件夹失败");
            }
        }
    }

    @Override
    public void onFileCreate(File file) {
        String compressedPath = file.getAbsolutePath();
        try {
            NetToolApi.createAFile(compressedPath.replace(FilePath+"\\", "//"), Files.readAllLines(file.toPath()));
            File cacheFile = new File(compressedPath.replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
            cacheFile.createNewFile();
            try (FileOutputStream fileOutputStream = new FileOutputStream(cacheFile)) {
                fileOutputStream.write(Files.readAllBytes(file.toPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFileChange(File blogFile) {
        String compressedPath = blogFile.getAbsolutePath();
        File sourceFile = new File(compressedPath.replace(FilePath, Config.nativeHexoPath + Constant.cachePath));
        //原始文件
        if(sourceFile.exists()){
            List<String> compare = PatchTool.compare(blogFile.toPath(), sourceFile.toPath());
            //获取了差异文件
            if(compare.size()>0){
                //上传文件
                boolean b = NetToolApi.updateAFile(compressedPath.replace(FilePath, ""), compare);
                //更新缓存文件
                try {
                    Files.copy(blogFile.toPath(),sourceFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onFileDelete(File file) {
        boolean b = NetToolApi.deleteAFile(file.getAbsolutePath());
        if(b){
            new File(file.getAbsolutePath().replace(FilePath,"")).delete();
        }
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}
