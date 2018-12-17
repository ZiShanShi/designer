package foundation.variant;

import java.util.List;

public interface IVariantRequestListener {

	public List<String> getVariantNames();

	public String getStringValue(String name, VariantRequestParams paramVariantRequestParams) throws Exception;
	
}
