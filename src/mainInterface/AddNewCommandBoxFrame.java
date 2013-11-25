package mainInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import util.FileToolkit;


public class AddNewCommandBoxFrame extends JDialog implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5266223577576745271L;
	/**
	 * 
	 */
	private JTextField nameCmd = null;
	private JTextField tarCmd = null;
	private JTextField codeCmd = null;
	private JTextField commentCmd = null;
	
	private String nameForCmd = null;
	private String tarForCmd = null;
	private String codeForCmd = null;
	private String commentForCmd = "";

	private boolean flagDone = false;

	public AddNewCommandBoxFrame(JFrame frame) {
		super(frame, "Add a new command");

		Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(dimensions.width / 4, dimensions.height / 4+200);

		setLocation(dimensions.width / 2 - dimensions.width / 4,
				dimensions.height / 2 - dimensions.height / 4);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel panelMain = new JPanel(new BorderLayout());
		JPanel panelText = new JPanel();
		Border paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		panelText.setBorder(paneEdge);
		panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));

		JLabel label0 = new JLabel("Name of the command", JLabel.TRAILING);
		panelText.add(label0);
		nameCmd = new JTextField("", 11);
		label0.setLabelFor(nameCmd);
		panelText.add(nameCmd);
		
		JLabel label1 = new JLabel("TAR for the command", JLabel.TRAILING);
		panelText.add(label1);
		tarCmd = new JTextField("", 11);
		label1.setLabelFor(tarCmd);
		panelText.add(tarCmd);
		
		JLabel label2 = new JLabel("Command in hexadecimal", JLabel.TRAILING);
		panelText.add(label2);
		codeCmd = new JTextField("", 11);
		label2.setLabelFor(codeCmd);
		panelText.add(codeCmd);
		
		JLabel label3 = new JLabel("Supplementary information(not obligatory)", JLabel.TRAILING);
		panelText.add(label3);
		commentCmd = new JTextField("", 11);
		label3.setLabelFor(commentCmd);
		panelText.add(commentCmd);

		
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setInfos();
				if (!isInfosEmpty()) {
					try {
						FileToolkit.addCommand(nameCmd.getText(),tarCmd.getText(),codeCmd.getText(),commentCmd.getText());
						//addCmdInfile(nameCmd.getText(),tarCmd.getText(),codeCmd.getText(),commentCmd.getText());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					setVisible(false);
					flagDone = true;
				}
			}
		});
		panelMain.add(panelText, BorderLayout.NORTH);
		panelMain.add(okButton, BorderLayout.SOUTH);
		this.add(panelMain);
	}

	private boolean isInfosEmpty() {
		if (this.nameForCmd.equals("")) {
			String msg = "Please enter the name without space(ex. downloadSignedProd)";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		}else if(this.tarForCmd.equals("")){
			String msg = "Please enter the tar (ex. B00010)";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		}else if(this.codeForCmd.equals("")){
			String msg = "Please enter the command hexadecimal";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		}else if(this.tarForCmd.length() != 6){
			String msg = "Please enter the tar with 6 digits";
			String title = "Warning";
			msg_box(msg, title);
			return true;
		}
		else
			return false;
	}

	private void setInfos() {
		this.nameForCmd = this.nameCmd.getText();
		this.tarForCmd = this.tarCmd.getText();
		this.codeForCmd = this.codeCmd.getText();
		this.commentForCmd = this.commentCmd.getText();
	}

	private void msg_box(String msg, String title) {
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.setVisible(true);
		while (!flagDone) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
