package designer.cache;

import java.util.Map;
import java.util.Set;

public interface ICacheSourceType {

    ICacheSourceType putFieldNodeSourceSet(EOptionSourceType type , Set<FieldNode> fieldNodeSourceSet);

    Map<EOptionSourceType, Set<FieldNode>> getFieldNodeSourceMap();

    ICacheSourceType setFieldNodeSourceMap(Map<EOptionSourceType, Set<FieldNode>> fieldNodeSourceMap);

    EOptionSourceType getTypeFromNode(FieldNode node);

    Set<FieldNode> getFieldNodeSet(EOptionSourceType type);
}
