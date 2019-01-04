package designer.xml;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * @author kimi
 * @description 包含在总父xml层级数root节点
 * @date 2018-12-29 15:02
 */


public class LayerElemetRoot {
    private int layer;
    private Element root;
    private String path;
    private Document document;
    public LayerElemetRoot(int layer, Element root) {
        this.layer = layer;
        this.root = root;
    }

    public LayerElemetRoot(int layer, Element root, String path) {
        this(layer, root);
        this.path = path;
    }

    public LayerElemetRoot(int layer, Document document, Element rootElement, String absolutePath) {
        this(layer,rootElement,absolutePath);
        this.document = document;
    }

    public int getLayer() {
        return layer;
    }

    public Element getRoot() {
        return root;
    }

    public String getPath() {
        return path;
    }

    public Document getDocument() {
        return document;
    }
}
