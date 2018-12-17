package foundation.variant;

import javax.servlet.http.HttpServletRequest;

public class VariantRequestParams
{
  private HttpServletRequest request;

  public VariantRequestParams()
  {
  }

  public VariantRequestParams(HttpServletRequest request)
  {
    this.request = request;
  }

  public HttpServletRequest getRequest() {
    return this.request;
  }
}