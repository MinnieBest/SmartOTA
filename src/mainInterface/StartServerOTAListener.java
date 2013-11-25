package mainInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartServerOTAListener implements ActionListener{
	ServerOTA server;
	
	public StartServerOTAListener(ServerOTA server){
		this.server = server;
	}
	
	public void actionPerformed(ActionEvent e){
		server.startServer();
	}
}
