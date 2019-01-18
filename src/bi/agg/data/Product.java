package bi.agg.data;


public class Product extends Dimension {

	public Product() {
		super("product");
		
		DimensionCell sku = new SKU();
		DimensionCell brand = new Brand();
		
		items.add(sku);
		items.add(brand);
	}
	
	
}
