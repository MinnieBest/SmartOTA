package serverSMS;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import util.Sendsms;
import util.FileToolkit;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dataBase.StockageData;

public class ServerSMSThread implements Runnable{
	private int port;
	private static MainServerSMS serverSMS;
	
	public ServerSMSThread(int port, MainServerSMS server) {//page(int port, MainServerSMS server, MainFrame frame)
		this.port = port;
		this.serverSMS = server;
	}
	
	public void run() {
		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress("Localhost",port), 0);
			server.createContext("/test", new MyHandler());/*the 2-way command in NowSMS is
															*http://127.0.0.1:8900/test?PhoneNumber=@@SENDER@@&message=@@FULLSMS@@&isBinary=@@BINARY@@
															*/
			server.setExecutor(null); // creates a default executor
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Server SMS is opened!");
	}
	
	static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
        	String textInSMS = null,telephoneSender = null,isBinary = "0";
        	boolean isCmdInFile = false;//true -> the command sent by telephone can be found in the file "HelloWorld.txt"
        	boolean isNumConfigured = true;
        	String msgNowSMS = t.getRequestURI().toString();/*
        													 *"/test?PhoneNumber=@@SENDER@@&message=@@FULLSMS@@&isBinary=@@BINARY@@"
        	 												 */
        	System.out.println("the msg is"+t.getRequestURI());
        	
        	//replace the space; '%20' means space
        	textInSMS = findValueOfKey("message",msgNowSMS).replace("%20", "").replaceAll("%0A", "");//keywords:PhoneNumber, message,isBinary
        	System.out.println("textInSMS = "+textInSMS);
        	//the phonenumber from the NowSMS is %2B33xxxxxxxxx
        	telephoneSender = findValueOfKey("PhoneNumber",msgNowSMS).replace("%2B", "+");
        	isBinary = findValueOfKey("isBinary",msgNowSMS);
        	
        	//find if the textInSMS is a command or not saved in the file "HelloWorld.txt"
        	//isCmdInFile = cmdIsInFile(textInSMS);
        	isCmdInFile = FileToolkit.isCommandExist(textInSMS);
        	//find if the information of the SIM card has already saved in the server
        	//isNumConfigured = numIsConfigured(telephoneSender);
        	isNumConfigured = FileToolkit.isNumberExist(telephoneSender);
        	
        	if(isBinary.equalsIgnoreCase("0")){
        		if(isCmdInFile && isNumConfigured){
        			//****load data of card SIM from the files
        			StockageData.phoneNb = telephoneSender;
        			loadParaSIM();
        			loadParaPE();
        			loadPushMsg();
        		
        			//Load this command in the file 'HelloWorld.txt'
        			loadCommand(textInSMS);
        			sendPush();
        			ResponseBySMS("Message Push to \""+textInSMS+"\" sent!",t);
        			serverSMS.showRightLN("SMS \""+textInSMS+"\" received from "+telephoneSender+" at "+getTime());
        			serverSMS.showRightLN("Message Push \""+textInSMS+"\" sent to "+telephoneSender+" at "+getTime());
        			StockageData.pushSent = true;//??timer, for starting the Thread 'timer' in the server Cat-Tp
        		}else if(textInSMS.equalsIgnoreCase("Hello") && isNumConfigured){
        			serverSMS.showRightLN("SMS \""+textInSMS+"\" received from "+telephoneSender+" at "+getTime());
        			ResponseBySMS("Welcome to SmartOTA!"+
						" It is supported by the ITS/SIM-Bouygues Telecom.",t);
        			
        			serverSMS.showRightLN("Response to SMS \""+textInSMS+"\" sent to "+telephoneSender+" at "+getTime());
        			serverSMS.showRightLN("");
        			System.gc();
        		}else if(!isCmdInFile &&isNumConfigured){
        			serverSMS.showRightLN("SMS \""+textInSMS+"\" received from "+telephoneSender);
        			ResponseBySMS("Error : Your message can not be understood.",t);
        			serverSMS.showRightLN("");
        		} else if(!isNumConfigured){
        			serverSMS.showRightLN("SMS \""+textInSMS+"\" received from "+telephoneSender);
        			serverSMS.showRightLN("This SIM card \""+telephoneSender+"\" is not configured in the server Cat-Tp");
        			serverSMS.showRightLN("");
        			ResponseBySMS("Error : This SIM card is not configured in the server. ",t);
        		}
        	} else if(isBinary.equalsIgnoreCase("1")){
        		//String res = description(textInSMS);
        		//ResponseBySMS("The response to the push message :"+textInSMS+" ("+res+")",t);
        		ResponseBySMS("The response to the push message :"+textInSMS,t);
        		serverSMS.showRightLN("SMS :"+textInSMS+"\" received from "+telephoneSender+" at "+getTime());
        		serverSMS.showRightLN("");
        	}
        }
    }
	
	private static void  loadParaSIM() throws IOException{
		String number = StockageData.phoneNb;
		
		StockageData.keyOTA = FileToolkit.getValeurSIMFromNumber(number,2);;
		StockageData.CNTR = FileToolkit.getValeurSIMFromNumber(number,3);;
		StockageData.Profil = FileToolkit.getValeurSIMFromNumber(number,4);
		StockageData.ICCID = FileToolkit.getValeurSIMFromNumber(number,1);
	}
	
	public static void loadParaPE() throws IOException{
		
		String pe = StockageData.Profil;
		StockageData.PDU = FileToolkit.getValueFromPE(pe, 1);
		System.out.println("PDU = "+StockageData.PDU );
		StockageData.SDU = FileToolkit.getValueFromPE(pe, 2);
		System.out.println("SDU = "+StockageData.SDU );
		StockageData.KIc = FileToolkit.getValueFromPE(pe, 3);
		System.out.println("KIc = "+StockageData.KIc );
		StockageData.KID = FileToolkit.getValueFromPE(pe, 4);
		System.out.println("KID = "+StockageData.KID );

		/*
		FileReader inPut=null;
		try {
			inPut = new FileReader("C:\\Workspace\\SmartOTA\\config\\PE\\"+pe+".ini");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inPut);
		String in;
		String name;
		String pdu = null, sdu = null, kic = null, kid = null, tar = null;
		
		while((in = reader.readLine()) != null){
			if(in.contains("--")||in.trim().equals(""))
				continue;
			String in2[] = in.split("=");
			name = in2[0].trim();
			if(name.equalsIgnoreCase("PDU_Buffer")){
				pdu = in2[1].trim();
				}
			else if(name.equalsIgnoreCase("SDU_Buffer")){
				sdu = in2[1].trim();
			}
			else if(name.equalsIgnoreCase("KIC")){
				kic = in2[1].trim();
			}
			else if(name.equalsIgnoreCase("KID")){
				kid = in2[1].trim();
			}
			else 
				System.out.println("Error in "+name);
		}
		
		StockageData.PDU = pdu;
		StockageData.SDU = sdu;
		StockageData.KIc = kic;
		StockageData.KID = kid;
		
		reader.close();
		inPut.close();
		*/
	}
	
	public static void loadPushMsg() throws IOException{
		StockageData.DATA = FileToolkit.getPushMsg();
		/*
		FileReader inPut=null;
		try {
			inPut = new FileReader("C:\\Workspace\\SmartOTA\\config\\pushMsg.ini");
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
		
		StockageData.DATA = msg+StockageData.ICCID;
		reader.close();
		inPut.close();
		*/
	}
	
	private static void ResponseBySMS(String textOfSMS, HttpExchange he) throws IOException{
		String response =textOfSMS;
		HttpExchange t = he;
		Headers responseHeaders = t.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println("Msg is handled!");
	}
	
	//find the text part in the received SMS
	private static String findValueOfKey(String keyword,String text){
		String res = "";
		String in[] = text.split("&");
		int i = 0;
		while(in[i] != null){
			String in2[] = in[i].split("=");
			if(in2[0].contains(keyword)){
				res = in2[1];
				break;
			}
			i++;
		}
		return res;
	}

	private static String loadCommand(String cmdType) throws IOException{
		String resultat = null;
		changeCommandInDatabase(cmdType);
		resultat = cmdType;
		
		return resultat;
	}
	
	//Change the value of commandList and tar in the Database 
	private static void changeCommandInDatabase(String nameCmd) throws IOException{
		String command = null, tar = null;
		String commandL[] =null;
		
		command = FileToolkit.getValueCmdFromName(nameCmd, 2);
		tar = FileToolkit.getValueCmdFromName(nameCmd, 1);
		
		//Cut the commands APDU by APDU
		commandL = command.split("%");
		StockageData.commandList.clear();
		//StockageData.commandList.add(0, command);
		System.out.println("commandL.length = "+commandL.length);
		
		//For cutting the commands into SDU
		
		 //move the commandL to commandList for cutting commands
		int pA = 0, pL =0;
		while(pA < commandL.length){
			String total = commandL[pA];
			int addNum = 0;
			do{
				if((pA + addNum) == (commandL.length - 1)){
					StockageData.commandList.add(pL, total);
					System.out.println("****total ="+total);
					pA = pA + addNum + 1;
					total = null;//memory leak
					break;
				}
				total = total + commandL[(pA + 1 +addNum)];
				addNum++;
				if(total.length() >= (Integer.parseInt(StockageData.SDU)*2)){
					//System.out.println("****total1 ="+total);
					StockageData.commandList.add(pL, (total.replace(commandL[pA+addNum], "")));
					System.out.println("****total2 ="+total);
					pL++;
					pA = pA + addNum;
					total = null;//memory leak
					break;
				}
			}while(true);
		}
		
		/*
		for(int i=0;i < commandL.length;i++){
			StockageData.commandList.add(i, commandL[i]);
			commandL[i] = null;
		}
		*/
		//StockageData.commandCutted = commandL.length;//For cutting command into SDU;
		commandL = null;
		StockageData.TAR = tar;
		System.out.println("tar = "+tar);
	}
	
	public static void sendPush(){
		Sendsms sendsms = new Sendsms();
	    sendsms.init();
	    sendsms.server = "http://127.0.0.1:8800/";
	    sendsms.phonenumber = StockageData.phoneNb;
	    sendsms.udh = "027000";
	    sendsms.pid = "7F";
	    sendsms.dcs = "F6";
	    
	    String CPL;
	    System.out.println("CNTR in server SMS = "+StockageData.CNTR);
	    CPL = StockageData.calculCPL(StockageData.CHL + StockageData.SPI + StockageData.KIc + StockageData.KID + StockageData.TAR + StockageData.CNTR + StockageData.PCNTR + StockageData.crypChecksum + StockageData.DATA);
        String input = StockageData.padding(CPL + StockageData.CHL + StockageData.SPI + StockageData.KIc + StockageData.KID + StockageData.TAR + StockageData.CNTR + StockageData.PCNTR + StockageData.DATA);
        StockageData.crypChecksum = StockageData.calculCheckSum(input);
        sendsms.data = CPL + StockageData.CHL + StockageData.SPI + StockageData.KIc + StockageData.KID + StockageData.TAR + StockageData.CNTR + StockageData.PCNTR + StockageData.crypChecksum + StockageData.DATA;
        
        sendsms.send();
        
        System.out.println("Push Msg :"+sendsms.data);
        //increase the otaCounter in the file 'configSIM.ini'
        
        String tmp = StockageData.increaseCounterInStockageData();
		try {
			StockageData.changeCNTRInConfig(tmp.toUpperCase());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StockageData.CNTR = tmp.toUpperCase();
	}
	
	public static void sendSMS(String text){
		Sendsms send = new Sendsms();
		send.init();
		send.server = "http://127.0.0.1:8800/";
		send.phonenumber = StockageData.phoneNb;
		send.text = text;
	    send.send();
	    send = null;
	}
	
	private static String getTime(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String tmp =sdf.format(cal.getTime());
		sdf = null;
		cal = null;
		return tmp;
	}
	
	private static String description(String response){
		String codeR = null, res = null;
		codeR = response;
		
		if(codeR.endsWith("026F0002")){
			res = "The BIP session can not be opened and no packet is received in the server";
		} else if (codeR.endsWith("029000")){
			res = "The BIP session is open";
		} else if (codeR.endsWith("0260013")){
			res = "The mobile can not support the BIP service";
		} else if (codeR.endsWith("016F0021")){
			res = "ME currently unable to execute the command and the session BIP is closed	";
		} else {
			res = "Unknown error";
		}
		return res;
	}
}
