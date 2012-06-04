package quizfrompdf;

public class FlowControl {

	public static void main1(String[] args) {
		// instantiation 1
		Engine engine1 = new FlowControl().new Engine();
		// instantiation 2
		FlowControl.Engine engine2 = new FlowControl().new Engine();
		// instantiation 3
		FlowControl airjet = new FlowControl();
		Engine engine3 = airjet.new Engine();
		// instantiation 4
		// Engine engine4 = new Engine();
		// sure, I don't have to write InnerClasses.Engine due to fact that I am
		// inside InnerClasses
		// but still I have to specify the exact instance of InnerClasses for
		// Engine instance
	}

	class Engine {
	}

	public void fly(int speed) {
		final int e = 1;
		int f = 2;
		class FlyingEquation {
			{
				System.out.println(e);// line 1
				// System.out.println(speed);// line 2
				// System.out.println(f);// line 2
				// An inner class defined inside a method cannot access the
				// non-final method local variables
			}
		}
	}

	public static void main(String args[]) {
		int a = 5;
		outside: for (int i = 1; i < 3; i++) {
			inside: for (int j = 1; j < 3; j++) {
				System.out.print(j);
				if (a++ > 6)
					continue inside;
				break outside;
			}
		}
		System.out.println();

		boolean flag = false;
		int x = 0;
		do {
			x++;
		} while (flag = !flag); // this is possible without compile error. The
								// assign command apparently returns the value
								// from right side
		// that is why int x=y=z=5 works - works even for objects

		System.out.println(x);
		String y;
		String z = y = "aaa";
		System.out.println(z);

		// while (false) {
		// System.out.print("while");
		// }
		// compilation error, unreachable code

		final Integer aa = 10;
		int b = 2;
		switch (b) {
		case 1:
			System.out.println("1");
			break;
		case 2:
			System.out.println("2");
			break;
		case 3: // cant be 'aa' here, because only constants are allowed (it
				// doesn't matter it is final)
			System.out.println("3");
			break;
		}

		if (true) {
			if (false) {
				System.out.println("aaa");
			} else {
			}
		} else {
			if (true) {
			}
		}
		// this is OK, only dead code warning, no unreachable code error
		// stmt2 (not here) is wrong, assignment rather condition in if

		// 1.4.7 -> 2

		int jjjj = 10;
		switch (1) {
		case 20:
			jjjj += 1;
		case 40:
			jjjj += 2;
		default:
			jjjj += 3;
		case 0:
			jjjj += 4;
		}
		System.out.println(jjjj); // constant can be in switch() condition

		String[] arr = { "one", "two" };
		if (arr.length > 0)
			for (String str : arr)
				System.out.print(str);
		System.out.println("--");// program name is NOT in the args

		for (int i = 4; i <= 5; i++) {
			switch (i) {
			case 5: {
				System.out.println("match");
			}
			default: {
				System.out.println("default");
			}
			}
		}

		// 1.4.11 -> Compile error semicolon instead of colon is used

		a = 15;
		outside: for (int i = 0; i < 3; i++) {// line 4
			inside: System.out.print(i); // line 5
			for (int j = 1; j < 3; j++) {
				if (a > 5)
//					continue inside;// line 8 // hard to say what is really wrong here, but maybe just inside labels the normal statement
					//not the loop
				break outside; // line 9
			}
		}
		
		// 1.4.13 -> 111
		//		14 -> no output is produced

	}

	// Switch statement works with short, byte, char, int (and its wrapper
	// objects), Enum and String

}
