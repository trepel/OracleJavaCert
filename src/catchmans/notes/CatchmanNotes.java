package catchmans.notes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nio.NioTester;
import nio.WatcherHandler;

import forkjoin.FindMaximumTask;
import forkjoin.IncrementArrayItemsTask;

public class CatchmanNotes {

    public static void outputDelimiter() {
        System.out.println();
        System.out.println("=======================================================================");
        System.out.println();
    }
    
    public static void outputDelimiter(String header) {
        System.out.println();
        System.out.println(header + "==============================================================");
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
        
        sdf.applyPattern("dddd");
        System.out.println(sdf.format(date));

    }

    public void copyOnWriteSomethingTest() {

        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        list.add("vivek");
        list.add("kumar");
        Iterator i = list.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
            list.add("abhishek"); // executed two times, for vivek and for kumar
            //if the normal arrayList is used there, the ConcurentModificationException is thrown, because this thread is trying 
            //both iterate over an arrayList and modifying the arrayList in the same time
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
            // i.remove(); // throws UnsupportedOperationException
            list.remove("mashik");
        }
        
        ArrayList<String> ar = new ArrayList<>();
        ar.add("one");
        ar.add("two");
        for (String item : ar) {
//			ar.add("three"); // ConcurrentModificationException, you cannot iterate and modify the arrayList at once
		}

    }

    private void loadingDriverTest() {
        // TODO : setup db and verify driver loading in real app [if time allows]
        // DriverManager.registerDriver(new org.apache.derby.jbdc.Driver30()); // the Driver30 is for derby

        // Class.forName("org.apache.derby.jbdc.Driver30");

        // via command line: -Djbdc.drivers=org.apache.derby.jbdc.Driver30:org.mydriver.Driver
    	
    	// see package database

    }

    private void resourceBundleTest() {

        System.out.println(Locale.getDefault().toLanguageTag());

        Locale[] supportedLocales = { new Locale("fr", "CA", "UNIX"), new Locale("en", "GB"), new Locale("cs"), Locale.FRENCH,
                Locale.GERMAN, Locale.ENGLISH };

        ResourceBundle bundle = ResourceBundle.getBundle("bundle", supportedLocales[0]);
        System.out.println(bundle.getString("name"));

        bundle = ResourceBundle.getBundle("bundles.bundle", supportedLocales[0]);
        System.out.println(bundle.getString("name"));
        
//        bundle = ResourceBundle.getBundle("nonexistingpathinbindirectory.bundles.bundle", supportedLocales[0]);
//        System.out.println(bundle.getString("name")); // MissingResourceException: can't find resource ...

        // System.out.println(bundle.getObject("1")); MissingResourceException: can't find resource for bundle, key 1
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
        for (long i = 0; i < maxLenght; i++) {
            array[(int) i] = i;
            if (i == 3) {
            	array[(int)i] = 49l;
            }
        }

        RecursiveAction incrementTask = new IncrementArrayItemsTask(array, 0, array.length);
        //not a good approach, I should ensure that only one instance of ForkJoinPool is created in the whole application
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(incrementTask);
        
        printArray(array);
        
        RecursiveTask<Long> findMaximum = new FindMaximumTask(array, 0, array.length);
        System.out.println("And the maximum is: " + pool.invoke(findMaximum));
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
        // Thread.mr.run();
        Thread t = new Thread(mr);
        // t.start();
        System.out.println(mcCallResult);
        // if calling directly run method of either Runnable or Thread instance,
        // that doesn't ensure paralelism, this thread waits for run thread to finish and after that it prints the call result
        // If call start() method of Thread t, this Thread instance does't wait for t Thread to finish its run() method (invoked via start() method)
    }

    public <T, V> T doNothingButReturn(Callable<V> task, T tInstance) {
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

    public void testShadowing() {
        AS a = new AS();
        AS b = new BS();
        System.out.println(a.name);
        a.printName();
        System.out.println(b.name); // prints AS, cos it is shadowed, you can't override property, only method
        b.printName();
        b.printName(""); // prints AS, cos printName is defined in ancestor, it doesn't see the descendand name property

        System.out.println(a.staticName);
        System.out.println(b.staticName); // again, shadowing of staticName, but I should invoke staticName on Class, not
                                          // instance

    }

    static void appendNewObject(List<?> list) {
//         list.add(new Object()); // compilation error! I don't understand why
    }

    public void testNumberFormatAPI() {
        NumberFormat gi = NumberFormat.getInstance();
        NumberFormat gii = NumberFormat.getIntegerInstance();
        NumberFormat gni = NumberFormat.getNumberInstance();

        System.out.println(gi.getClass().getName());
        System.out.println(gii.getClass().getName());
        System.out.println(gni.getClass().getName());

        // Print out a number using the localized number, integer, currency,
        // and percent format for each locale
        Locale[] locales = { Locale.ENGLISH, new Locale("cs") };
        double myNumber = -1234.56;
        NumberFormat form;
        for (int j = 0; j < 4; ++j) {
            System.out.println("FORMAT");
            for (int i = 0; i < locales.length; ++i) {
                // if (locales[i].getCountry().length() == 0) {
                // continue; // Skip language-only locales
                // }
                System.out.print(locales[i].getDisplayName());
                switch (j) {
                    case 0:
                        form = NumberFormat.getInstance(locales[i]);
                        break;
                    case 1:
                        form = NumberFormat.getIntegerInstance(locales[i]);
                        break;
                    case 2:
                        form = NumberFormat.getCurrencyInstance(locales[i]);
                        break;
                    default:
                        form = NumberFormat.getPercentInstance(locales[i]);
                        break;
                }
                if (form instanceof DecimalFormat) {
                    System.out.print(": " + ((DecimalFormat) form).toPattern());
                }
                System.out.print(" -> " + form.format(myNumber));
                try {
                    System.out.println(" -> " + form.parse(form.format(myNumber)));
                } catch (ParseException e) {
                }
            }
        }
    }

    public void permissionTest() {

        File allP = new File("./permissionTest/allPermissions.txt");

        File readP = new File("./permissionTest/readOnly.txt");

        File readWriteP = new File("./permissionTest/writeOnly.txt");

        try {
            // throws FNFE
            FileReader allPReader = new FileReader(allP);
            FileReader readPReader = new FileReader(readP);
            FileReader readWritePReader = new FileReader(readWriteP);
            FileReader[] arr = {allPReader, readPReader, readWritePReader};

            // throws IOE
            CharBuffer sb = CharBuffer.allocate(15);
            for (int j = 0; j < 3; j++) {
                int i = arr[j].read(sb);
                if (i != -1) {

                    System.out.println(i + ": " + new String(sb.array()));
                    sb.clear();
                } else {
                    System.out.println("EOF");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("FNFException is thrown:\n" + e.getMessage());
//            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException is thrown:\n" + e.getMessage());
        }

    }
    
    public void regexTest() {
        Pattern p = Pattern.compile("[\\s][a-c[p-q[r-r]]][\\s]end");// [a-b[c-d[e-f]]] - so called union, can be recursively nested
        Matcher m = p.matcher("blabla b end blabla r end blabla d end");
        
        while (m.find()) {
            System.out.println(m.group());
        }
    }
    
    public void scannerTest() {
        String input = " hello  hi,   I    am Thomas.\nHi, I am not Tomas"; 
        //first space skipped, more spaces behave like one. If commented below delimiter is used
        // the numberOfSpaces - 1 spaces get to the result 
        Scanner sc = new Scanner(input);//.useDelimiter("\\s");
        System.out.println(sc.delimiter().pattern());
        System.out.println("--------------------------------");
        while (sc.hasNext()) {
            System.out.println(sc.next()+"~");
        }
        
        sc.close(); // scanner has no rewind-like method
        System.out.println("--------------------------------");
        sc = new Scanner(input);
//        sc.findInLine("(\\w+)\\s+(\\w+),\\s+(\\w+)\\s+(\\w+)\\s+(\\w+)");//  only up to line separator
        System.out.println(sc.findInLine("(m.*m)"));
        sc.nextLine();
        System.out.println(sc.findInLine("(m.*m)")); // brackets are here for sc.match() method
        System.out.println("--------------------------------");
        MatchResult result = sc.match(); // see commented pattern, the every (\\w+) would be one result.group
        for (int i=1; i<=result.groupCount(); i++)
            System.out.println(result.group(i));
        sc.close();
        
//        System.out.println("Selec operation (add/disctract):");
//        sc = new Scanner(System.in);
//        Scanner sc2 = new Scanner(System.in); // scanner can be combined for input, e.g. one for reading int and one for string
//        String op = sc.next();
//        System.out.println("Insert first number:");
//        int first = sc.nextInt();
//        System.out.println("Insert second number:");
//        int second = sc.nextInt();
//        System.out.println("Insert funny sentence with {0} placeholder for result:");
//        String sentence = sc2.nextLine();
//        int res = 0;
//        if ("add".equals(op)) {
//            res = first + second;
//        } else if ("disctract".equals(op)) {
//            res = first - second;
//        } else {
//            throw new UnsupportedOperationException("operation: " + op);
//        }
//        System.out.println(sentence.replace("{0}", String.valueOf(res)));
//        sc.close();
//        sc2.close();
        
    }
    
    public void collectionsBinarySearchAndComparatorTest() {
        Comparator<String> comparator = new Comparator<String>() {
            
            @Override
            public int compare(String o1, String o2) {
                int i = o1.compareTo(o2);
                if (i == 0) return 0;
                if (i > 0) return -i;
                return Math.abs(i);
            }
        };
        
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
//        list.add("e");
        Collections.sort(list, comparator);
        int index = Collections.binarySearch(list, "a");
        System.out.println(index);
        index = Collections.binarySearch(list, "b");
        System.out.println(index);
        index = Collections.binarySearch(list, "c");
        System.out.println(index);
        index = Collections.binarySearch(list, "d");
        System.out.println(index);
        index = Collections.binarySearch(list, "e");
        System.out.println(index);
    }

    class Inner {
    };
    
    // return type is immediately before the method name, thus <E> is before the return type
    private static <E> void fillList(E elem, List<E> list) {
        list.add(elem);
    }

    private void genericsTest() {
        String stringElem = "string element";
        List<String> list = new ArrayList<String>();
        
        CatchmanNotes.<String>fillList(stringElem, list); //these three ways of invoking generics method are equal
        this.<String>fillList(stringElem, list);
        fillList(stringElem, list);
        
        for (String elem : list) {
            System.out.println(elem);
        }
        
        MyClass<Integer> mci = new MyClass<Integer>("");
        
        Set<? extends Number> someSet = new HashSet<Integer>(); // this is set of numbers, but currently it is not known, what type of numbers exactly
        // the <Integer> can be replaced by Diamond <>, but I don't really see the sense of it
        // it can be directly Number, but it can be Integer, Double, .... too, but only ONE of them, not all
//        someSet.add(new Integer(10)); // compile error, it is not known whether someSet is Set of Integers
        // this is only good for reading and processing values regardless of exact type of number, because I only use method defined in Number interface
        
        Set<Integer> integerSet = new HashSet<>();
        integerSet.add(new Integer(20));
        Set<Double> doubleSet = new HashSet<>();
        doubleSet.add(new Double(30));
        
        Set<Number> numberSet = new HashSet<>(); // this is set of numbers, all subclass of number instance can be added
        numberSet.add(new Integer(10));
        numberSet.add(new Double(100));
        
        callSomeNumberMethod(integerSet);
        callSomeNumberMethod(doubleSet);
        callSomeNumberMethod(numberSet);
        
        // the Set<Number> is not superclass for Set<Integer> because
        // you can say Integer is a Number but you cannot say that Set for integers is Set for all numbers
        
    }
    
    private void callSomeNumberMethod(Set<? extends Number> set) {
    	for (Number number : set) {
			System.out.println(number.byteValue());
		}
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {

        CatchmanNotes cn = new CatchmanNotes();

        System.out.println(cn.doNothingButReturn(new MyCallable(), "tInstance").getClass());

        CatchmanNotes.outputDelimiter("DATE FORMAT");
        cn.simpleDateFormatTest();
        CatchmanNotes.outputDelimiter("COPY ON WRITE LIST");
        cn.copyOnWriteSomethingTest();
        CatchmanNotes.outputDelimiter("LOADING DRIVER");
        cn.loadingDriverTest();
        CatchmanNotes.outputDelimiter("RESOURCE BUNDLE");
        cn.resourceBundleTest();
        CatchmanNotes.outputDelimiter("FORK JOIN");
        cn.forkjoinTest();
        CatchmanNotes.outputDelimiter("RUNNABLE VS. CALLABLE");
        cn.testRunnableAndCallable();
        CatchmanNotes.outputDelimiter("CASTING IN JAVA");
        cn.testCastException();
        
//        CatchmanNotes.outputDelimiter("NIO TESTER - PATH AND FILES AND FILESYSTEMS");
        //moved directly to NioTester class
        
        CatchmanNotes.outputDelimiter("SHADOWING OF VARIABLES");
        cn.testShadowing();

        CatchmanNotes.Inner inner = cn.new Inner();

        CatchmanNotes.outputDelimiter("NUMBER FORMAT API");
        cn.testNumberFormatAPI();
        CatchmanNotes.outputDelimiter("PERMISSION TEST");
        cn.permissionTest();
        CatchmanNotes.outputDelimiter("REGEX TEST");
        cn.regexTest();
        CatchmanNotes.outputDelimiter("COLLECTION, SORT, BINARY SEARCH");
        cn.collectionsBinarySearchAndComparatorTest();
        CatchmanNotes.outputDelimiter("SCANNER API");
        cn.scannerTest();
        CatchmanNotes.outputDelimiter("GENERICS");
        cn.genericsTest();

        // WatcherHandler wh;
        // try {
        // wh = new WatcherHandler();
        // wh.register(Paths.get("/home/trepel/OracleJavaCert/watchTesting"));
        // } catch (IOException e) {
        // e.printStackTrace();
        // return;
        // }
        // new Thread(wh).start();

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
        // throw new Exception("Checked exception in runnable");

    }

}

class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {

        System.out.println("Call in callable, return string: XXX");
        return "XXX";
    }

}

interface I {
    void method();
}

class AI {
    public void method() {
    }
} // compile error without public, cos every method in interface is public abstract automatically
// AI class can have implements I clausule but doesn't have to, no compile error occurs

class BI extends AI implements I {
}

interface AC {
} // the same excepiton occurs if the AC is class and BC and CC extend it

class BC implements AC {
}

class CC implements AC {
}

interface soundable {
    void makeSound();
}

enum Animals implements soundable { // enum can implement interface, can have private or without-modifier constructor ONLY, is
                                    // comparable

    DOG("haf"), CAT("mnau");

    String sound;

    Animals(String sound) {
        this.sound = sound;
    }

    public void makeSound() {
        System.out.println(sound);
    }

}

class AS {
    String name = "AS";

    void printName() {
        System.out.println(name);
    }

    void printName(String s) {
        System.out.println(name);
    }

    static String staticName = "staticAS";
}

class BS extends AS {
    String name = "BS";

    void printName() {
        System.out.println(name);
    }

    void printName(Object o) {
        System.out.println(name);
    }

    static String staticName = "staticBS";
}

class MyClass<X> {
	  <T> MyClass(T t) {
	    System.out.println(t);
	  }
	}
