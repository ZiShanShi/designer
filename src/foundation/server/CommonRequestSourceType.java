package foundation.server;

public enum CommonRequestSourceType {
	
	xml,js,jpg,png,html,jsp,json,ttf,jpeg,zip,rar,gif,css,bmp,map,unknow;
	
	public static CommonRequestSourceType parse(String type) {
		if (type.equalsIgnoreCase(CommonRequestSourceType.xml.name())) {
			return CommonRequestSourceType.xml;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.js.name())) {
			return CommonRequestSourceType.js;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.jpg.name())) {
			return CommonRequestSourceType.jpg;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.xml.name())) {
			return CommonRequestSourceType.png;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.html.name())) {
			return CommonRequestSourceType.html;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.jsp.name())) {
			return CommonRequestSourceType.jsp;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.json.name())) {
			return CommonRequestSourceType.json;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.ttf.name())) {
			return CommonRequestSourceType.ttf;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.jpeg.name())) {
			return CommonRequestSourceType.jpeg;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.zip.name())) {
			return CommonRequestSourceType.zip;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.rar.name())) {
			return CommonRequestSourceType.rar;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.gif.name())) {
			return CommonRequestSourceType.gif;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.css.name())) {
			return CommonRequestSourceType.css;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.bmp.name())) {
			return CommonRequestSourceType.bmp;
		}
		else if (type.equalsIgnoreCase(CommonRequestSourceType.map.name())) {
			return CommonRequestSourceType.map;
		}
		else {
			return CommonRequestSourceType.unknow;
		}
	}
}
