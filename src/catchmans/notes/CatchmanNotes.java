package catchmans.notes;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import nio.NioTester;
import nio.WatcherHandler;

import forkjoin.IncrementArrayItemsTask;

public class CatchmanNotes {

    public static void outputDelimiter() {
        System.out.println();
        System.out.println("=======================================================================");
        System.out.println();
    }
    
    public void simpleDateFormatTest() {

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd"); // this is standart pattern - note the case

        Date date = null;
        try {
            date = sdf.parse("2011-11-03");
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        System.out.println(sdf.format(date));

        sdf.applyPattern("MM");
        System.out.println(sdf.format(date)); // 11
        sdf.applyPattern("MMM");
        System.out.println(sdf.format(date)); // Nov
        sdf.applyPattern("MMMM");
        System.out.println(sdf.format(date)); // November

    }

    public void copyOnWriteSomethingTest() {

        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        list.add("vivek");
        list.add("kumar");
        Iterator i = list.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
            list.add("abhishek"); // executed two times, for vivek and for kumar
        }
        System.out.println("After modification:");
        Iterator i2 = list.iterator();
        while (i2.hasNext()) {
            System.out.println(i2.next());
        }

        System.out.println("Example:");
        i = list.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
            list.add("mashik");
//            i.remove(); // throws UnsupportedOperationException
            list.remove("mashik");
        }

    }
    
    private void loadingDriverTest() {
        // TODO : setup db and verify driver loading in real app [if time allows]
//        DriverManager.registerDriver(new org.apache.derby.jbdc.Driver30()); // the Driver30 is for derby
        
//        Class.forName("org.apache.derby.jbdc.Driver30");
        
        // via command line: -Djbdc.drivers=org.apache.derby.jbdc.Driver30:org.mydriver.Driver
        
    }
    
    private void resourceBundleTest() {
        
        System.out.println(Locale.getDefault().toLanguageTag());
        
        Locale[] supportedLocales = { new Locale("fr", "CA", "UNIX"), new Locale("en", "GB"), new Locale("cs"),
                Locale.FRENCH,
                Locale.GERMAN,
                Locale.ENGLISH
             };
        
        ResourceBundle bundle = ResourceBundle.getBundle("bundle", supportedLocales[0]);
        System.out.println(bundle.getString("name"));
        
        bundle = ResourceBundle.getBundle("bundles.bundle", supportedLocales[0]);
        System.out.println(bundle.getString("name"));
        
//        System.out.println(bundle.getObject("1")); MissingResourceException: can't find resource for bundle, key 1
    }
    
    public void printArray(long[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
    
    private void forkjoinTest() {
        
        int maxLenght = 10;
        long[] array = new long[maxLenght];
        for (long i = 0; i < maxLenght; i++ ) {
            array[(int)i] = i;
        }
        
        RecursiveAction incrementTask = new IncrementArrayItemsTask(array, 0, array.length);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(incrementTask);
        
        printArray(array);
        
    }
    
    private void testRunnableAndCallable() {
        MyRunnable mr = new MyRunnable();
        MyCallable mc = new MyCallable();
        
        String mcCallResult = null;
        try {
            mcCallResult = mc.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Thread.mr.run();
        Thread t = new Thread(mr);
//        t.start();
        System.out.println(mcCallResult);
        // if calling directly run method of either Runnable or Thread instance, 
        // that doesn't ensure paralelism, this thread waits for run thread to finish and after that it prints the call result
        // If call start() method of Thread, this Thread instance does't wait for t Thread to finish its run() method
    }
    
    public <T,V> T doNothingButReturn(Callable<V> task, T tInstance) {
        return tInstance;
    }
    
    public void testCastException() {
        
        AC ac = new BC();
        try {
        ac = (CC) ac;
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
        
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        CatchmanNotes cn = new CatchmanNotes();
        
        System.out.println(cn.doNothingButReturn(new MyCallable(), "tInstance").getClass());

        cn.simpleDateFormatTest();
        CatchmanNotes.outputDelimiter();
        cn.copyOnWriteSomethingTest();
        CatchmanNotes.outputDelimiter();
        cn.loadingDriverTest();
        CatchmanNotes.outputDelimiter();
        cn.resourceBundleTest();
        CatchmanNotes.outputDelimiter();
        cn.forkjoinTest();
        CatchmanNotes.outputDelimiter();
        cn.testRunnableAndCallable();
        CatchmanNotes.outputDelimiter();
        cn.testCastException();
        CatchmanNotes.outputDelimiter();
        
        NioTester nioT = new NioTester();
        nioT.pathTest();
        CatchmanNotes.outputDelimiter();
        
        WatcherHandler wh;
        try {
            wh = new WatcherHandler();
            wh.register(Paths.get("/home/trepel/OracleJavaCert/watchTesting"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        new Thread(wh).start();
        
    }


}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Run in runnable, can't return a value");
        CatchmanNotes.outputDelimiter();
//        throw new Exception("Checked exception in runnable");
        
    }
    
}

class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        
        System.out.println("Call in callable, return string: XXX");
        return "XXX";
    }
    
}

interface I { void method(); }
class AI { public void method() {} } // compile error without public, cos every method in interface is public abstract automatically
// AI class can have implements I clausule but doesn't have to, no compile error occurs
class BI extends AI implements I { }


interface AC {} // the same excepiton occurs if the AC is class and BC and CC extend it
class BC implements AC {}
class CC implements AC {}

interface soundable { void makeSound(); }
enum Animals implements soundable { // enum can implement interface, can have private or without-modifier constructor ONLY, is comparable
    
    DOG("haf"),
    CAT("mnau");
    
    String sound;
    Animals(String sound) {
        this.sound = sound;
    }
    
    public void makeSound() { System.out.println(sound); }

}
