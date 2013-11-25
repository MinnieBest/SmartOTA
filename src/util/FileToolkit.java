package util;
/**
* 
* @author Yue.W
* 
*/
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import dataBase.StockageData;

//Class "ToolFile" give programor the methods to operate the config files in SmartOTA, like 'configSIM.ini', 'commandsHelloWorld.txt'
public class FileToolkit {
	
	static final String Installation_Path = "C:\\Workspace\\SmartOTA\\"; 
	
	static public void addCommand(String name, String tar, String code, String comment) throws IOException{

		String newComment = "----------"+comment+"----------";
		String newCommand = name.replace(" ", "")+"="+tar+"="+code;
		
		FileReader inPut=null;
		try {
			inPut = new FileReader(Installation_Path+"config\\commandList.ini");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inPut);
		String in;
		String text = "";
		while((in = reader.readLine()) != null){
				text +=in+"\r\n";
		
		}
		reader.close();
		inPut.close();
		
		text +=newComment+"\r\n";
		text +=newCommand+"\r\n";
		
		FileWriter writer = new FileWriter(Installation_Path+"config\\commandList.ini");
        writer.write(text);
        writer.close();
	}

	static public String[] getAllCNTR(){

		String[] res = null;
		String tmpRes = "";
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configSIM.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				
				tmp = line.split("/");
				if(tmp[0] != null){
					tmpRes += tmp[0]+"@";
				}
			}
			res = tmpRes.split("@");
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	static public String getFirstCNTR(){
		String res="";
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configSIM.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				tmp = line.split("/");
				if(tmp[0] != null){
					res = tmp[3];
					break;
				}
			}
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	static public String[] getFirstSIM(){
		String[] res = null;
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configSIM.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				
				tmp = line.split("/");
				if(tmp[0] != null){
					res = tmp;
					break;
				}
			}
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	static public String getValeurSIMFromNumber(String num, int type){
		String res ="";
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configSIM.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				tmp = line.split("/");
				if(tmp[0].equalsIgnoreCase(num)){
					res = tmp[type];
					break;
				}
			}
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	static public void setCNTR(String number, String counter) throws IOException{
		FileReader inPut=null;
		try {
			inPut = new FileReader(Installation_Path+"config\\configSIM.ini");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inPut);
		String in;
		String num, text = "";
		while((in = reader.readLine()) != null){
			if(in.contains("---")||in.trim().equals("")){
				text +=in+"\r\n";
				continue;
			}
			String in2[] = in.split("/");
			num = in2[0].trim();
			if(num.equalsIgnoreCase(number)){
				in = in2[0]+"/"+in2[1]+"/"+in2[2]+"/"+counter+"/"+in2[4];//modify the valeur of phoneNb
				text +=in+"\r\n";
			}else {
				text +=in+"\r\n";
			}
		}
		reader.close();
		inPut.close();
		
		FileWriter writer = new FileWriter(Installation_Path+"config\\configSIM.ini");
        writer.write(text);
        text = null;
        writer.close();
	}

	static public String[] getAllPE(){
		String[] res = null;
		String tmpRes = "";
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configPE.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("---")||line.trim().equals("")){
					continue;
				}
				tmp = line.split("/");
				if(tmp[0] != null){
					tmpRes += tmp[0]+"@";
				}
			}
			res = tmpRes.split("@");
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	static public boolean isNumberExist(String numberE){
		boolean res = false;
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configSIM.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				
				tmp = line.split("/");
				if(tmp[0].equalsIgnoreCase(numberE)){
					res = true;
					break;
				}
				res = false;
			}
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	static public void addNewSIM(String number, String ICCID, String keyOTA,String counter, String pe) throws IOException{
		String newSIM = number+"/"+ICCID+"/"+keyOTA+"/"+counter+"/"+pe;
		
		FileReader inPut=null;
		try {
			inPut = new FileReader(Installation_Path+"config\\configSIM.ini");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inPut);
		String in;
		String text = "";
		while((in = reader.readLine()) != null){
			if(in.contains("---")||in.trim().equals("")){
				text +=in+"\r\n";
				continue;
			}
			text +=in+"\r\n";
		}
		text += newSIM + "\r\n";
		newSIM = null;
		reader.close();
		inPut.close();
		
		FileWriter writer = new FileWriter(Installation_Path+"config\\configSIM.ini");
        writer.write(text);
        text = null;
        writer.close();
	}
	
	static public boolean isCommandExist(String cmdName) throws IOException{
		boolean res = false;
		
		FileReader input = new FileReader(Installation_Path+"config\\commandList.ini");
		BufferedReader br = new BufferedReader(input);
		
		String in, name=null;
		
		while((in = br.readLine()) != null){
			if(in.contains("---")||in.trim().equals(""))
				continue;
			String in2[] = in.split("=");
			name = in2[0].trim();
			
			if(name.equalsIgnoreCase(cmdName)){
				res = true;
				break;
			}
		}
		br.close();
		input.close();
		return res;
	}
	
	static public String getPushMsg() throws IOException{
		FileReader inPut=null;
		try {
			inPut = new FileReader(Installation_Path+"config\\pushMsg.ini");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inPut);
		String in;
		String name, msg = null;
		
		while((in = reader.readLine()) != null){
			
			if(in.contains("--")||in.trim().equals(""))
				continue;
			String in2[] = in.split("=");
			name = in2[0].trim();
			if(name.equalsIgnoreCase("pushmsg"))
				msg = in2[1].trim();
			else {
				System.out.println("Error in "+"\\config\\pushMsg.ini");
			}
		}
		
		//StockageData.DATA = msg+StockageData.ICCID;
		reader.close();
		inPut.close();
		return msg+StockageData.ICCID;
	}
	
	static public String getValueCmdFromName(String nameCmd, int type) throws IOException{

		String res="";
		//Find the command named 'nameCmd' in the file 'C:\Workspace\SmartOTA\config\commandList.ini'
		FileReader input = new FileReader(Installation_Path+"config\\commandList.ini");
		BufferedReader br = new BufferedReader(input);
		String in, name=null;
		
		while((in = br.readLine()) != null){
			if(in.contains("---")||in.trim().equals(""))
				continue;
			String in2[] = in.split("=");
			name = in2[0].trim();
			
			if(name.equalsIgnoreCase(nameCmd)){
				res = in2[type].trim();
				break;
			}
		}
		br.close();
		input.close();
		return res;
	}

	/*type=0, value = pe;type=1, value=PUD_Buffer;type=2, value=SDU_Buffer;
	 *type=3,value=KIC;type=4,value=KID
	 */
	static public String getValueFromPE(String PE, int type){
		String res ="";
		InputStream inS=null;
		try {
			inS = new FileInputStream(Installation_Path+"config\\configPE.ini");
			InputStreamReader inSR = new InputStreamReader(inS);
			BufferedReader bufR = new BufferedReader(inSR);
			String line = null, tmp[] = null;
			while ((line = bufR.readLine()) != null) {
				if(line.contains("--")||line.trim().equals("")){
					continue;
				}
				tmp = line.split("/");
				if(tmp[0].equalsIgnoreCase(PE)){
					res = tmp[type];
					break;
				}
			}
			inS.close();
			bufR = null;
			inSR = null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
}
