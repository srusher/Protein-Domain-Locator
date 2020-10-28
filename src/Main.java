/*
Class Name: BIFS 618
Homework assignment 4, question 2_2 and 2_3
File name: Main.java (contains main function for zincFinger.java)
Program author name: Samuel Rusher
*/

public class Main {


    public static void main(String[] args){

        zincFinger seqs = new zincFinger(); //creating seqs object from zincFinger class

        seqs.readFile(); //calling readFile() function from zincFinger class
        seqs.modifyFile(); //calling the modifyFile() method
        seqs.findSeqID(); //calling readFile() function from zincFinger class
        seqs.findSite(); //calling readFile() function from zincFinger class
        seqs.printResults(); //calling readFile() function from zincFinger class


    }



}
