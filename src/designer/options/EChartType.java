package designer.options;

public enum EChartType {
    bar, line, pie, scatter, effectScatter, radar, tree, treemap, sunburst,
    boxplot, candlestick, heatmap, map, parallel, lines, graph,
    sankey, funnel, gauge, pictorialBar, themeRiver, custom, unknown;

    public static EChartType parse(String type) {
        if (EChartType.bar.name().equalsIgnoreCase(type)) {
            return bar;
        } else if (EChartType.line.name().equalsIgnoreCase(type)) {
            return line;
        } else if (EChartType.pie.name().equalsIgnoreCase(type)) {
            return pie;
        } else if (EChartType.scatter.name().equalsIgnoreCase(type)) {
            return scatter;
        } else if (EChartType.effectScatter.name().equalsIgnoreCase(type)) {
            return effectScatter;
        } else if (EChartType.radar.name().equalsIgnoreCase(type)) {
            return radar;
        } else if (EChartType.tree.name().equalsIgnoreCase(type)) {
            return tree;
        } else if (EChartType.treemap.name().equalsIgnoreCase(type)) {
            return treemap;
        } else if (EChartType.sunburst.name().equalsIgnoreCase(type)) {
            return sunburst;
        } else if (EChartType.boxplot.name().equalsIgnoreCase(type)) {
            return boxplot;
        } else if (EChartType.candlestick.name().equalsIgnoreCase(type)) {
            return candlestick;
        } else if (EChartType.heatmap.name().equalsIgnoreCase(type)) {
            return heatmap;
        } else if (EChartType.map.name().equalsIgnoreCase(type)) {
            return map;
        } else if (EChartType.parallel.name().equalsIgnoreCase(type)) {
            return parallel;
        } else if (EChartType.lines.name().equalsIgnoreCase(type)) {
            return lines;
        } else if (EChartType.graph.name().equalsIgnoreCase(type)) {
            return graph;
        } else if (EChartType.sankey.name().equalsIgnoreCase(type)) {
            return sankey;
        } else if (EChartType.funnel.name().equalsIgnoreCase(type)) {
            return funnel;
        } else if (EChartType.gauge.name().equalsIgnoreCase(type)) {
            return gauge;
        } else if (EChartType.pictorialBar.name().equalsIgnoreCase(type)) {
            return pictorialBar;
        } else if (EChartType.themeRiver.name().equalsIgnoreCase(type)) {
            return themeRiver;
        } else if (EChartType.custom.name().equalsIgnoreCase(type)) {
            return custom;
        }

        return unknown;
    }

}
