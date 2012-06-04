package executors;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExcecutorsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService e = Executors.newFixedThreadPool(3);
		
		Set<Callable<String>> callables = new HashSet<Callable<String>>();
		Set<Future<String>> futures = new HashSet<Future<String>>();
		callables.add(new Callable<String>() {
		    public String call() throws Exception {
		        return "Task 1";
		    }
		});
		callables.add(new Callable<String>() {
		    public String call() throws Exception {
		        return "Task 2";
		    }
		});
		callables.add(new Callable<String>() {
		    public String call() throws Exception {
		        return "Task 3";
		    }
		});
		
		for (Callable<String> callable : callables) {
			futures.add(e.submit(callable));
		}
		
		for (Future<String> future : futures) {
			try {
				System.out.println(future.get());
			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			}
		}
		
		e.shutdown();
	}

}
