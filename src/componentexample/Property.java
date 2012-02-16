package componentexample;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Property {
	private Method accessor;
	private Method mutator;
	private String readWrite;
	private String name;

	
	
	
	public Property(Method method, String name){
		setMethod(method);
		this.name = name;
		setAccess();
	}
	
	
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		Class<?>[] type;
		Class klass;
		String propertyType = null;
		if(accessor == null){
			type = mutator.getParameterTypes();
			klass = type[0].getClass();
			propertyType = klass.getSimpleName();
		}else{
			klass = accessor.getReturnType();
			propertyType = klass.getSimpleName();
		}
		return propertyType;
	}
	
	public String getEncapsulation(){
		return readWrite;
	}
	
	public void setAccess(){
	if(accessor == null){
		readWrite = "write only";
	}
	else if(mutator == null){
		readWrite = "read only";
	} else {
		readWrite = "read/write";
	}
	}
	
	public void setMethod(Method method){
		if(!method.getReturnType().getSimpleName().equalsIgnoreCase("void")){
			accessor = method;
		} else {
			mutator = method;
		}
		setAccess();
	}
	

}
