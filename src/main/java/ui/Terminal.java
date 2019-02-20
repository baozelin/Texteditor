package ui;
import java.io.*;  

public class Terminal {  
     //get the info of executing instruction
    public static String executeCmd(String strCmd)throws Exception{
		//run the instruction in cmd/terminal
    	Process p = Runtime.getRuntime().exec(strCmd);
    	StringBuilder sbCmd = new StringBuilder();
    	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
    	String line;
    	while ((line = br.readLine()) != null) { 
    		sbCmd.append(line + "\n");
    	}
    	return sbCmd.toString();
    } 
}