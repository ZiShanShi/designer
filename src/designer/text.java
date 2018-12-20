package designer;

import designer.options.echart.Title;
import designer.options.echart.json.GsonOption;
import designer.options.echart.style.TextStyle;
import designer.topic.Topic;
import foundation.util.Util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * @author kimi
 * @description
 * @date 2018-12-19 15:09
 */


public class text {

    public static void main(String[] args)  {


        //eee();
        //tetxt();
        options();

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
        String name = Topic.class.getName();

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
