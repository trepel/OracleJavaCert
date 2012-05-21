package dunda;

import java.util.Random;
import java.util.Scanner;

public class Dunda {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // System.out.println("Selec operation (add/disctract):");
//        Scanner sc = new Scanner(System.in);
        // Scanner sc2 = new Scanner(System.in); // scanner can be combined for input, e.g. one for reading int and one for
        // string
        // String op = sc.next();
        // System.out.println("Insert first number:");
//        int first = sc.nextInt();
        // System.out.println("Insert second number:");
        // int second = sc.nextInt();
        // System.out.println("Insert funny sentence with {0} placeholder for result:");
        // String sentence = sc2.nextLine();
        // int res = 0;
        // if ("add".equals(op)) {
        // res = first + second;
        // } else if ("distract".equals(op)) {
        // res = first - second;
        // } else {
        // throw new UnsupportedOperationException("operation: " + op);
        // }
        // System.out.println(sentence.replace("{0}", String.valueOf(res)));
        // sc2.close();
        //

//        while (first != 6) {
//
//            Random r = new Random();
//            System.out.println(r.nextInt(6) + 1);
//
//            first = sc.nextInt();
//
//        }
//
//        sc.close();

        int[] arrayX = { 1, 2, 3, 6 };

        int koef = 6;
        int x = 0;
        int y = 0;

        for (int i = 0; i < arrayX.length; i++) {

            x = arrayX[i];
            y = koef * x;
            System.out.print(y + " ");
        }
        System.out.println();

    }

}
