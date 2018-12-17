package foundation.dictionary;

import foundation.callable.Callable;

public class DictionaryService extends Callable
{
  private static DictionaryContainer dictionaryContainer = DictionaryContainer.getInstance();

  protected void publishMethod()
  {
    addMethod("call");
  }

  protected void call() throws Exception {
    String name = this.paths[1];
    Dictionary dictionary = (Dictionary)dictionaryContainer.get(name);

    if (dictionary != null)
      this.resultPool.addValue(dictionary.getDataList());
  }
}