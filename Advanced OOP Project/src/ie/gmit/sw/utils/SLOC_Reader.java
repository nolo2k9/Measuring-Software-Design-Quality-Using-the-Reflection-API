package ie.gmit.sw.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ie.gmit.sw.Database;
/**
 * <h1>Concrete class SLOC_Reader</h1> 
 * This class has <strong> one responsibilty</strong>. To go through the contents of a file and print out the Source Lines Of Code.
 * This method is utilsed in the database class to get the source lines of code of a passed in JAR file.
 * @author Keith Nolan.
 *
 * @version 1.0.
 * 
 * @see Database
 * 
 * @since JDK15.
 *
 */

public class SLOC_Reader {
	/**
	 * <p>
	 * Takes in a String.
	 * Reads through each of the lines in the file
	 * Ignores irrelevant aspects of the file
	 * Counts the Source lines of code.
	 * </p>
	 * 
	 * @throws IOException
	 * 
	 * @param The paramters used in this method are String f.
	 *            
	 * @return The value of <code>i</code> SLOCS.
	 * 
	 *
	 */

	public static void countLines (String f) throws IOException {
		//file reader
		FileReader file = new FileReader(f);
		//buffered reader
		BufferedReader br = new BufferedReader(file);
		//counter
		int i = 0;
		//end of file control variable
		boolean isEOF = false;
		do {
			// variable to readlines
			String s = br.readLine();
			if (s != null) {
				isEOF = true;
				//replace these with blank space
				s = s.replaceAll("\\n|\\t|\\s", "");
				// s is not a blank space or begins with //
				if ((!s.equals("")) && (!s.startsWith("//"))) {
					//increment i by 1
					i = i + 1;
				}
			} else {
				
				isEOF = false;
			}
		} while (isEOF);
		//print out number of counted lines 
		System.out.println("- " + i + " Source Lines of code" + "\n");
		//close buffered reader
		br.close();
		//close file reader
		file.close();

	}

}
