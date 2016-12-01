package ox.softeng.projector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ox.softeng.projector.annotations.Projectable;
import ox.softeng.projector.annotations.Projection;
import ox.softeng.projector.annotations.Projections;

public class Projector {

	public static final JsonNodeFactory factory = JsonNodeFactory.instance;
	
	public static <T> JsonNode project(T inputObject, String projectionName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		return project(inputObject, projectionName, null);
	}
	
	
	public static <T> JsonNode project(T inputObject, String projectionName, CallableFilterFactory<T> filterFactory) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		//System.out.println("Projecting... " + projectionName);
		if(inputObject == null)
		{
			//System.err.println("null object passed to Projector.project!");
			return null;
		}
		if(filterFactory != null)
		{
			CallableFilter<T> filter = filterFactory.newCallableFilter(inputObject);
			try {
				if(!filter.call())
				{
					// We're not allowed to view this object!
					return null;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		ObjectNode on = factory.objectNode();
		for(Field f : getAllFields(new ArrayList<Field>(), inputObject.getClass()))
		{
			//System.out.println(f.getName());
			if(isProjectable(f, projectionName, inputObject.getClass()))
			{
				String recurseProjection = getRecurseProjection(f, projectionName);
				Object value = PropertyUtils.getSimpleProperty(inputObject, f.getName());
				if(value == null)
				{
				
					on.put(f.getName(), (String) null);
				}
				else
				{
					//System.out.println(f.getType());
					Class<?> fieldType = f.getType();
					if(fieldType == String.class)
					{
						on.put(f.getName(), value.toString());
					}
					else if(fieldType == Integer.class)
					{
						on.put(f.getName(), (Integer) value); 
					}
					else if(fieldType == BigDecimal.class)
					{
						on.put(f.getName(), (BigDecimal) value);
					}
					else if(fieldType == Boolean.class)
					{
						on.put(f.getName(), (Boolean) value);
					}
					else if(isProjectableClass(fieldType))
					{
						//System.out.println("projectable class: " + fieldType);
						on.set(f.getName(), project((T) value, recurseProjection, filterFactory));
					}
					else if(Collection.class.isAssignableFrom(fieldType))
					{
						//System.out.println("We gotta list...");
						ArrayNode an = factory.arrayNode();
						for(T o : (Collection<T>)value)
						{
							an.add(project(o, recurseProjection, filterFactory));
						}
						on.set(f.getName(), an);
					}
					else
					{
						on.put(f.getName(), value.toString());
					}
				}
			}
		}
		return on;

	}

	public static <T> ArrayNode project(Collection<T> inputObjects, String projectionName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		return project(inputObjects, projectionName, null);
	}
	
	public static <T> ArrayNode project(Collection<T> inputObjects, String projectionName, CallableFilterFactory<T> filterFactory) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		ArrayNode an = factory.arrayNode();
		for(T inputObject : inputObjects)
		{
			an.add(project(inputObject, projectionName, filterFactory));
		}
		return an;
	}
		
	private static boolean isProjectableClass(Class<?> cls)
	{
		Projectable p = cls.getDeclaredAnnotation(Projectable.class);
		if(p != null)
		{
			return true;
		}
		return false;
	}
	
	private static boolean isProjectable(Field field, String projectionName, Class<?> projectionClass)
	{
		Projection[] ps = getProjections(field);
		if(ps != null)
		{
			for(Projection p : ps)
			{
				if(p.always())
				{
					return true;
				}
				if(p.name().equalsIgnoreCase(projectionName) && isValidClass(projectionClass, p.classes()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private static String getRecurseProjection(Field field, String projectionName)
	{
		Projection[] ps = getProjections(field);
		if(ps != null)
		{
			for(Projection p : ps)
			{
				if(p.name().equalsIgnoreCase(projectionName))
				{
					if(p.recurseProjection() != null && !"".equals(p.recurseProjection()))
					{
						return p.recurseProjection();
					}
					else return projectionName;
				}
				if(p.always())
				{
					if(p.recurseProjection() != null && !"".equals(p.recurseProjection()))
					{
						return p.recurseProjection();
					}
					else return projectionName;
				}
			}
		}
		return projectionName;
	}
	
	private static boolean isValidClass(Class<?> projectionClass, Class<?>[] allowedClasses)
	{
		if(allowedClasses == null)
		{
			return true;
		}
		if(allowedClasses.length == 0)
		{
			return true;
		}
		for(Class<?> c : allowedClasses)
		{
			if(projectionClass == c || c.isAssignableFrom(projectionClass))
			{
				return true;
			}
		}
		return false;
	}
	
	private static Projection[] getProjections(Field field)
	{
		Projections ps = field.getAnnotation(Projections.class);
		if(ps != null)
		{
			return ps.value();
		}
		Projection p = field.getAnnotation(Projection.class);
		if(p != null)
		{
			return new Projection[] {p};
		}
		return null;
		
	}
	
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
	
}
