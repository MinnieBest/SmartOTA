package mainInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class ConfigDeclareSIMListener implements ActionListener{
	public ConfigDeclareSIMListener(){
	}
	
	public void actionPerformed(ActionEvent e){
		ConfigDeclareSIMFrame configFrame = new ConfigDeclareSIMFrame(new JFrame());
		configFrame.setVisible(true);
	}
}
