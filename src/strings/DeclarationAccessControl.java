package strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DeclarationAccessControl {

	// 1.5.1 099 // int is automatically initialzed by 0 (zero)

	// 2 strictfp - it exists, it is about floating point (fp in the end)
	// 3 easy -> static can't reference non-static stuff
	// 4 last three options
	// 5 easy, usage of this in static context
	// 6 easy, no catch here
	// 7 difficult one (correct answer is at the bottom
	
	// not verified yet
	// 8 all but abstract and final option
	// 9 		-|| -
	// 10 last option, invalid call of constructor Sheet() 
	// 11 all but last two
	// 12 all but 3rd and 4th
	// 13 first one

	class A {
		A() {
			System.out.println("A");
		}
		
		final void aa() {}
	}
	
	class B extends A {
		B() {
			System.out.println("B");
		}
		
		void aa(String b) { final int c = 2;}
	}
	
	public static void main(String[] args) throws IOException {
		new DeclarationAccessControl().new B();

	}
	
}


// 7 line4 constructor of Account class is visible only in package trunk1 (no
// modifier), can't be instantiated there
// 7 line2 -||- , there is implicit call of super()