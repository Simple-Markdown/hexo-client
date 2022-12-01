package live.midreamsheep.hexo.client.data;

import java.util.HashSet;
import java.util.Set;

public class TextPostfix {
    public static final Set<String> set = new HashSet<>();
    static {
        set.add(".xml");
        set.add(".yml");
        set.add(".md");
        set.add(".txt");
    }
}
