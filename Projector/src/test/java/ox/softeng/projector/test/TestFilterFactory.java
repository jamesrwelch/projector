package ox.softeng.projector.test;

import ox.softeng.projector.CallableFilter;
import ox.softeng.projector.CallableFilterFactory;

public class TestFilterFactory extends CallableFilterFactory<Object> {

	@Override
	public CallableFilter<Object> newCallableFilter(Object object) {
		return new TestFilter(object);
		
	}

}
