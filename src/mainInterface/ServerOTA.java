package mainInterface;
/**
*
* @author Yue.W
*/
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import serverCatTp.MainServer;
import serverSMS.MainServerSMS;

public class ServerOTA {
	private MainFrame page;
	public String keyOTA, CNTR;
	public List<String> commandListA = new LinkedList<String>(), commandListR = new LinkedList<String>();
	
	public ServerOTA() throws IOException{
		page = new MainFrame("SmartOTA v1.1", this);
		page.setVisible(true);
		page.deactivatePushMsg();
	}
	
	public void startServer(){
		Thread t = new Thread(){
			public void run(){
				startOTA();
			}
		};
		t.start();
	}
	//*****Lancer the server Cap-TP and the server SMS
	public void startOTA(){
		MainServer server = new MainServer(page);
		server.startServer();
		
		page.addTextRightLN("Server SmartOTA is open!");
		MainServerSMS serverSMS = new MainServerSMS(page);
		serverSMS.startServer();
		
		page.deactivateStartTest();
		page.activatePushMsg();
	}

	public static void main(String[] args){
		try {
			ServerOTA serverOTA = new ServerOTA();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadLog(String filename){
		File file = new File(filename);
		if(Desktop.isDesktopSupported()){
			try {
				//open the file log by "Notepad"
				Desktop.getDesktop().edit(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		file = null;
	}
}