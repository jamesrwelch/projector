package ox.softeng.projector.test;

import ox.softeng.projector.CallableFilter;

public class TestFilter extends CallableFilter<Object> {

	
	public TestFilter(Object objectToFilter) {
		super(objectToFilter);
		
	}

	@Override
	public Boolean call() throws Exception 
	{
		if(getObjectToFilter().getClass() == TestClassA.class)
		{
			return (((TestClassA)getObjectToFilter()).getAge() > 18);
		}
		else
		{
			return true;
		}
	}

}
