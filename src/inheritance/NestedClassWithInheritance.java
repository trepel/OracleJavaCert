package inheritance;

public class NestedClassWithInheritance {
    
    private int a = 10;
    
    public class Nested {
        
        public void m () {
            System.out.println(a); // nested classes has access to all enclosing class members (even the private ones)
        }
        
        public int getA() {
            return a;
        }
        
    }

}

class SubClass extends NestedClassWithInheritance {
    
    void mm() {
//        a = 20; // compile error, a is not visible in subclass, because it is private
        
        Nested nested = new Nested();
        nested.getA(); // but I can indirectly get the a value due to public/protected nested class
        
        I i = new B(); // this is OK, cos B extends A that implements I, B doesn't need to implement I directly
        i.g();
    }
    
}

interface I { void g(); }
abstract class A implements I{}
class B extends A { public void g() {} } // has to be public, cos g() in interface is public implicitly


