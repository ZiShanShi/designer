package designer.widget.theme;

import net.sf.json.JSONObject;

/**
 * @author kimi
 * @description charts的配色主题
 * @date 2018-12-17 17:20
 */


public class Theme {
    private String name;
    private JSONObject themeJson;


    public String getName() {
        return name;
    }

    public Theme setName(String name) {
        this.name = name;
        return this;
    }

    public JSONObject getThemeJson() {
        return themeJson;
    }

    public Theme setThemeJson(JSONObject themeJson) {
        this.themeJson = themeJson;
        return this;
    }
}
