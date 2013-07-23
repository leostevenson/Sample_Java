package dialPanel;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class DataProcessTest {
	private static final String REGEX = "[0-9#\\*\\x20]*";
	@Test
	public void testDataProcess() {
		DataProcess dp;
		try{
			FileInputStream fstreamIn = new FileInputStream("data\\messaging.in");
			
			DataInputStream in = new DataInputStream(fstreamIn);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			FileWriter fstreamOut = new FileWriter("data\\messaging.out");
			BufferedWriter out = new BufferedWriter(fstreamOut);
			
			while ((strLine = br.readLine()) != null)   {
				if (strLine.matches(REGEX)){
					dp  = new DataProcess();
										
					char[] charArray = strLine.toCharArray();
					for (char c : charArray){
						dp.Input(c);
					}
					out.write(dp.content);
					out.newLine();
					//System.out.println("Output: " + dp.content);
				}
				else{
					System.out.println("There might be some invalid characters in this line!");
				}
			}
			
			in.close();
			out.close();
		}catch (Exception e){
			System.err.println("Error: " + e.getMessage());
		}
	}
}
