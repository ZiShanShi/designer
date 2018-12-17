package foundation.variant;

public interface IVariantParseListener {

	void onSegment(String segment);

	void addVariant(String variant) throws Exception;

}
