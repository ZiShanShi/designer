

package designer.options.echart.json;

import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.cache.ICacheSourceType;
import designer.options.echart.Option;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 增强的Option
 *
 * @author kimi
 */
public class GsonOption extends Option implements ICacheSourceType {

    private transient Map<EOptionSourceType,Set<FieldNode>> fieldNodeSourceMap;

    private transient Map<FieldNode,EOptionSourceType> nodeSourceTypeMap;

    public GsonOption() {
        fieldNodeSourceMap = new HashMap<>();
        nodeSourceTypeMap = new HashMap<>();
    }

    public Map<EOptionSourceType, Set<FieldNode>> getFieldNodeSourceMap() {
        return fieldNodeSourceMap;
    }


    public GsonOption putFieldNodeSourceSet(EOptionSourceType type ,Set<FieldNode> fieldNodeSourceSet) {
        fieldNodeSourceMap.put(type, fieldNodeSourceSet);
        for (FieldNode fieldNode : fieldNodeSourceSet) {
            nodeSourceTypeMap.put(fieldNode,type);
        }
        return this;
    }
    public void putFieldNode(EOptionSourceType type ,FieldNode node) {
        Set<FieldNode> fieldNodes = fieldNodeSourceMap.get(type);
        if (fieldNodes == null) {
            fieldNodes = new HashSet<>();
        }
        fieldNodes.add(node);
        fieldNodeSourceMap.put(type, fieldNodes);
    }

    public GsonOption setFieldNodeSourceMap(Map<EOptionSourceType, Set<FieldNode>> fieldNodeSourceMap) {
        this.fieldNodeSourceMap = fieldNodeSourceMap;
        return this;
    }

    @Override
    public EOptionSourceType getTypeFromNode(FieldNode node) {
        return  nodeSourceTypeMap.get(node);
    }

    @Override
    public Set<FieldNode> getFieldNodeSet(EOptionSourceType type) {
        Set<FieldNode> fieldNodes = fieldNodeSourceMap.get(type);
        if (fieldNodes == null) {
            fieldNodes = new HashSet<>();
        }
        fieldNodeSourceMap.put(type, fieldNodes);
        return fieldNodes;
    }

    /**
     * 在浏览器中查看
     */
    public void view() {
        OptionUtil.browse(this);
    }

    @Override
    /**
     * 获取toString值
     */
    public String toString() {
        return GsonUtil.format(this);
    }

    /**
     * 获取toPrettyString值
     */
    public String toPrettyString() {
        return GsonUtil.prettyFormat(this);
    }

    /**
     * 导出到指定文件名
     *
     * @param fileName
     * @return 返回html路径
     */
    public String exportToHtml(String fileName) {
        return exportToHtml(System.getProperty("java.io.tmpdir"), fileName);
    }

    /**
     * 导出到指定文件名
     *
     * @param fileName
     * @return 返回html路径
     */
    public String exportToHtml(String filePath, String fileName) {
        return OptionUtil.exportToHtml(this, filePath, fileName);
    }

    public void setPreFieldNode(FieldNode rootNode) {
        nodeSourceTypeMap.keySet().stream()
                .map(fieldNode -> fieldNode.addPreName(rootNode)).collect(Collectors.toList());

    }
}
