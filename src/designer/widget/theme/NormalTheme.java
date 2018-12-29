package designer.widget.theme;

/**
 * @author kimi
 * @description
 * @date 2018-12-24 15:22
 */


public class NormalTheme {
    private int width;
    private int borderWidth;
    private int barBorderWidth;
    private String barBorderColor;
    private String borderColor;
    private String color0;
    private String color;
    private String borderColor0;
    private TextStyleTheme textStyle;
    private String  areaColor;

    public int getWidth() {
        return width;
    }

    public NormalTheme setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public NormalTheme setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public int getBarBorderWidth() {
        return barBorderWidth;
    }

    public NormalTheme setBarBorderWidth(int barBorderWidth) {
        this.barBorderWidth = barBorderWidth;
        return this;
    }

    public String getBarBorderColor() {
        return barBorderColor;
    }

    public NormalTheme setBarBorderColor(String barBorderColor) {
        this.barBorderColor = barBorderColor;
        return this;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public NormalTheme setBorderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public String getColor0() {
        return color0;
    }

    public NormalTheme setColor0(String color0) {
        this.color0 = color0;
        return this;
    }

    public String getColor() {
        return color;
    }

    public NormalTheme setColor(String color) {
        this.color = color;
        return this;
    }

    public String getBorderColor0() {
        return borderColor0;
    }

    public NormalTheme setBorderColor0(String borderColor0) {
        this.borderColor0 = borderColor0;
        return this;
    }

    public TextStyleTheme getTextStyle() {
        return textStyle;
    }

    public NormalTheme setTextStyle(TextStyleTheme textStyle) {
        this.textStyle = textStyle;
        return this;
    }

    public String getAreaColor() {
        return areaColor;
    }

    public NormalTheme setAreaColor(String areaColor) {
        this.areaColor = areaColor;
        return this;
    }
}
