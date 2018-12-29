package designer.widget.theme;

/**
 * @author kimi
 * @description
 * @date 2018-12-24 15:23
 */


public class ItemStyleTheme {
    private NormalTheme normal;
    private NormalTheme emphasis;

    public NormalTheme getNormal() {
        return normal;
    }

    public ItemStyleTheme setNormal(NormalTheme normal) {
        this.normal = normal;
        return this;
    }

    public NormalTheme getEmphasis() {
        return emphasis;
    }

    public ItemStyleTheme setEmphasis(NormalTheme emphasis) {
        this.emphasis = emphasis;
        return this;
    }
}
