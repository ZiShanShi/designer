package foundation.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//servlet选择安装环境

public class selectInstall extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
    }
	 
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	Enumeration<String> names = request.getHeaderNames();  
    	String url = "";  
    	while (names.hasMoreElements())  
    	{  
    		String name = (String) names.nextElement();  
    		if (request.getHeader(name).contains("ios") || request.getHeader(name).contains("iPhone"))  
    		{  
    			url = "https://itunes.apple.com/cn/app/menarini-ji-xiao-da-ren/id1172739560?mt=8";  
    			break;  
    		}  
    		if (request.getHeader(name).contains("Android"))  
    		{  
    			url = "https://www.pgyer.com/menarinisfez";  
    			break;  
    		} 
    	}  
    	response.sendRedirect(url);  
    }  
}