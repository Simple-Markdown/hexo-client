package live.midreamsheep.hexo.client.monitor;

import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerMapper;
import live.midreamsheep.hexo.client.net.NetToolApi;
import live.midreamsheep.hexo.client.tool.patch.PatchTool;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.IOException;
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
        HandlerMapper.handlerMap.get(HandlerEnum.ADD_DIR.getId()).handle(directory.getAbsolutePath().replace(FilePath, "").getBytes());
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("修改文件夹：" + directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("删除文件夹：" + directory.getAbsolutePath());
    }

    @Override
    public void onFileCreate(File file) {
        String compressedPath = file.getAbsolutePath();
        System.out.println("新建文件：" + compressedPath);
        try {
            Files.copy(file.toPath(), new File(Config.nativeHexoPath+Constant.cachePath+compressedPath.replace(FilePath,"")).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFileChange(File blogFile) {
        String compressedPath = blogFile.getAbsolutePath();
        File sourceFile = new File(Config.nativeHexoPath+Constant.cachePath+compressedPath.replace(FilePath,""));
        //原始文件
        if(sourceFile.exists()){
            List<String> compare = PatchTool.compare(blogFile.toPath(), sourceFile.toPath());
            //获取了差异文件
            if(compare.size()>0){

            }
            //复制到缓存文件夹
            try {
                Files.copy(blogFile.toPath(),sourceFile.toPath(),java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFileDelete(File file) {
        boolean b = NetToolApi.deleteAFile(file.getAbsolutePath());
        if(b){
            new File(Config.nativeHexoPath+Constant.cachePath+file.getAbsolutePath().replace(FilePath,"")).delete();
        }
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}
