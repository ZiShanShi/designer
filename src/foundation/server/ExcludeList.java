package foundation.server;

import java.util.ArrayList;
import java.util.List;

public class ExcludeList
{
  private List<String> dataList;

  public ExcludeList()
  {
    this.dataList = new ArrayList();
  }

  public boolean contains(RequestPath path) {
    String uri = path.getURI();

    if (uri == null) {
      return false;
    }

    for (String segment : this.dataList) {
      if (uri.contains(segment)) {
        return true;
      }
    }

    return false;
  }

  public void add(String exclude) {
    this.dataList.add(exclude);
  }
}