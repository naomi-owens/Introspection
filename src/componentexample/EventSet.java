package componentexample;

import java.lang.reflect.Method;

public class EventSet {
	
	private Method method;
	private String eventName;
	private Method[] events;
	private boolean adapter = false;

	
	
	

	public EventSet(Method add,String eventName){
		this.method = add;
		this.eventName = eventName;
		setEvent(add);
	}
	
	public void setEvent(Method method){
		Class<?>[] klass = method.getParameterTypes();
		events = klass[0].getMethods();
	}
	
	public String getEventName(){
		return eventName;
	}
	
	
	public Method[] getEvents(){
		return events;
	}
	
	public void setAdapter(boolean adapter){
		this.adapter = true;
	}


}
