package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.config.Config;
import live.midreamsheep.hexo.client.data.Constant;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.monitor.ListenerApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Push implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        compare();
    }
    private void compare(){
        Config.isPulling = true;
        List<String> blogs = new ArrayList<>();
        getFiles(new File(Config.nativeHexoPath+File.separator+ Constant.blogPath),blogs);
        List<String> caches = new ArrayList<>();
        getFiles(new File(Config.nativeHexoPath+File.separator+ Constant.cachePath),caches);
        //对比文件
        String filePath = new File(Config.nativeHexoPath+File.separator+Constant.cachePath).getAbsolutePath();
        String sourcePath = new File(Config.nativeHexoPath+File.separator+Constant.blogPath).getAbsolutePath();
        for (String blog : blogs) {
            String blogPath = blog.replace(sourcePath,"");
            if(caches.contains(filePath+blogPath)){
                //文件存在
                File blogFile = new File(blog);
                File cacheFile = new File(filePath+blogPath);
                if(blogFile.length() != cacheFile.length()){
                    System.out.println("文件"+blogPath+"有变化");
                    //文件不同
                    ListenerApi.fileChange(blogFile,true);
                }
                caches.remove(filePath+blogPath);
            }else{
                System.out.println("新文件"+blogPath);
                //文件不存在
                ListenerApi.fileCreate(new File(blog),true);
            }
        }
        String filePath2 = new File(Config.nativeHexoPath+File.separator+Constant.cachePath).getAbsolutePath();
        for (String cach : caches) {
            System.out.println("删除文件"+cach.replace(filePath2,sourcePath));
            ListenerApi.fileDelete(new File(cach.replace(filePath2,sourcePath)),true);
        }
        Config.isPulling = false;
    }
    private void getFiles(File file,List<String> files){
        if(file.isDirectory()){
            for (File file1 : Objects.requireNonNull(file.listFiles())) {
                getFiles(file1,files);
            }
        }else{
            files.add(file.getAbsolutePath());
        }
    }
}
