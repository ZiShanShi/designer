package designer.options.echart.code;

/**
 * 数据过滤模式
 *
 * @author kimi
 * @since  2018-12-16 10:24
 */
public enum FilterMode {
    filter,//数据窗口外的数据，被 过滤掉。这个配置项是最常用的
    empty,//数据窗口外的数据，被 设置为空。 与『过滤掉』的区别是，『设置为空』的数据当空数据展示，也就是说还会占有位置
}
