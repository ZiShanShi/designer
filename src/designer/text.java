package designer;

import designer.options.echart.Title;
import designer.options.echart.json.GsonOption;
import designer.options.echart.series.Series;
import designer.options.echart.style.TextStyle;
import designer.widget.Widget;
import foundation.util.Util;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kimi
 * @description
 * @date 2018-12-19 15:09
 */


public class text {

    public static void main(String[] args)  {


        //eee();
        //tetxt();
//        options();
//        clasname();
        try {
            String ss = "   {  \"animation\":true,\n" +
                    "    \"title\":{\n" +
                    "        \"id\":\"\",\n" +
                    "        \"text\":\"test\",\n" +
                    "        \"link\":\"\",\n" +
                    "        \"target\":\"blank\",\n" +
                    "        \"subtext\":\"\",\n" +
                    "        \"sublink\":\"\",\n" +
                    "        \"textStyle\":{\n" +
                    "            \"color\":\"#333333\",\n" +
                    "            \"align\":\"left\",\n" +
                    "            \"fontSize\":18,\n" +
                    "            \"fontStyle\":\"normal\",\n" +
                    "            \"fontWeight\":\"normal\"\n" +
                    "        },\n" +
                    "        \"subtextStyle\":{\n" +
                    "            \"color\":\"#aaa\",\n" +
                    "            \"align\":\"left\",\n" +
                    "            \"fontSize\":12,\n" +
                    "            \"fontStyle\":\"normal\",\n" +
                    "            \"fontWeight\":\"normal\"\n" +
                    "        },\n" +
                    "        \"show\":true,\n" +
                    "        \"backgroundColor\":\"transparent\",\n" +
                    "        \"borderColor\":\"#ccc\",\n" +
                    "        \"borderWidth\":0,\n" +
                    "        \"itemGap\":10,\n" +
                    "        \"zlevel\":0,\n" +
                    "        \"z\":2,\n" +
                    "        \"left\":\"auto\",\n" +
                    "        \"top\":\"auto\",\n" +
                    "        \"right\":\"auto\",\n" +
                    "        \"bottom\":\"auto\",\n" +
                    "        \"shadowBlur\":0,\n" +
                    "        \"shadowColor\":\"\",\n" +
                    "        \"shadowOffsetX\":0,\n" +
                    "        \"shadowOffsetY\":0\n" +
                    "    },\n" +
                    "    \"toolbox\":{\n" +
                    "        \"id\":\"\",\n" +
                    "        \"feature\":{\n" +
                    "\n" +
                    "        },\n" +
                    "        \"orient\":\"horizontal\",\n" +
                    "        \"itemSize\":15,\n" +
                    "        \"showTitle\":true,\n" +
                    "        \"iconStyle\":{\n" +
                    "            \"color\":{\n" +
                    "\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"show\":true,\n" +
                    "        \"itemGap\":10,\n" +
                    "        \"zlevel\":0,\n" +
                    "        \"z\":2,\n" +
                    "        \"left\":\"auto\",\n" +
                    "        \"top\":\"auto\",\n" +
                    "        \"right\":\"auto\",\n" +
                    "        \"bottom\":\"auto\",\n" +
                    "        \"width\":\"auto\",\n" +
                    "        \"height\":\"auto\"\n" +
                    "    },\n" +
                    "    \"tooltip\":{\n" +
                    "        \"showContent\":true,\n" +
                    "        \"trigger\":\"item\",\n" +
                    "        \"triggerOn\":\"mousemove$or$click\",\n" +
                    "        \"position\":{\n" +
                    "\n" +
                    "        },\n" +
                    "        \"showDelay\":0,\n" +
                    "        \"hideDelay\":0,\n" +
                    "        \"transitionDuration\":0.4,\n" +
                    "        \"enterable\":false,\n" +
                    "        \"axisPointer\":{\n" +
                    "\n" +
                    "        },\n" +
                    "        \"textStyle\":{\n" +
                    "            \"color\":\"#fff\",\n" +
                    "            \"fontSize\":14,\n" +
                    "            \"fontStyle\":\"normal\",\n" +
                    "            \"fontWeight\":\"normal\"\n" +
                    "        },\n" +
                    "        \"alwaysShowContent\":false,\n" +
                    "        \"show\":true,\n" +
                    "        \"backgroundColor\":\"rgba(50,50,50,0.7)\",\n" +
                    "        \"borderColor\":\"#333\",\n" +
                    "        \"borderWidth\":0\n" +
                    "    },\n" +
                    "    \"legend\":{\n" +
                    "        \"id\":\"\",\n" +
                    "        \"orient\":\"horizontal\",\n" +
                    "        \"type\":\"plain\",\n" +
                    "        \"itemWidth\":25,\n" +
                    "        \"itemHeight\":14,\n" +
                    "        \"textStyle\":{\n" +
                    "            \"color\":\"#333333\",\n" +
                    "            \"align\":\"left\",\n" +
                    "            \"fontSize\":18,\n" +
                    "            \"fontStyle\":\"normal\",\n" +
                    "            \"fontWeight\":\"normal\",\n" +
                    "            \"backgroundColor\":\"transparent\"\n" +
                    "        },\n" +
                    "        \"selectedMode\":\"true\",\n" +
                    "        \"selected\":{\n" +
                    "\n" +
                    "        },\n" +
                    "        \"data\":[\n" +
                    "            \"qty\",\n" +
                    "            \"qty_original\"\n" +
                    "        ],\n" +
                    "        \"align\":\"auto\",\n" +
                    "        \"show\":true,\n" +
                    "        \"backgroundColor\":\"transparent\",\n" +
                    "        \"borderColor\":\"#ccc\",\n" +
                    "        \"borderWidth\":1,\n" +
                    "        \"itemGap\":10,\n" +
                    "        \"zlevel\":0,\n" +
                    "        \"z\":2,\n" +
                    "        \"shadowBlur\":0,\n" +
                    "        \"shadowColor\":\"\",\n" +
                    "        \"shadowOffsetX\":0,\n" +
                    "        \"shadowOffsetY\":0\n" +
                    "    },\n" +
                    "    \"grid\":{\n" +
                    "        \"id\":\"\",\n" +
                    "        \"containLabel\":false,\n" +
                    "        \"show\":true,\n" +
                    "        \"backgroundColor\":\"transparent\",\n" +
                    "        \"borderColor\":\"#ccc\",\n" +
                    "        \"borderWidth\":0,\n" +
                    "        \"zlevel\":0,\n" +
                    "        \"z\":2,\n" +
                    "        \"left\":\"10%\",\n" +
                    "        \"top\":\"60\",\n" +
                    "        \"right\":\"10%\",\n" +
                    "        \"bottom\":\"60\",\n" +
                    "        \"width\":\"auto\",\n" +
                    "        \"height\":\"auto\",\n" +
                    "        \"shadowBlur\":0,\n" +
                    "        \"shadowColor\":\"\",\n" +
                    "        \"shadowOffsetX\":0,\n" +
                    "        \"shadowOffsetY\":0\n" +
                    "    },\n" +
                    "    \"xAxis\":[\n" +
                    "        {\n" +
                    "            \"type\":\"category\",\n" +
                    "            \"data\":[\n" +
                    "                \"C00001\",\n" +
                    "                \"C00002\",\n" +
                    "                \"C00003\",\n" +
                    "                \"C00004\",\n" +
                    "                \"C00005\",\n" +
                    "                \"C00006\",\n" +
                    "                \"C00007\",\n" +
                    "                \"C00008\",\n" +
                    "                \"C00009\",\n" +
                    "                \"C00010\",\n" +
                    "                \"C00011\",\n" +
                    "                \"C00012\",\n" +
                    "                \"C00013\"\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"yAxis\":[\n" +
                    "        {\n" +
                    "            \"type\":\"value\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"series\":[\n" +
                    "        {\n" +
                    "            \"barMinHeight\":0,\n" +
                    "            \"barWidth\":{\n" +
                    "\n" +
                    "            },\n" +
                    "            \"barMaxWidth\":0,\n" +
                    "            \"barGap\":\"30%\",\n" +
                    "            \"defaultMap\":{\n" +
                    "\n" +
                    "            },\n" +
                    "            \"legendHoverLink\":true,\n" +
                    "            \"xAxisIndex\":0,\n" +
                    "            \"yAxisIndex\":0,\n" +
                    "            \"name\":\"qty\",\n" +
                    "            \"type\":\"bar\",\n" +
                    "            \"stack\":\"\",\n" +
                    "            \"tooltip\":{\n" +
                    "                \"position\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"textStyle\":{\n" +
                    "                    \"color\":\"#fff\",\n" +
                    "                    \"fontSize\":14,\n" +
                    "                    \"fontStyle\":\"normal\",\n" +
                    "                    \"fontWeight\":\"normal\"\n" +
                    "                },\n" +
                    "                \"backgroundColor\":\"rgba(50,50,50,0.7)\",\n" +
                    "                \"borderColor\":\"#333\",\n" +
                    "                \"borderWidth\":0\n" +
                    "            },\n" +
                    "            \"itemStyle\":{\n" +
                    "\n" +
                    "            },\n" +
                    "            \"markPoint\":{\n" +
                    "                \"symbol\":\"pin\",\n" +
                    "                \"symbolSize\":\"50\",\n" +
                    "                \"itemStyle\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"data\":[\n" +
                    "\n" +
                    "                ],\n" +
                    "                \"animation\":true,\n" +
                    "                \"animationDuration\":1000,\n" +
                    "                \"animationEasing\":\"cubicOut\",\n" +
                    "                \"animationDurationUpdate\":300,\n" +
                    "                \"animationEasingUpdate\":\"cubicOut\"\n" +
                    "            },\n" +
                    "            \"markLine\":{\n" +
                    "                \"symbol\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"symbolSize\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"precision\":2,\n" +
                    "                \"data\":[\n" +
                    "\n" +
                    "                ],\n" +
                    "                \"animation\":true,\n" +
                    "                \"animationDuration\":1000,\n" +
                    "                \"animationEasing\":\"cubicOut\",\n" +
                    "                \"animationDurationUpdate\":300,\n" +
                    "                \"animationEasingUpdate\":\"cubicOut\"\n" +
                    "            },\n" +
                    "            \"zlevel\":0,\n" +
                    "            \"z\":2,\n" +
                    "            \"label\":{\n" +
                    "                \"color\":\"#fff\"\n" +
                    "            },\n" +
                    "            \"coordinateSystem\":\"cartesian2d\",\n" +
                    "            \"data\":[\n" +
                    "                \"3000.0000\",\n" +
                    "                \"13000.0000\",\n" +
                    "                \"4400.0000\",\n" +
                    "                \"9000.0000\",\n" +
                    "                \"25000.0000\",\n" +
                    "                \"1200.0000\",\n" +
                    "                \"19200.0000\",\n" +
                    "                \"2880.0000\",\n" +
                    "                \"1300.0000\",\n" +
                    "                \"216.0000\",\n" +
                    "                \"476.0000\",\n" +
                    "                \"3840.0000\",\n" +
                    "                \"4800.0000\"\n" +
                    "            ]\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"barMinHeight\":0,\n" +
                    "            \"barWidth\":{\n" +
                    "\n" +
                    "            },\n" +
                    "            \"barMaxWidth\":0,\n" +
                    "            \"barGap\":\"30%\",\n" +
                    "            \"defaultMap\":{\n" +
                    "\n" +
                    "            },\n" +
                    "            \"legendHoverLink\":true,\n" +
                    "            \"xAxisIndex\":0,\n" +
                    "            \"yAxisIndex\":0,\n" +
                    "            \"name\":\"qty_original\",\n" +
                    "            \"type\":\"bar\",\n" +
                    "            \"stack\":\"\",\n" +
                    "            \"tooltip\":{\n" +
                    "                \"position\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"textStyle\":{\n" +
                    "                    \"color\":\"#fff\",\n" +
                    "                    \"fontSize\":14,\n" +
                    "                    \"fontStyle\":\"normal\",\n" +
                    "                    \"fontWeight\":\"normal\"\n" +
                    "                },\n" +
                    "                \"backgroundColor\":\"rgba(50,50,50,0.7)\",\n" +
                    "                \"borderColor\":\"#333\",\n" +
                    "                \"borderWidth\":0\n" +
                    "            },\n" +
                    "            \"itemStyle\":{\n" +
                    "\n" +
                    "            },\n" +
                    "            \"markPoint\":{\n" +
                    "                \"symbol\":\"pin\",\n" +
                    "                \"symbolSize\":\"50\",\n" +
                    "                \"itemStyle\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"data\":[\n" +
                    "\n" +
                    "                ],\n" +
                    "                \"animation\":true,\n" +
                    "                \"animationDuration\":1000,\n" +
                    "                \"animationEasing\":\"cubicOut\",\n" +
                    "                \"animationDurationUpdate\":300,\n" +
                    "                \"animationEasingUpdate\":\"cubicOut\"\n" +
                    "            },\n" +
                    "            \"markLine\":{\n" +
                    "                \"symbol\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"symbolSize\":{\n" +
                    "\n" +
                    "                },\n" +
                    "                \"precision\":2,\n" +
                    "                \"data\":[\n" +
                    "\n" +
                    "                ],\n" +
                    "                \"animation\":true,\n" +
                    "                \"animationDuration\":1000,\n" +
                    "                \"animationEasing\":\"cubicOut\",\n" +
                    "                \"animationDurationUpdate\":300,\n" +
                    "                \"animationEasingUpdate\":\"cubicOut\"\n" +
                    "            },\n" +
                    "            \"zlevel\":0,\n" +
                    "            \"z\":2,\n" +
                    "            \"label\":{\n" +
                    "                \"color\":\"#fff\"\n" +
                    "            },\n" +
                    "            \"coordinateSystem\":\"cartesian2d\",\n" +
                    "            \"data\":[\n" +
                    "                \"2000.0000\",\n" +
                    "                \"5200.0000\",\n" +
                    "                \"2200.0000\",\n" +
                    "                \"6000.0000\",\n" +
                    "                \"10000.0000\",\n" +
                    "                \"400.0000\",\n" +
                    "                \"19200.0000\",\n" +
                    "                \"1440.0000\",\n" +
                    "                \"650.0000\",\n" +
                    "                \"216.0000\",\n" +
                    "                \"238.0000\",\n" +
                    "                \"1920.0000\",\n" +
                    "                \"1600.0000\"\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"animationThreshold\":2000,\n" +
                    "    \"animationDuration\":1000,\n" +
                    "    \"animationDurationUpdate\":300,\n" +
                    "    \"animationEasing\":\"linear\",\n" +
                    "    \"animationEasingUpdate\":\"cubicOut\",\n" +
                    "    \"radar\":{\n" +
                    "        \"id\":\"\",\n" +
                    "        \"zlevel\":0,\n" +
                    "        \"z\":2,\n" +
                    "        \"center\":\"['50%', '50%']\",\n" +
                    "        \"radius\":\"75%\",\n" +
                    "        \"startAngle\":90,\n" +
                    "        \"name\":{\n" +
                    "            \"show\":true,\n" +
                    "            \"textStyle\":{\n" +
                    "                \"color\":\"#333\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"nameGap\":15,\n" +
                    "        \"splitNumber\":5,\n" +
                    "        \"scale\":false,\n" +
                    "        \"silent\":false,\n" +
                    "        \"triggerEvent\":false,\n" +
                    "        \"axisLine\":\"true true none [10, 15] [0, 0] #333 1 solid\",\n" +
                    "        \"splitLine\":\"true auto #333 solid\",\n" +
                    "        \"splitArea\":\"true auto ['rgba(250,250,250,0.3)','rgba(200,200,200,0.3)'] 0 0\"\n" +
                    "    }}";
            loadJson(ss);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadJson(String ss) throws IOException {
        String themeJson = FileUtils.readFileToString(new File("D:\\workspace\\finebi\\source\\my\\desginer\\out\\artifacts\\designer_Web_exploded\\chartTemplate\\theme\\vintage.json"), "UTF-8");
        JSONObject themeObject = JSONObject.fromObject(themeJson);
        JSONObject optionObject = JSONObject.fromObject(ss);
        loadJSONObject(themeObject,optionObject);

    }

    private static void loadJSONObject(JSONObject themeObject, JSONObject optionObject) {
        Set ontKeySet = themeObject.keySet();

        for (Object key : ontKeySet) {
            Object oneThemeValue = themeObject.get(key);
            Object oneOptionValue = optionObject.get(key);
//            checkObjectType(oneThemeValue, oneOptionValue, optionObject, key);
        }
        System.out.println(optionObject.toString());
    }



    private static void clasname() {
        String name = Series.class.getName();
        int length = Series.class.getSimpleName().length();
        System.out.println(name.substring(0, name.length() - length));
        String name1 = Series.class.getPackage().getName();
        System.out.println(name1);

    }

    private static void options() {
        GsonOption option = new GsonOption();
        Title title = new Title();
        TextStyle textStyle = new TextStyle();
        textStyle.color("3cc");
        title.textStyle(textStyle);
    }

    private static void tetxt() {
        try {
        String name = Widget.class.getName();

            Class<?> aClass = Class.forName(name);
            Field[] declaredFields = aClass.getDeclaredFields();

            Field declaredField = aClass.getDeclaredField("hh");

            Class<? extends Object> fieldClass = declaredField.getType();
            Constructor<?>[] declaredConstructors = fieldClass.getDeclaredConstructors();
            for (Constructor<?> declaredConstructor : declaredConstructors) {
                Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    System.out.println(parameterType.getSimpleName());
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void eee() {
        String defaultPath = "D:\\workspace\\finebi\\source\\my\\desginer\\WebRoot\\chartTemplate\\default";

        String chartTemplePath = Util.filePathJion(defaultPath, "echart.xml");
        File file = new File(chartTemplePath);

        SAXReader reader = new SAXReader();

        try {
            Document read = reader.read(file);
            Element rootElement = read.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator();
            if (iterator.hasNext()) {
                Element next = iterator.next();
                String name = next.getName();
                Iterator iterator1 = next.attributeIterator();
                if (iterator1.hasNext()) {

                }
                String stringValue = next.getStringValue();
                System.out.println(stringValue);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
