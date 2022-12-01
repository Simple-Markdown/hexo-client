package live.midreamsheep.hexo.client.tool.patch;

import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PatchTool {
    public static List<String> compare(Path blogPath, Path sourcePath) {
        //原始文件
        List<String> original = null;
        List<String> revised = null;
        try {
            original = Files.readAllLines(blogPath);
            revised = Files.readAllLines(sourcePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //两文件的不同点
        Patch<String> patch = DiffUtils.diff(original, revised);
        //生成统一的差异格式
        return UnifiedDiffUtils.generateUnifiedDiff(blogPath.getFileName().toString(), sourcePath.getFileName().toString(), original, patch, 0);
    }
}
