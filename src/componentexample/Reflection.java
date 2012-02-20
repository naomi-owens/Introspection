package componentexample;


import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;



public class Reflection {
	private JTextArea textArea;
	private JTextArea textArea2;
	private JTabbedPane tabPane;
	private Map<String, Property> properties;
	private Map<String, EventSet> events = new HashMap<String, EventSet>();
	private DefaultMutableTreeNode[] eventsArray;


	public Reflection(JTextArea textArea, JTextArea textArea2, Map<String, Property> properties){
		this.textArea = textArea;
		this.textArea2 = textArea2;
		this.properties = properties;
		
	}
	
	
	public void printToTextArea(String className){
		try {
			textArea.setText(null);
			textArea2.setText(null);
			Class myClass = Class.forName(className);
			Method methods[] = myClass.getMethods();
			for(int i = 0; i<methods.length; i++){
				String property =methods[i].getName();
				getProperty(methods[i]);
				Class<?> klass[] = methods[i].getParameterTypes();
				
				textArea.append(Modifier.toString(methods[i].getModifiers()) + " " + methods[i].getReturnType()
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
			} else if(!properties.containsKey(word) && word != null){
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
	
	public void printEvents(Method [] events){
		String name = null;
		for(int j = 0; j<events.length; j++){
			DefaultMutableTreeNode event =  new DefaultMutableTreeNode(events[j].getName());
			eventsArray[j] = event;
		}
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
	
}

