package accesscontrol;

public class Person {
    
    String firstName;
    String lastName;
    
    public Person(String f, String l) {
        firstName = f;
        lastName = l;
    }
    
    public String toString() {
        return firstName + " " + lastName;
    }

}
