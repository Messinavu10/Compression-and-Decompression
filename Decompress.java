import java.util.Scanner;
import java.io.*;

public class Decompress {

	public static int doubleCount = 0;
	public static String[] Double(String[] oldEntries) {
    	String newEntries[] = new String[2*oldEntries.length];
    	for(int i = 0; i < oldEntries.length; i++) {
        	newEntries[i] = oldEntries[i];
    	}
    	doubleCount++;
    	return newEntries;
	}

	public static void main(String[] args)
	{

        boolean run = false;
        boolean end = false;
    	String last;


    	Scanner sc = new Scanner(System.in);
    	InputStream inputFile;
    	PrintWriter outputFile;
    	PrintWriter Filelog;

    	String currEntries[] = new String[163];


    	int nextInd = 0;
    	currEntries[0] = Character.toString((char)10); 
    	currEntries[1] = Character.toString((char)9); 
    	currEntries[2] = Character.toString((char)11);
    	nextInd += 4;

    	
    	for (int i = 0; i <= 94; i++)
    	{
        	currEntries[i+3] = Character.toString((char)i+32);
        	nextInd++;
    	}

    
    	do {
        	try {

            	
            	String nextFile;
            	if(run) {
                	System.out.println("Enter the filename for Decompression: ");
                	nextFile = sc.nextLine();
            	} else {
                	if(args[0] != null) {
                    	nextFile = args[0];
                	} else {
                    	nextFile = "";
                	}
            	}

            	
            	inputFile = new FileInputStream(nextFile);
            	outputFile = new PrintWriter(new FileOutputStream(nextFile.substring(0, nextFile.length()-4)));
            	Filelog = new PrintWriter(new FileOutputStream(nextFile.substring(0, nextFile.length()-4) + ".log"));

            	String currByte; 
            	int nextByte = inputFile.read(); 

            	
            	outputFile.print(currEntries[nextByte-29].toString());
            	currByte = currEntries[nextByte-29];

            	
            	doubleCount = 0;
            	long startTime = System.nanoTime();

            	while((nextByte = inputFile.read()) != -1) {

                	
                	if(nextInd >= currEntries.length) {
                    	currEntries = Double(currEntries);
                	}

                	
                	if(nextByte-29 == -19) {
                    	outputFile.print("\n");
                    	currEntries[nextInd] = currByte + "\n";
                    	currByte = "\n";
                    	nextInd++;
                    	continue;
                	}
                	else if(nextByte-29 == -20) {
                    	outputFile.print("\t");
                    	currEntries[nextInd] = currByte + "\t";
                    	currByte = "\t";
                    	nextInd++;
                    	continue;
                	}
                	else if(nextByte-29 == -18) {
                    	outputFile.print("\r");
                    	currEntries[nextInd] = currByte + "\r";
                    	currByte = "\r";
                    	nextInd++;
                    	continue;
                	}

                	
                	if(nextByte-28 <= 97 && nextByte > 27) {
                    	outputFile.print(currEntries[nextByte-29].toString());
                    	currEntries[nextInd] = currByte + Character.toString((char)nextByte);
                    	currByte = Character.toString((char)nextByte);

                
                	} else {
                    	outputFile.print(currEntries[nextByte-28].toString());
                    	currEntries[nextInd] = currByte + currEntries[nextByte-28].substring(0, currEntries[nextByte-28].length()-1);
                    	currByte = currEntries[nextByte-28];
                	}
                	nextInd++;
            	}
            	outputFile.close();

            	
            	long TotalTime = System.nanoTime() - startTime;
            	double Millisec = (TotalTime/1000000);
       	 
            	
            	Filelog.write("Decompression for file " + nextFile + "\n");
            	Filelog.write("Decompression took " + Millisec + " milliseconds to run.\n");
            	Filelog.write("The table was doubled " + doubleCount + " times.\n");
            	Filelog.close();

        	
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

        	
        	System.out.println("Would you like to enter another file? (y or n)");
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

