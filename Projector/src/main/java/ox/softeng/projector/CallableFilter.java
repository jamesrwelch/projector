package ox.softeng.projector;

import java.util.concurrent.Callable;

public abstract class CallableFilter<T> implements Callable<Boolean> {

	
	private T objectToFilter;
	
	public CallableFilter(T objectToFilter)
	{
		this.objectToFilter = objectToFilter;
	}
	
	public T getObjectToFilter()
	{
		return objectToFilter;
	}
	
}
