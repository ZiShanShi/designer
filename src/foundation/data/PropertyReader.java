package foundation.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader
{
  private File file;
  private Properties properties;

  public PropertyReader(File file)
    throws IOException
  {
    this.file = file;
    load();
  }

  public Variant get(String name) {
    String value = this.properties.getProperty(name);
    return new Variant(value);
  }

  private void load() throws IOException {
    this.properties = new Properties();

    FileInputStream inputStream = new FileInputStream(this.file);
    try {
      this.properties.load(inputStream);
    }
    finally {
      inputStream.close();
    }
  }
}