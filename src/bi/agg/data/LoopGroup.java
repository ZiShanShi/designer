package bi.agg.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("rawtypes")
public class LoopGroup<T extends Loop> implements Iterable<T> {

	protected List<T> loopList;
	protected int size;
	protected int pos;
	
	public LoopGroup() {
		loopList = new ArrayList<T>();
		pos = -1;
		size = 0;
	}
	
	public void first() {
		pos = -1;
		
		int max = loopList.size() - 1, n = max;
		
		//1、设置为开始开始状态，移动BOF指针
		for (int i = max; i >= 0; i--) {
			Loop loop = loopList.get(i);
			loop.first();
			
			n = i;
			
			if (!loop.isEmpty()) {
				break;
			}
		}
		
		//2、设置为开始开始状态，但是不移动BOF指针
		for (int i = 0; i < n; i++) {
			Loop loop = loopList.get(i);
			loop.open();
		}
	}
	
	public boolean next() {
		pos++;
		
		if (pos >= size) {
			return false;
		}
		
		int level = loopList.size() - 1;
		
		for (int i = level; i >= 0; i--) {
			Loop loop = loopList.get(i);
			boolean hasNext = loop.next();
			
			if (hasNext) {
				break;
			}
		}
		
		return true;
	}
	
	public void appendLoop(T item) {
		loopList.add(item);
		
		if (size == 0) {
			size = item.size();
		}
		else {
			if (item.size() != 0) {
				size = size * item.size();
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return loopList.iterator();
	}
	
	public boolean isEmpty() {
		return size <= 0;
	}
	
	public int size() {
		return size;
	}
	
	public int getDimensionSize() {
		return loopList.size();
	}
}
