package strings;

public class InterfacesAbstractClasses {

}

interface Bendable { // line 1
	final int x = 2009; // line 3
	int y = 5;
	public static final int z = 2; // only this three modifiers are allowed
	
	void get();
	
	void getString();

	void method1(); // line 5
	
	public static class Angle {}
}

abstract class BendableObject {
	
	abstract void getB();
//	public abstract static void getC(); // compile error for some reason, investigate
}


// NOT VERIFIED
// 1 it is free from compilation error
// 2 no, yes, ab & pub, no, ab & pub
// 3 1 and 3
// 4 1
// 5 1
// 6 d - only pub, static, final allowed
// 7 true, false, true, true, true
// 8 1, 4, 5
// 9 1,2
// 10 1,5
//11 4
//12 1,3, 5, 6
