package componentexample;


import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;



public class Reflection {
	private JTextArea textArea;
	private JTextArea textArea2;
	private JTextArea textArea3;
	private JTextArea textArea4;
	private Map<String, Property> properties;
	private Map<String, EventSet> events = new HashMap<String, EventSet>();


	public Reflection(JTextArea textArea, JTextArea textArea2,JTextArea textArea3, JTextArea textArea4, Map<String, Property> properties){
		this.textArea = textArea;
		this.textArea2 = textArea2;
		this.textArea3 = textArea3;
		this.properties = properties;
		this.textArea4 = textArea3;
		
	}
	
	
	public void printToTextArea(String className){
		try {
			textArea.setText(null);
			textArea2.setText(null);
			Class myClass = Class.forName(className);
			getFields(myClass);
			Method methods[] = myClass.getMethods();
			events.clear();
			properties.clear();
			for(int i = 0; i<methods.length; i++){
				getProperty(methods[i]);
				Class<?> klass[] = methods[i].getParameterTypes();
				
				textArea.append(Modifier.toString(methods[i].getModifiers()) + " " + methods[i].getReturnType().getSimpleName()
						+ " "+ methods[i].getName() + "("); 
				printParameters(klass);
				textArea.append(");" +  "\n\n");
				getEventSet(methods[i]);
			}			
			for(Map.Entry<String, Property> entry : properties.entrySet()){
				String key = entry.getKey();
				Property value = entry.getValue();
				textArea2.append(key + " " + value.getType() + "  " + value.getEncapsulation() + "\n\n");
			}

		}
		catch (ClassNotFoundException e) {
			System.out.println("no class");
		}
	}
 

	public void getProperty(Method methodName){
		String name = methodName.getName();
		Pattern setMethodPattern = Pattern.compile("^[gs]et(\\w+)");
		Matcher matcher = setMethodPattern.matcher(name);
		String word = null;
		if(matcher.matches()){
			char lcase = matcher.group(1).toLowerCase().charAt(0);
			word = matcher.group(1);
			word = word.replace(matcher.group(1).charAt(0), lcase);
			if(properties.containsKey(word)){
				properties.get(word).setMethod(methodName);
			} else if(!properties.containsKey(word)){
				properties.put(word, new Property(methodName, word));
			}
		} 
	}
	
	public String printParameters(Class<?>[] klass){
		String name = null;
		for(int j = 0; j<klass.length; j++){
			textArea.append(" " + klass[j].getSimpleName() + " ");
		}
		return name;
	}
	

	public void getEventSet(Method methodName){
		String name = methodName.getName();
		Pattern setMethodPattern = Pattern.compile("^add(\\w+)Listener$");
		Matcher matcher = setMethodPattern.matcher(name);
		String word = null;
		if(matcher.matches()){
			char lcase = matcher.group(1).toLowerCase().charAt(0);
			word = matcher.group(1);
			word = word.replace(matcher.group(1).charAt(0), lcase);
			if(!events.containsKey(word)){
				events.put(word,new EventSet(methodName, word));
			}
		}
	}
	
	public Map<String, EventSet> getEvents(){
		return events;
	}
	
	public void getFields(Class klass){
		int num = klass.getFields().length;
		//if(klass.getFields().length = null){
		String length = "" + num;
		if(!length.isEmpty()){
			textArea3.setText("The class has" +  length  + "fields");
		} else{
			textArea3.setText("The class has no fields");
		}
	}
	
	public void checkNoArgueMentConstructor(Class klass){
		Constructor[] constructor = klass.getConstructors();
		boolean noArguement = false;
		int i = 0;
		for(int j = 0; j<constructor.length; j++){
			Class<?>[] cklass = constructor[i].getParameterTypes();
			if(cklass[0].getName()== null){
				noArguement = true;
			}
		}
		if(noArguement == true){
			
		}
	}
	
}

