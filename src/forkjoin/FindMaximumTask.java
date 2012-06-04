package forkjoin;

import java.util.concurrent.RecursiveTask;

public class FindMaximumTask extends RecursiveTask<Long>{
	
	private static final long serialVersionUID = 1L;
    
    private final static int THRESHOLD = 5;
    final long[] array;
    final int lo;
    final int hi;
    
    public FindMaximumTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

	@Override
	protected Long compute() {
		
		System.out.println("Thread: " + lo + " " + hi);
        if (hi - lo <= THRESHOLD) {
            return findMaximum();
        } else {
            int mid = (lo + hi) >>> 1;
            
        // this is convenient way if using RecursiveAction, that returns no result
//            invokeAll(new FindMaximumTask(array, lo, mid), new FindMaximumTask(array, mid, hi));
        
            //this is convenient way if using RecursiveTask<T> that returns T as a result
            FindMaximumTask fm1 = new FindMaximumTask(array, lo, mid);
            FindMaximumTask fm2 = new FindMaximumTask(array, mid, hi);
            
            fm1.fork(); // call compute method in different thread
            Long subResult1 = fm2.compute();
            Long subResult2 = fm1.join(); // waits for result of compute method and returns it
            
            // merge/combine somehow the two subresults to final result and return it
            return subResult1 > subResult2 ? subResult1 : subResult2;
        }
	}
	
	private Long findMaximum() {
		long temp = array[0];
		for (int i = lo; i < hi; i++) {
			if (array[i] > temp) {
				temp = array[i];
			}
		}
		return Long.valueOf(temp);
	}

}
