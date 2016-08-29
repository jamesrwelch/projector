package ox.softeng.projector;

import java.util.List;

import ox.softeng.projector.annotations.Projectable;
import ox.softeng.projector.annotations.Projection;

@Projectable
public class TestClassA {

	@Projection(name="proj1", classes={TestClassA.class})
	@Projection(name="proj2", classes={})
	private String name;
	
	
	@Projection(name="proj2", classes={})
	private Integer age;
	
	@Projection(name="proj2")
	private List<TestClassB> b; 
	
	TestClassA(){
		
	}

	public String getName() {
		return "Getter: " + name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public List<TestClassB> getB() {
		return b;
	}

	public void setB(List<TestClassB> b) {
		this.b = b;
	}
	
	
}
