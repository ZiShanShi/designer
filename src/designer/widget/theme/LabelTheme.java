package designer.widget.theme;

/**
 * @author kimi
 * @description
 * @date 2018-12-24 15:32
 */


public class LabelTheme {
    private NormalTheme normal;
    private NormalTheme emphasis;

    public NormalTheme getNormal() {
        return normal;
    }

    public LabelTheme setNormal(NormalTheme normal) {
        this.normal = normal;
        return this;
    }

    public NormalTheme getEmphasis() {
        return emphasis;
    }

    public LabelTheme setEmphasis(NormalTheme emphasis) {
        this.emphasis = emphasis;
        return this;
    }
}
