package designer;

import designer.exception.DesignerFileException;
import designer.options.ChartOption;
import designer.options.DesignerComponentFactory;
import designer.options.EChartType;
import designer.options.Theme;
import designer.options.echart.Option;
import designer.topic.*;
import foundation.util.Util;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author kimi
 * @description 设计器工具类
 * @date 2018-12-17 14:20
 */


public class DesignerUtil {

    public static String getOptionFilePath(String id) {
        return id;
    }

    public static String fixXmlStringValue(String rawValue) {
        rawValue = rawValue.replaceAll(Util.String_Escape_newLine,Util.String_Space);
        rawValue = rawValue.replaceAll(Util.String_Escape_newSpace,Util.String_Space);
        rawValue = rawValue.trim();
        if (Util.isEmptyStr(rawValue)) {
            return null;
        }else {
            return rawValue;
        }
    }
    public static File checkFileLegality(String filePath) {
        if (Util.isEmptyStr(filePath)) {
            throw new DesignerFileException("path is empty");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new DesignerFileException(filePath);
        }
        return file;
    }

    public static Option combineOption(Element path) {
        return null;
    }

    private static void combine(Topic topic, Topic baseTopic) {
        //TODO 组合成完整的topic
    }
    private static void combine(Topic topic) {
        //TODO 组合成完整的topic
        combine(topic);
    }
    private static void combine(Option option) {
        //TODO 组合成完整的option
        combine(option);
    }

    private static void combine(Option option, Option baseOption) {
        //TODO 组合成完整的option
    }

    public static void loadTopicFile(File topicFile, boolean isDefault, Topic topic) {
        SAXReader reader = new SAXReader();
        try {
            Document read = reader.read(topicFile);
            Element root = read.getRootElement();
            loadTopicElement(root, topic);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (isDefault) {
            DesignerComponentFactory.getInstance().setBaseTopic(topic);
        } else {
            Topic baseTopic = DesignerComponentFactory.getInstance().getBaseTopic();
            combine(topic, baseTopic);
        }
    }


    public static void loadTopicFile(File topicFile, Topic topic) {
        loadTopicFile(topicFile, true, topic);
        topic.invalidate();
    }

    private static void loadTopicElement(Element root, Topic topic) {
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element childElement = iterator.next();

            load2Topic(childElement, topic);
        }
    }

    private static void load2Topic(Element childElement, Topic topic) {
        String name = childElement.getName();
        String elementStringValue = childElement.getStringValue();
        if (DesignerConstant.HAS_GRID.equalsIgnoreCase(name)) {
            topic.setHasGrid(Util.stringToBoolean(elementStringValue));
        } else if (DesignerConstant.CHANGE_TYPE.equalsIgnoreCase(name)) {
            List<ChartType> chartTypeList = new ArrayList<>();

            Iterator<Element> chartTypeIterator = childElement.elementIterator();
            while (chartTypeIterator.hasNext()) {
                Element oneChartTypeEle = chartTypeIterator.next();
                String oneChartTypeName = oneChartTypeEle.getName();
                EChartType oneChartType = EChartType.valueOf(oneChartTypeName);

                Attribute minDimensionAttr = oneChartTypeEle.attribute("minDimension");
                int minDimension = Integer.parseInt(minDimensionAttr.getValue());

                Attribute minMensurmentAttr = oneChartTypeEle.attribute("minMensurment");
                int minMensurment = Integer.parseInt(minMensurmentAttr.getValue());

                Attribute defaultAttr = oneChartTypeEle.attribute("default");
                boolean isDefault = Util.stringToBoolean(defaultAttr.getValue());

                if (isDefault) {
                    topic.setDefaultType(oneChartType);
                }

                ChartType chartType = new ChartType(oneChartType);
                chartType.setMinDimensionNum(minDimension).setMinMensurmentNum(minMensurment);

                chartTypeList.add(chartType);
            }
        } else if (DesignerConstant.DIMENSIONS.equalsIgnoreCase(name)) {
            try {
                List<String> dimensions = Util.StringToList(elementStringValue);//默认；
                topic.setDimensionList(dimensions);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if (DesignerConstant.MEASURMENTS.equalsIgnoreCase(name)) {
            try {
                List<String> mesurments = Util.StringToList(elementStringValue);//默认；
                topic.setMensurmentList(mesurments);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else if (DesignerConstant.DATA_TYPE.equalsIgnoreCase(name)) {
            EDataType eDataType = EDataType.valueOf(elementStringValue);
            topic.setDataType(eDataType);

        }else if (DesignerConstant.DATA_NAME.equalsIgnoreCase(name)) {
            topic.setDataName(elementStringValue.trim());

        }else if (DesignerConstant.FILTERS.equalsIgnoreCase(name)) {
            //TODO 未定
            List<Element> segmentList = childElement.elements("segment");
            for (Element segment : segmentList) {
                Attribute nameAttr = segment.attribute("name");
                String fieldName;
                if (nameAttr == null) {
                    throw new DesignerFileException("节点: " + name);
                } else {
                    fieldName = nameAttr.getValue();
                }
                Attribute linkAttr = segment.attribute("link");
                String link;
                if (linkAttr == null) {
                    link = DesignerConstant.keyword_type_equal;
                } else {
                    link = linkAttr.getValue();
                }
                Attribute typeAttr = segment.attribute("type");
                ESqlValueType type;
                if (typeAttr == null) {
                    type = ESqlValueType.value;
                } else {
                    type = ESqlValueType.valueOf(typeAttr.getValue());
                }

                SegmentPart segmentPart = new SegmentPart();
                segmentPart.setLink(link).setName(fieldName).setValueType(type).setValue(elementStringValue);
                topic.putSegment(segmentPart);
            }


        }else if (DesignerConstant.CHART_OPTION.equalsIgnoreCase(name)) {
            ChartOption chartOption;
            Element path = childElement.element("path");
            if (path != null) {
                Option realOption = DesignerUtil.combineOption(path);
                chartOption = new ChartOption(realOption);
            } else {
                chartOption = new ChartOption();
            }

            Element element = childElement.element(DesignerConstant.THEME);
            if (element != null) {
                String themeName = element.getStringValue();
                Theme realTheme = DesignerComponentFactory.getInstance().getThemeByName(themeName);
                chartOption.setTheme(realTheme);
            }

        }else if (DesignerConstant.GRID_OPTION.equalsIgnoreCase(name)) {
            //TODO 暂无
        }
    }
}
