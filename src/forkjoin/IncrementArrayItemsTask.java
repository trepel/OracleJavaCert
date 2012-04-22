package forkjoin;

import java.util.concurrent.RecursiveAction;

public class IncrementArrayItemsTask extends RecursiveAction {

    private static final long serialVersionUID = 1L;
    
    private final static int THRESHOLD = 5;
    final long[] array;
    final int lo;
    final int hi;

    public IncrementArrayItemsTask(long[] array, int lo, int hi) {
        this.array = array;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    protected void compute() {
        System.out.println("Thread: " + lo + " " + hi);
        if (hi - lo <= THRESHOLD) {
            for (int i = lo; i < hi; ++i)
                array[i]++;
        } else {
            int mid = (lo + hi) >>> 1;
            
        // this is convenient way if using RecursiveAction, that returns no result
            invokeAll(new IncrementArrayItemsTask(array, lo, mid), new IncrementArrayItemsTask(array, mid, hi));
        
            //this is convenient way if using RecursiveTask<T> that returns T as a result
//            IncrementArrayItemsTask iait1 = new IncrementArrayItemsTask(array, lo, mid);
//            IncrementArrayItemsTask iait2 = new IncrementArrayItemsTask(array, lo, mid);
//            
//            iait1.fork(); // call compute method in different thread
//            T subResult1 = iait2.compute();
//            T subResult2 = iait1.join(); // waits for result of compute method and returns it
            // merge/combine somehow the two subresults to final result and return it
        }
    }
}
