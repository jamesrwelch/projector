package ox.softeng.projector;

import ox.softeng.projector.annotations.Projectable;
import ox.softeng.projector.annotations.Projection;

@Projectable
public class TestClassB {

	@Projection(name="proj2", classes={TestClassB.class})
	private String name;
	
	
	TestClassB(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
