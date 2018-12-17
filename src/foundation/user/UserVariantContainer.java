package foundation.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import foundation.config.Preloader;
import foundation.variant.GlobalVariant;
import foundation.variant.IVariantRequestListener;
import foundation.variant.VariantExistsException;
import foundation.variant.VariantRequestParams;

public class UserVariantContainer extends Preloader implements
		IVariantRequestListener {
	private static UserVariantContainer instance;
	private List<String> variantNames;

	public UserVariantContainer() {
		this.variantNames = new ArrayList<String>();
		initVariantNames();
	}

	public static synchronized UserVariantContainer getInstance()
			throws VariantExistsException {
		if (instance == null) {
			instance = new UserVariantContainer();
		}

		return instance;
	}

	public String getStringValue(String name, VariantRequestParams params) {
		HttpServletRequest request = params.getRequest();

		if (request == null) {
			return null;
		}

		HttpSession session = request.getSession();
		OnlineUser onlineUser = (OnlineUser) session
				.getAttribute(OnlineUser.class.getSimpleName());

		if ("user.id".equals(name)) {
			return onlineUser.getId();
		}
		if ("userid".equals(name)) {
			return onlineUser.getId();
		}
		if ("user.emp_name".equals(name)) {
			return onlineUser.getEmp_name();
		}

		return null;
	}

	public List<String> getVariantNames() {
		return this.variantNames;
	}

	public void initVariantNames() {
		this.variantNames.add("user.id");
		this.variantNames.add("userid");
		this.variantNames.add("user.emp_name");
	}

	public void load() throws Exception {
		GlobalVariant.regist(instance);
	}

}