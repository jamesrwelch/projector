package ox.softeng.projector;


public abstract class CallableFilterFactory<T> {

	abstract public CallableFilter<T> newCallableFilter(T object);
	
}
