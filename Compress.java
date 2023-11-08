import java.util.Scanner;
import java.io.*;

public class Compress {

	public static void main(String[] args)
	{
    	
    	
        boolean run = false;
        boolean end = false;
    	String last;
    	
    	Scanner sc = new Scanner(System.in);
    	OutputStream fileoutput;
    	BufferedReader filereader;
    	PrintWriter FileLog;

    	int nextCode = 32;
    	HashTableChain<String, Integer>SavingNumbers = new HashTableChain<String, Integer>();
    	for (int i = 32; i <= 126; i++)
    	{
        	SavingNumbers.put(Character.toString((char)i), i);
        	nextCode++;
    	}
 	
    	SavingNumbers.put("\t", 9); 
    	SavingNumbers.put("\n", 10); 
    	SavingNumbers.put("\r", 11); 

    	do {
        	try {

            	String nextFile;
            	if(run) {
                	System.out.println("Enter file name for Compression: ");
                	nextFile = sc.nextLine();
            	} else {
                	if(args[0] != null) {
                    	nextFile = args[0];
                	} else {
                    	nextFile = "";
                	}
            	}

            	
            	filereader = new BufferedReader(new FileReader(nextFile));
            	fileoutput = new FileOutputStream(nextFile + ".zzz");
            	FileLog = new PrintWriter(new FileOutputStream(nextFile + ".zzz.log"));

            	
            	int nextCh = filereader.read();
            	String currString = String.valueOf((char)nextCh);

            	
            	long startTime = System.nanoTime();
           	 
            	while(nextCh != -1) {
               	 
                	
                	if(SavingNumbers.get(currString) != null) {
                    	nextCh = filereader.read();
                    	currString += String.valueOf((char)nextCh);
                	} else {
                    	
                    	if(nextCh < 31 || SavingNumbers.get(currString.substring(0, currString.length()-1)) == null) {
                        	currString = "\n";
                        	continue;
                    	} else {
                        	fileoutput.write(SavingNumbers.get(currString.substring(0, currString.length()-1)).byteValue());
                    	}                   	
                    	currString = String.valueOf((char)nextCh);
                    	nextCode++;
                	}
            	}
            	
            	fileoutput.write(currString.substring(0, currString.length()-1).getBytes());
            	fileoutput.close();

            	
            	long TotalTime = System.nanoTime() - startTime;
            	double MilliSec = (TotalTime/1000000);

            
            	File InputFile = new File(nextFile);
            	long InputFileSize = InputFile.length();
            	File OutputFile = new File(nextFile + ".zzz");
            	long OutputFileSize = OutputFile.length();

            	
            	FileLog.write("Compression of " + nextFile + "\n");
            	FileLog.write("Compressed from " + InputFileSize + " Kilobytes to " + OutputFileSize + " Kilobytes.\n");
            	FileLog.write("Compression took " + MilliSec + " milliseconds to run.\n");
            	FileLog.write("The dictionary contains " + (nextCode-35) + " total entries.\n");
            	FileLog.write("The table was rehashed " + SavingNumbers.hashCount() + " times.\n");
            	FileLog.close();

        
        	} catch (FileNotFoundException e) {
            	System.out.println("ERROR! - No such file.");
            	run = true;
            	continue;
        	} catch (ArrayIndexOutOfBoundsException e) {
            	System.out.println("ERROR! - No such file.");
            	run = true;
            	continue;
        	} catch(IOException e) {
            	System.out.println("ERROR! - File Reading Issue.");
            	e.printStackTrace();
        	}

        	System.out.println("Would you like to compress another file? (y or n)");
        	last = sc.nextLine();
        	if(last.equalsIgnoreCase("y")) {
            	run = true;
        	} else {
            	end = true;
        	}
    	} while(!end);
    	sc.close();
	}
}

