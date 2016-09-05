package ox.softeng.projector.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(Projections.class)
public @interface Projection {
	String name() default "";
	Class<?>[] classes() default {};
	boolean always() default false;
	String recurseProjection() default "";
}
