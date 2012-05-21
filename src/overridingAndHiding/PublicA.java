package overridingAndHiding;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PublicA {

    //overloading
    public void draw(String s){}
    public void draw(Integer i){}
    public void draw(Object o){}
//    public Object draw(Object o) throws Excepiton{} 
    // can't be, cos overloading only looks at method signature (name + input parameters (type and number))
    
    public static void main(String[] args) {

        A a = new A();
        B b = new B();
        A ab = new B();

        A.hidden();
        B.hidden();
        System.out.println();

        System.out.println(a.name);
        System.out.println(b.name);
        System.out.println(ab.name); // name from A class is used, cos ab is declared as A type
        System.out.println();

        
        System.out.println(a.m(null, null));
        System.out.println(b.m(null, null));
        System.out.println(ab.m(null, null));

    }

}

// overriding
class A {

    static void hidden() {
        System.out.println("hidden in A");
    }

    String name = "A";

    Object m(Object o, Number n) {
        System.out.println("m from A class");
        return null;
    }

    Object mm(Object o) throws IOException {
        System.out.println("mm from A class");
        return null;
    }

}

class B extends A {

    String name = "B";

    // @Override // can't be override annotation here, this is just hiding
    static void hidden() {
        System.out.println("hidden in B");
    }

    @Override
    // private Object m(Object o, Number n) {return null;} // false due to reducing visibility
    protected Integer m(Object oo, Number nn) {
        System.out.println("m from B class, name: " + name + ". Name from A is: " + super.name);
        return null;
    } // OK - I can return subtype, name of param can be changed
    // protected void m(Object o, Number n) {return;} // false, can't return void
    // protected Number m(Integer o, Number n) {return null;} // false - arguments must be of exactly the same type
    // protected Number m(Object o, Number n) throws Exception {return null;} // false due to throws clause

    @Override
    // public Integer mm(Object i) throws Exception {return null;} // false, cannot 'widen' the exception type
    public Integer mm(Object i) throws FileNotFoundException {
        System.out.println("mm form B class");
        return null;
    } // OK, FNFE is subclass of IOE
}