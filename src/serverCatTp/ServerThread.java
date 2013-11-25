package serverCatTp;

/**
*
* @author Yue.W
*/

import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import dataBase.StockageData;
import mainInterface.MainFrame;
import serverSMS.ServerSMSThread;
import state.QueueCAT_TP_PackageMap;
import state.State;
import util.Util;

public class ServerThread implements Runnable {
	private static int  port; 
	private InetAddress IPAddress;
	private DatagramSocket serverSocket;
	private String dataInLog;
	private MainFrame page;
	private List<String> commands = new LinkedList<String>();//The commands will send to mobile by the server
	private boolean cmdLoaded = false;
	private volatile boolean testFinished = false;
	private boolean waitSYN = true;//a flag to prevent the server receiving others packers except for packet 'SYN'
	private int numTest = 0;//i means how many BIP session smartOTA has done
	
	public ServerThread(int port,MainFrame frame) {
		this.port = port;
		this.page = frame;
	}

	@Override
	public void run() {
		
		// Start the server
		byte[] receiveData = new byte[1024];
		
		System.out.println("\nServer starting...");
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		try {
			serverSocket = new DatagramSocket(port);
			while (true) {
				serverSocket.receive(receivePacket);
				/*
				 * if server get a 'Reset', it means the BIP session is finished, 
				 * server will reload the commands after it receives the new package
				 */
				if(!cmdLoaded ||waitSYN){
					commands =StockageData.commandList;
					cmdLoaded = true;
					
					/* 
					 * Update the CNTR in the class 'State'
					 */
					System.out.println("CNTR in server CAPTP = "+StockageData.CNTR);
					StockageData.changeStateOTACounter(StockageData.CNTR);
					
					page.deactivateConfigMenu();
					page.deactivateConfigSIM();
					page.deactivatePushMsg();
					dataInLog = "";
				}
				
				processData(serverSocket, receivePacket);
				
				if(testFinished){
					numTest++;
					//Send a SMS to say "Session is finished!"
					ServerSMSThread.sendSMS("The BIP session is finished; You can begin a new test");
					
				    commands.clear();
				    commands = null;
				    cmdLoaded = false;
				    waitSYN = true;
				    
				    //update the cntr in the file "configSIM.ini"
				    StockageData.changeCNTRInConfig(State.otaCounter);
				    
				    //??timer : stop the 'timer' thread
				    StockageData.pushSent = false;
				    
				    String nameLog = "Session_"+numTest+"_"+getTime();
				    FileWriter writer = new FileWriter("C:\\Workspace\\SmartOTA\\log\\"+nameLog+".txt");
			        writer.write(dataInLog);
			        writer.close();
			        dataInLog = "";
				    
			        if(numTest%50 == 0){
			        	page.clearRightText();
			        }
			        
				    System.gc();
				    
				    page.addTextRightLN("******BIP Session N°"+numTest+" is finished!******");
				    page.addTextRightLN("");
				    //page.addTextBottomLN("*********************************************************************************");
				    //page.addTextBottomLN("***********************BIP Session N°"+numTest+" is finished!***************************");
				    //page.addTextBottomLN("*********************************************************************************");
				    page.activateConfigMenu();
				    page.activateConfigSIM();
				    page.activatePushMsg();
				    testFinished = false;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void processData(final DatagramSocket dataSocket,
			DatagramPacket receivePacket) {

		IPAddress = receivePacket.getAddress();
		int portInPacket = receivePacket.getPort();
		CAT_TP_Package sendCattpPackage = new CAT_TP_Package();

		// Cut the length of the received Packet
		byte[] packageBytes = receivePacket.getData();//byte[]
		short dataLength = (short) (packageBytes[8] & 0xFF);
		dataLength = (short) (((dataLength << 8) & 0xFF00) + (packageBytes[9] & 0xFF));
		short headerLength = packageBytes[3];
		CAT_TP_Package recCattpPackage = new CAT_TP_Package(//CAT_TP_Package 
				receivePacket.getData(), 0, (short) (headerLength + dataLength));
		String sentence = Util.ByteArrayToHexString(recCattpPackage
				.getPackageBytes());
		//page.addTextBottomLN("");
		System.out.println("\nRECEIVED(at"+getTime()+"): " + sentence);
		dataInLog += "\nRECEIVED(at"+getTime()+"): " + sentence+"\r\n";
		//page.addTextBottomLN("\nRECEIVED: " + sentence);
		System.out.println("recv:" + "Seq:" + recCattpPackage.getSequenceNb()
				+ "; ACK:" + recCattpPackage.getACKNb());
		dataInLog += "recv:" + "Seq:" + recCattpPackage.getSequenceNb()
				+ "; ACK:" + recCattpPackage.getACKNb()+"\r\n";
		//page.addTextBottomLN("recv:" + "Seq:" + recCattpPackage.getSequenceNb()
				//+ "; ACK:" + recCattpPackage.getACKNb());
		sentence = null;
		
		
		if (recCattpPackage.getACKNb() < State.sequenceNb) {
			// The package sent before by server wasn't received by card
			// or the package before is a ACK without data.
			// The data should be resent
			/*
			sendCattpPackage = QueueCAT_TP_PackageMap
					.getSentPackage(recCattpPackage.getACKNb() + 1);
			State.sequenceNb = recCattpPackage.getACKNb() + 1;
			State.ackNb = recCattpPackage.getSequenceNb();
			*/
			if(recCattpPackage.isRST()){
				State.clearState();
				QueueCAT_TP_PackageMap.clearQueue();
				page.addTextRightLN("BIP session is closed by the mobile...");
				waitSYN = true;
				testFinished = true;
			}else {}//page.addTextBottomLN("This package is discarded");
			
		} else {
			// Normal sequence
			if (recCattpPackage.getSequenceNb() >= State.ackNb) {
				// The package not been received
				State.ackNb = recCattpPackage.getSequenceNb();

				if (recCattpPackage.isSYN()) {
					System.out.println("SYN");
					dataInLog += "SYN"+"\r\n";
					//page.addTextBottomLN("SYN");
					page.addTextRightLN("BIP session start...");
					sendCattpPackage.Init();//(prevent to create a new instance)sendCattpPackage = new CAT_TP_Package();
					State.sequenceNb = 1;
					//For cutting the commands into SDU
					State.maxPDUDataLen = Integer.parseInt(StockageData.PDU) - 20;//maxPDULen - PDUheaderLen
					State.maxSDULen = Integer.parseInt(StockageData.SDU);
					//the flag  waitSYN is set to be 'False'
					waitSYN = false;
					// SYN+ACK
					sendCattpPackage.encapsulate((byte) 0xC0,
							(short) State.sequenceNb,
							recCattpPackage.getSequenceNb(),
							recCattpPackage.getDesPort(),
							recCattpPackage.getSourcePort());
				}
				if (recCattpPackage.isACK()) {
					if (recCattpPackage.isRST()) {
						System.out.println("RST");
						dataInLog += "RST"+"\r\n";
						//page.addTextBottomLN("RST");
						// Close the connection
						dataSocket.close();
						return;
					}
					if (recCattpPackage.isSEG()) {
						System.out.println("SEG");
						dataInLog += "SEG"+"\r\n";
						//page.addTextBottomLN("SEG");
						// TODO: Segment received
					}
					if (recCattpPackage.isNUL()) {
						System.out.println("ACK+NUL");
						dataInLog += "ACK+NUL"+"\r\n";
						//page.addTextBottomLN("ACK+NUL");
						sendCattpPackage.Init();//(prevent to create a new instance)sendCattpPackage = new CAT_TP_Package();
						if (recCattpPackage.getWindowSize() > 0) {
							sendCattpPackage = prepareCommand(sendCattpPackage,
									recCattpPackage);
						} else {
							// ACK
							//if waitSYN is 'true', all the ack will be ignored by server
							if(!waitSYN){
								sendCattpPackage.encapsulate((byte) 0x40,
										(short) (State.sequenceNb + 1),
										recCattpPackage.getSequenceNb(),
										recCattpPackage.getDesPort(),
										recCattpPackage.getSourcePort());
							} else {
								page.activateConfigMenu();
							    page.activateConfigSIM();
							    page.activatePushMsg();
								return;}
						}
					} else {
						System.out.println("ACK");
						dataInLog += "ACK"+"\r\n";
						if (State.hasCommand) {
							if (recCattpPackage.getWindowSize() > 0) {
								sendCattpPackage = prepareCommand(
										sendCattpPackage, recCattpPackage);
							} else {
								if(!waitSYN){//?if waitSYN is 'true', all the ack will be ignored by server
									sendCattpPackage.encapsulate((byte) 0x40,
											(short) (State.sequenceNb + 1),
											recCattpPackage.getSequenceNb(),
											recCattpPackage.getDesPort(),
											recCattpPackage.getSourcePort());
								} else {
									page.activateConfigMenu();
								    page.activateConfigSIM();
								    page.activatePushMsg();
									return;}
							}
						} else {
							sendCattpPackage.Init();//(prevent to create a new instance)sendCattpPackage = new CAT_TP_Package();
							State.sequenceNb++;
							State.increaseCounter();//increase the 'CNTR' for starting the next test
							// ACK+RST
							sendCattpPackage.encapsulate((byte) 0x50,
									(short) State.sequenceNb,
									recCattpPackage.getSequenceNb(),
									recCattpPackage.getDesPort(),
									recCattpPackage.getSourcePort());
							State.clearState();
							QueueCAT_TP_PackageMap.clearQueue();
							testFinished = true;//Flag to finish the test
							//the flag waitSYN is set to 'true'
							waitSYN = true;
						}
					}
				}
				if (recCattpPackage.isEACK()) {
					System.out.println(" EACK");
					dataInLog += "EACK"+"\r\n";
					//page.addTextBottomLN(" EACK");
					// TODO
				}
			} else {// The package has been already received
				sendCattpPackage.Init();//(prevent to create a new instance)sendCattpPackage = new CAT_TP_Package();
				sendCattpPackage.encapsulate((byte) 0x40,
						(short) (State.sequenceNb), (short) (State.ackNb),
						recCattpPackage.getDesPort(),
						recCattpPackage.getSourcePort());
			}
			try{
				sendUDPPackage(sendCattpPackage, dataSocket, IPAddress, portInPacket);
			} catch(NullPointerException e){
				
				e.printStackTrace();
			}
		}
		sendCattpPackage = null;
		recCattpPackage = null;
		// Send UDP Package
		//sendUDPPackage(sendCattpPackage, dataSocket, IPAddress, port);
	}

	protected Package0348 constructCommand() {
		Package0348 command = new Package0348();
		command.CHL = StockageData.CHL;
		command.SPI = StockageData.SPI;
		command.KIc = StockageData.KIc;
		command.KID = StockageData.KID;
		command.TAR = StockageData.TAR;
		command.CNTR = State.otaCounter;
		command.PCNTR = StockageData.PCNTR;
		command.DATA = commands.get(State.idxCommandList);
		command.keyOTA = StockageData.keyOTA;
		return command;
	}

	private void cutPackage0348(final Package0348 package0348, int partSize) {
		int partNb = (package0348.getPackage().length / partSize) + 1;
		int lastPartLength = package0348.getPackage().length % partSize;
		State.segPartsNeeded = partNb;
		State.lastPartLength = lastPartLength;
	}

	private CAT_TP_Package prepareCommand(CAT_TP_Package sendCattpPackage,
			CAT_TP_Package recCattpPackage) {
		Package0348 package0348 = null;
		byte data[];
		State.sequenceNb++;
		if (State.isSendingSeq) {
			System.out.println("Sending Segment..." + State.segNb);
			dataInLog += "Sending Segment..." + State.segNb+"\r\n";
			sendCattpPackage.Init();//(prevent to create a new instance)sendCattpPackage = new CAT_TP_Package();
			if (State.segNb == (State.segPartsNeeded - 1)) {
				System.out.println("(Last segment)");
				dataInLog += "(Last segment)"+"\r\n";
				data = State.wholePackage0348.getPartPackage(State.segNb
						* State.maxPDUDataLen, State.lastPartLength);
				sendCattpPackage.encapsulate((byte) 0x40, data,
						(int) State.sequenceNb,
						recCattpPackage.getSequenceNb(),
						recCattpPackage.getDesPort(),
						recCattpPackage.getSourcePort());

				State.isSendingSeq = false;
				State.lastPartLength = 0;
				State.segNb = 0;
				State.segPartsNeeded = 0;
				State.wholePackage0348 = null;

				State.idxCommandList++;
				if (State.idxCommandList == commands.size()) {
					State.hasCommand = false;
				}
			} else {
				data = State.wholePackage0348.getPartPackage(State.segNb
						* State.maxPDUDataLen, State.maxPDUDataLen);
				sendCattpPackage.encapsulate((byte) 0x44, data,
						(short) State.sequenceNb,
						recCattpPackage.getSequenceNb(),
						recCattpPackage.getDesPort(),
						recCattpPackage.getSourcePort());
				State.segNb++;
			}
		} else{
			Package0348 bufPackage = constructCommand();
			if (bufPackage.getPackage().length > State.maxPDUDataLen) {
				System.out.println("Preparing Segment...");
				dataInLog += "Preparing Segment..."+"\r\n";
				State.isSendingSeq = true;
				State.increaseCounter();
				State.wholePackage0348 = constructCommand();
				this.cutPackage0348(State.wholePackage0348, State.maxPDUDataLen);
				System.out.println("Sending Segment..." + State.segNb);
				dataInLog += "Sending Segment..." + State.segNb+"\r\n";
				data = State.wholePackage0348.getPartPackage(State.segNb
						* State.maxPDUDataLen, State.maxPDUDataLen);
				sendCattpPackage.encapsulate((byte) 0x44, data,
						(short) State.sequenceNb,
						recCattpPackage.getSequenceNb(),
						recCattpPackage.getDesPort(),
						recCattpPackage.getSourcePort());
				State.segNb++;
			} else {
				State.increaseCounter();
				package0348 = this.constructCommand();
				sendCattpPackage.Init();
				sendCattpPackage.encapsulate((byte) 0x40, package0348,
						(int) State.sequenceNb,
						recCattpPackage.getSequenceNb(),
						recCattpPackage.getDesPort(),
						recCattpPackage.getSourcePort());
				State.idxCommandList++;
				if (State.idxCommandList == commands.size()) {
					State.hasCommand = false;
				}
			}
			bufPackage = null;
		}
		data = null;
		return sendCattpPackage;
	}

	private void sendUDPPackage(CAT_TP_Package sendCattpPackage,
			DatagramSocket dataSocket, InetAddress IPAddress, int port) {
		DatagramPacket sendPacket = new DatagramPacket(
				sendCattpPackage.getPackageBytes(),
				sendCattpPackage.getHeaderLength()
						+ sendCattpPackage.getDataLength(), IPAddress, port);
		try {
			dataSocket.send(sendPacket);
			QueueCAT_TP_PackageMap.addPackageMap(
					sendCattpPackage.getSequenceNb(), sendCattpPackage);
			System.out.println("\nSENT(at"+getTime()+"): "
					+ Util.ByteArrayToHexString(sendCattpPackage
							.getPackageBytes()));
			dataInLog += "\nSENT(at"+getTime()+"): "
					+ Util.ByteArrayToHexString(sendCattpPackage
							.getPackageBytes())+"\r\n";
			System.out.println("send:" + "Seq:"
					+ sendCattpPackage.getSequenceNb() + "; ACK:"
					+ sendCattpPackage.getACKNb());
			dataInLog += "send:" + "Seq:"
					+ sendCattpPackage.getSequenceNb() + "; ACK:"
					+ sendCattpPackage.getACKNb()+"\r\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendPacket = null;
		
	}
	private String getTime(){
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String tmp =sdf.format(cal.getTime());
		sdf = null;
		cal = null;
		return tmp;
	}
}
