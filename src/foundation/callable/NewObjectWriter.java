package foundation.callable;

import foundation.util.Util;

public class NewObjectWriter
  implements IBeanWriter
{
  public void write(EnvelopWriter writer)
  {
    writer.beginObject();

    writer.writeString("id", Util.newShortGUID());

    writer.endObject();
  }

  public void setBean(Object object)
  {
  }
}