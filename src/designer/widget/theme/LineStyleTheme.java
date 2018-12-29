package designer.widget.theme;

/**
 * @author kimi
 * @description
 * @date 2018-12-24 15:23
 */


public class LineStyleTheme {
    private NormalTheme normal;
    private NormalTheme emphasis;
    private  String color;

    public NormalTheme getNormal() {
        return normal;
    }

    public LineStyleTheme setNormal(NormalTheme normal) {
        this.normal = normal;
        return this;
    }

    public NormalTheme getEmphasis() {
        return emphasis;
    }

    public LineStyleTheme setEmphasis(NormalTheme emphasis) {
        this.emphasis = emphasis;
        return this;
    }

    public String getColor() {
        return color;
    }

    public LineStyleTheme setColor(String color) {
        this.color = color;
        return this;
    }
}
