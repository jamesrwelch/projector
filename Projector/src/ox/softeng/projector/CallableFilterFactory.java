package ox.softeng.projector;

import java.util.concurrent.Callable;

public abstract class CallableFilterFactory<T> implements Callable<Boolean> {

	abstract public CallableFilter<T> newCallableFilter(T object);
	
}
