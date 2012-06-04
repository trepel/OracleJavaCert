package trywithresources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TryWithResourcesTester {
	
	public static void main(String[] args) {
		
		tryTheTryBlockBehavior();
    }
	
	public static void tryTheTryBlockBehavior() {
		
		File f = new File("./pathTesting/subdir1/file1.txt");
        try (FileReader fr = new FileReader(f);
        		BufferedReader br = new BufferedReader(fr);
        		ResourceOne resOne = new ResourceOne(); // the resources are closed in opposite order as they were defined
        		ResourceTwo resTwo = new ResourceTwo(); // the last ';' is not required, but can be in place
//        		String s = new String() // can't be, String doesn't implement AutoCloseable interface
        		) {
            System.out.println("line readed: " + br.readLine());
            fr.close(); // duplicate closing doesn't throw any excepiton
            fr.close();
            
            throw new IllegalArgumentException("IllegalArgumentException from try block");
            
        } catch (IOException | MyCloseException e) { //multi-catch block, e is automatically final
            // typically, this catches IOExcepiton from both try-block and resource-closing
        	// in this particular case, resource-closing throws my custom exception, this can be defined here too
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
        	System.out.println(e.getMessage());
        	System.out.println("Suppressed exception: " + e.getSuppressed()[0].getMessage());
        } finally {
            // try-with resources can have finally block, but resource-closing is executed before this block
        	System.out.println("This should be written after closing messages of both Resource One and Resource Two");
        }        
    }

}

class ResourceOne implements AutoCloseable {

	@Override
	public void close() throws MyCloseException {
		System.out.println("Resource ONE is closing itself");
	}
	
}

class ResourceTwo implements AutoCloseable {

	@Override
	public void close() throws MyCloseException {
		System.out.println("Resource TWO is closing itself");
		throw new MyCloseException("Closing failed"); 
	}
	
}

class MyCloseException extends Exception { public MyCloseException(String message){super(message);}}
