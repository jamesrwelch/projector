package ox.softeng.projector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Tester {

	static ObjectMapper jacksonMapper = new ObjectMapper().findAndRegisterModules();

	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TestClassA a = new TestClassA();
		a.setName("a");
		a.setAge(18);
		TestClassB b = new TestClassB();
		b.setName("b");
		a.setB(Arrays.asList(b));
		JsonNode db = Projector.project(a, "proj2", null);
		jacksonMapper.writeValue(System.out, db);
	}
	
	
	
	
}
