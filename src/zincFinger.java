/*
Class Name: BIFS 618
Homework assignment 4, question 2_2 and 2_3
File name: zincFinger.java (goes with Main.java)
Program author name: Samuel Rusher
*/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class zincFinger {

    public String file1 = "";
    public ArrayList<String> seqID = new ArrayList<String>(); //creating arraylist to store seqIDs
    public ArrayList<Integer> seqIDStartIndex = new ArrayList<Integer>(); //creating arraylist to store start index at each seq ID
    public ArrayList<Integer> seqIDEndIndex = new ArrayList<Integer>(); //creating arraylist to store end index at each seqID
    public ArrayList<Integer> zincSitesStart = new ArrayList<Integer>(); //creating arraylist to store the start index of each zinc site
    public ArrayList<Integer> zincSitesEnd = new ArrayList<Integer>(); //creating arraylist to store the end index of each zinc site

    public void readFile() { //this method is a standard file reading method that stores each line of the file into the file1 field

        try {
            File myfile = new File("zincFinger.txt"); // creating myfile object
            Scanner myReader = new Scanner(myfile); //creating scanner object
            while (myReader.hasNextLine()) { //while loop will execute as long as there is another line in the file
                file1 += myReader.nextLine(); //adding the current line as a string to file1
            }
            myReader.close();
        } catch (FileNotFoundException e) { //catching any file not found exceptions
            System.out.println("\nThe file entered does not exist. Please try again.\n");
            e.printStackTrace();
        }
    }

    public void modifyFile(){ //this method replaces all of the line-breaks in file1 with whitespace
        file1 = file1.replace("\n","");
    }

    public void findSeqID() { //this method adds each seq ID to the seqID arraylist
        int start = 0;
        int end = 0;

        for (int x = 0; x < file1.length(); x++) {
            if (file1.charAt(x) == '\n'){
                x--;
            }
            if (file1.charAt(x) == '>') { //the '>' signifies the beginning of a seqID
                start = x;
            } else if (file1.charAt(x) == ']') { //the ']' signifies the end of a seqID
                end = x + 1; //adding 1 to end to get the last nucleotide included in the range
                seqID.add(file1.substring(start, end)); //adding the substring to the seqID arraylist
                seqIDStartIndex.add(start); //storing the start Index
                seqIDEndIndex.add(end); //storing the end index
            }
        }
    }

    public void findSite() { //this method will find the start and end index of each consensus sequence

        Pattern pattern = Pattern.compile("[C][^C]{2}[C][^C]{17}[C][^C]{2}[C]", Pattern.CASE_INSENSITIVE); //creating regex pattern for the consensus seq
        Matcher matcher = pattern.matcher(file1); //creating a matcher object that will search file1

        while (matcher.find()) { //this loop will store the zinc start and end indices to their own separate arrays
            zincSitesStart.add(matcher.start()); //adding the start index of each match to arraylist
            zincSitesEnd.add(matcher.end()); //adding the end index of each match to arraylist

        }
    }

    //this method took me a lot of trial and error so I apologize if some of the code is sloppy or doesn't seem to flow very well - However, it works

    public void printResults() { //this method will print each seqID that corresponds to each zinc site match, along with additional information

        int count = 0; //count variable that will be used in for loop
        for (int x = 0; x < seqIDEndIndex.size(); x++) { //for loop will execute a number of times equal to the length of the seqIDEndIndex arraylist

            if (seqIDEndIndex.get(x) < zincSitesStart.get(count) && zincSitesStart.get(count) < seqIDStartIndex.get(x + 1)) { //checking to see if the zinc start site at position 'count' is between the SeqID end index at position x and the next seqID start index at x + 1
                    System.out.println("\n\n"+seqID.get(x) + "\nContains the zinc finger site: " + file1.substring(zincSitesStart.get(count), zincSitesEnd.get(count)) + "\nAt locations:"); //printing the seqID at position x and the exact regex pattern of its corresponding zinc site (start index of count and end index of count)
                    System.out.println((zincSitesStart.get(count) - seqIDEndIndex.get(x)) + " " + (zincSitesEnd.get(count)- seqIDEndIndex.get(x))); //printing the range in where the consensus sequence was found in the given seqID

                int line = 0; //for loop line variable so I can alternate lines that I'm printing output to on the console
                int count2 = 0; //count variable that will be used in for loop
                String aa_seq = (file1.substring(seqIDEndIndex.get(x), seqIDStartIndex.get(x + 1))); //storing the amino acid sub string into the aa_seq variable
                int each_49 = 0; //using each_49 to make sure each count3 (in loop below) is always set to an increment of 49
                int match = 0; //variable used to store match position of an amino acid printed with the corresponding amino acid in the regex sequence
                int n = 0; //used as a condition to limit the execution of the first 'else-if' statement

                for (int i = 0; i < aa_seq.length();i++){ //this nested for loop will iterate the length of the aa_seq

                    if (i == aa_seq.length()-1){ //checking to see if this is the last amino acid in aa_seq
                        System.out.print(aa_seq.charAt(count2)); //printing out the last aa
                        System.out.println(); //creating a potential "*" line below
                        line++; //incrementing line to alternate between printing amino acid line and potential "*" line
                        each_49 += 49; //this assigns count2 to the value of the length of a full line (if the amino acid sequence ends prior to the end of that line) -- count3 is always assigned to count2 and must
                    }

                    else if (i >= 49 && i%49 ==0 && n > 0){ //this statement is evaluated every 50 iterations and only BEFORE the last 'else' statement is executed --> n > 0
                        System.out.println(); //creating a new line to print "*" below each amino acid line
                        line++; //incrementing line to alternate between printing amino acid line and potential "*" line
                        each_49 += 49; //adding 49 to each_49 -- only used for the last line of amino
                    }

                    if (line%2 == 0) { //checking to see if line is equal to an even number
                        System.out.print(aa_seq.charAt(count2)); //printing each amino acid, one after the other, on the same line
                        count2++; //incrementing count2 only when an amino acid is printed, keeps the program from skipping amino acids after printing the "*" line
                        n++; //incrementing n, so that one of the conditions is met in the above else-if statement
                    }

                    else { //this block will create a blank line below the previously printed amino acid line and print a "*" underneath each amino acid of the given consensus sequence

                        int count3 = each_49; //setting count3 equal to each_49


                        for (int y =0; y < 49;y++){ //this nested for loop will iterate 49 times

                            if (count3-49 == ((zincSitesEnd.get(count)-1) - seqIDEndIndex.get(x))){ //checking to see if count3 matches the zinc site end position (relative to this aa string, not the entire file1 string) -- 49 must be subtracted from count3 to get the position of the first aa on the previous row
                                System.out.print("*"); //printing a "*" because this implies it is a match for the final position
                                match = 0; //resetting match to 0 for the next consensus seq
                                break; //breaking out of the for loop
                            }
                            else if (count3-49 == (zincSitesStart.get(count) - seqIDEndIndex.get(x))+match){ //checking to see if count3 matches the zinc site start position (relative to this aa string, not the entire file1 string) -- 49 must be subtracted from count3 to get the position of the first aa on the previous row
                                    System.out.print("*"); //adding a "*" in the blank line below the aa that matched
                                    match++; //incrementing match, which will be added to the zinc start site each iteration to get the next aa in the consensus seq
                            }
                            else{
                                System.out.print(" "); //printing a blank space if there is not a match
                            }
                            count3++; //incrementing count3

                        }
                        System.out.println(); //moving the console to the next line down
                        line++; //incrementing line so we can start printing aa again
                        n = 0; //setting n = 0 so the first else-if statement (line 94) will not execute
                        if (i == aa_seq.length()-1){ //if this is the last aa of the sequence, 'i' will not be decremented so that the for loop can terminate
                            continue;
                        }
                        else{
                            i--; //decrementing 'i' because the entire else block (starting at line 106) is not printing any aa, if 'i' was incremented here like normal we would prematurely end the aa seq
                        }
                    }
                }
                if (count == zincSitesEnd.size() - 1) { //this if statement will ensure no index out of bounds exception is thrown by breaking out of the for loop that starts at line 72
                    break;
                }
                count++; //incrementing count (first evaluated at line 74)
            }
        }
    }

}