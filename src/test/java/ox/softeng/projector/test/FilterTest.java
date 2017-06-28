package ox.softeng.projector.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ox.softeng.projector.Projector;


public class FilterTest {

	static ObjectMapper jacksonMapper = new ObjectMapper().findAndRegisterModules();

	@Test
	public void basicTest() throws Exception
	{
		TestClassA a = getExample();
		JsonNode db = Projector.project(a, "proj2");
		jacksonMapper.writeValue(System.out, db);
		assertTrue(db.get("name").asText().equals("Getter: a"));
		assertTrue(db.get("b").get(0).get("name").asText().equals("b"));
		assertTrue(db.get("age") == null);
	}
	
	@Test
	public void filterTest() throws Exception
	{
		
		TestClassA a = getExample();
		JsonNode db = Projector.project(a, "proj2", new TestFilterFactory());
		jacksonMapper.writeValue(System.out, db);

		assertTrue(db == null);

		
		a.setAge(20);
		db = Projector.project(a, "proj2", new TestFilterFactory());
		jacksonMapper.writeValue(System.out, db);
		
		assertTrue(db.get("name").asText().equals("Getter: a"));
		assertTrue(db.get("b").get(0).get("name").asText().equals("b"));
		assertTrue(db.get("age") == null);
		
	}
	
	TestClassA getExample()
	{
		TestClassA a = new TestClassA();
		a.setName("a");
		a.setAge(18);
		TestClassB b = new TestClassB();
		b.setName("b");
		a.setB(Arrays.asList(b));

		return a;
		
		
	}
	
	
	
	
}
