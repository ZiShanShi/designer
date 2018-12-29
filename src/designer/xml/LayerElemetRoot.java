package designer.xml;

import org.dom4j.Element;

/**
 * @author kimi
 * @description 包含在总父xml层级数root节点
 * @date 2018-12-29 15:02
 */


public class LayerElemetRoot {
    private int layer;
    private Element root;

    public LayerElemetRoot(int layer, Element root) {
        this.layer = layer;
        this.root = root;
    }

    public int getLayer() {
        return layer;
    }

    public Element getRoot() {
        return root;
    }
}
