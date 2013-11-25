package mainInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class AddNewCommandBoxListener implements ActionListener{

	public AddNewCommandBoxListener(){
	}
	
	public void actionPerformed(ActionEvent e){
		AddNewCommandBoxFrame configFrame = new AddNewCommandBoxFrame(new JFrame());
		configFrame.setVisible(true);
	}
	
}
