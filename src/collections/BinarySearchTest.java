package collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinarySearchTest {
	
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<>();
		list.add("d");
		list.add("c");
		list.add("a");
		list.add("b");
		
		System.out.println(Collections.binarySearch(list, "c"));
		System.out.println(Collections.binarySearch(list, "d"));
		System.out.println();
		
		Collections.sort(list);
		System.out.println(Collections.binarySearch(list, "c"));
		System.out.println(Collections.binarySearch(list, "d"));
		
	}

}
