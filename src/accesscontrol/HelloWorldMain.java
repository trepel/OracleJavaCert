package accesscontrol;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HelloWorldMain {

    public static String createMessage(String message) {
        return "Message: " + message;
    }

    public static void main(String[] args) {
        Person p = new Person(args[0], args[1]);
        System.out.println(p.toString());
        
        Set<Person> persons = new HashSet<>();
        persons.add(p);
        persons.add(new Person("joseph", "carrot"));
        
        for (Person person : persons) {
            System.out.println("Hi, " + person.toString());
        }
    }

}
