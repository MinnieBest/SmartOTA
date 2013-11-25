package mainInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;

/*! \brief Main frame of the graphical interface.
 * 
 * 	This class contains all the graphical elements of the application.
 * */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;/* !< Panel that contains all other elements. */
	private JPanel centerPanel;/*
								 * !< Panel that contains elements at the center
								 * of the window.
								 */

	private JPanel northPanel;/*
							 * !< Panel that contains element at the top of the
							 * window.
							 */
	//private JScrollPane leftPanelN;
	/*
									 * !< Scrollable panel that contains the
									 * left text area.
									 */
	private JTextPaneG leftText;
	/* !< The left text area. */
	private JLabel titleLeft;
	/* !< Title of the left text area. */

	private JScrollPane rightPanelN;/*
									 * !< Scrollable panel that contains the
									 * right text area.
									 */
	private JTextPaneG rightText; /* !< The right text area. */
	private JLabel titleRight;/* !< Title of the right text area. */

	//private JScrollPane southPanel;
	/*
									 * !< Scrollable panel that contains the
									 * bottom text area
									 */
	//private JTextPaneG bottomText;/* !< The bottom text area */
	//private JLabel titleBottom;/* !< Title of the bottom text area. */

	private JPanelG leftPanel;/* !< ?? */

	private JMenuBar menuBar;/* !< Menu bar. */
	private JMenu file;/* !< Menu 'File' of the menu bar. */
	private JMenuItem quitterMenu;/* !< Item 'Exit' of the menu 'File'. */
	//private JMenuItem configTel;//Item 'config Tel.'

	private JMenu configTest;/* !< Menu 'Cmd.' of the menu bar. */
	private JMenu configSIM;/*Menu 'Config.' of the menu bar*/
	private JMenuItem configCNTR; /*Item 'configCNTR' of the menu 'Config.'*/
	private JMenuItem declareSIM;/* 'Declare a new SIM' of the menu'Config.'*/
	//private JMenu configTele;/*submenu 'configTel' of the menu 'Config.'*/
	//private JMenuItem userTel;/*Item 'Telephone for user' of the submenu 'Telephone'*/
	//private JMenuItem testTel;/*Item 'Telephone for test' of the submenu 'Telephone'*/

	private JButton pushMsg;/* !< Button 'pushMsg'. */
	private JButton startTest;/* !< Button 'Start test'. */
	private JButton skipTest;/* !< Not used. */
	private JButton loadReport;/* !< Button Load report. */
	private JButton logTest;//!!!!!!!
	
	private JMenuItem NewCommand;/*
								 * <!add new command in the file 'HelloWorld.txt'
								 * 
								 */
	private JRadioButton Install;
	private ButtonGroup group = new ButtonGroup();
	// private JCheckBox realSIM;
	/*
	 * <!Activate or deactivate real realSIM. true if activated
	 */
	int frameW = 1024/2;/* !< Width of the window. */
	int frameH = 740/2;/* !< Height of the window. */
	int scrollPanelW = 200;/* !<Width of the scrollable panels. */

	int scrollBarW = 20;/* !< Width of the scroll bar. */
	int labelLeftY = 0;/* !< ??. */

	int labelW = 100;/* !< ??. */
	int labelH = 20;/* !< ??. */
	
	private ServerOTA server=null; 
	private MainFrame page;

	/*
	 * ! \brief Constructor of the class. \param title : Title of the window
	 * \param rmt : class RemoteClient associated
	 * 
	 * Create and configure the grahical element of the application.
	 */
	public MainFrame(String title, final ServerOTA server) throws IOException {
		super(title);
		this.server = server;
		this.page = this;

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		mainPanel = new JPanel();
		menuBar = new JMenuBar();
		file = new JMenu("Menu");
		configTest = new JMenu("Cmd.");
		configSIM = new JMenu("Config.");
		configCNTR = new JMenuItem("SIM Counter");
		declareSIM = new JMenuItem("Declare a new SIM");
		//configTele = new JMenu("Telephone");
		
		//userTel = new JMenuItem("Telephone for user");
		//testTel = new JMenuItem("Telephone for test");
		
		quitterMenu = new JMenuItem("Exit");
		//configTel = new JMenuItem("Config Tel.");

		northPanel = new JPanel();
		centerPanel = new JPanel();
		leftPanel = new JPanelG();

		//bottomText = new JTextPaneG();
		leftText = new JTextPaneG();
		rightText = new JTextPaneG();

		titleLeft = new JLabel("	Server CAT-TP");
		titleRight = new JLabel("	Server SMS");
		//titleBottom = new JLabel("	Server CAT-TP");

		pushMsg = new JButton("Push Msg");
		startTest = new JButton("Start");
		logTest = new JButton("See log");
		NewCommand = new JMenuItem("New command");
		//Install = new JRadioButton("Install");
		group.add(NewCommand);
		group.add(Install);
		
		//southPanel = new JScrollPane(bottomText);

		//leftText.setSize(leftPanelN.WIDTH, leftPanelN.HEIGHT + 40);
		//leftPanelN = new JScrollPane(leftText);
		rightPanelN = new JScrollPane(rightText);

		//leftPanelN
		//		.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//leftPanelN
			//	.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		rightPanelN
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//southPanel
				//.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//southPanel.setBorder(BorderFactory.createBevelBorder(1, Color.GRAY,
				//Color.GRAY));
		//leftPanelN.setBorder(BorderFactory.createBevelBorder(1, Color.GRAY,
				//Color.GRAY));
		rightPanelN.setBorder(BorderFactory.createBevelBorder(1,
				Color.LIGHT_GRAY, Color.LIGHT_GRAY));

		leftText.setLayout(null);
		rightText.setLayout(null);
		//bottomText.setLayout(null);

		leftText.add(titleLeft);
		rightText.add(titleRight);
		//bottomText.add(titleBottom);

		titleLeft.setBounds(10, 0, 150, 18);
		titleRight.setBounds(10, 0, 200, 18);
		//titleBottom.setBounds(10, 0, 300, 18);

		Font font = new Font("Time New Roman", Font.BOLD, 13);
		Border border = BorderFactory.createBevelBorder(1, Color.GRAY,
				Color.WHITE);
		titleLeft.setFont(font);
		titleLeft.setForeground(Color.GRAY);
		titleLeft.setBorder(border);

		titleRight.setFont(font);
		titleRight.setForeground(Color.GRAY);
		titleRight.setBorder(border);

		//titleBottom.setFont(font);
		//titleBottom.setForeground(Color.GRAY);
		//titleBottom.setBorder(border);

		mainPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridLayout(1, 1, 5, 5));
		northPanel.setLayout(new GridLayout(1, 2, 5, 5));
		leftPanel.setLayout(new GridLayout(15, 1, 5, 5));

		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(menuBar, BorderLayout.NORTH);

		menuBar.add(file);
		menuBar.add(configTest);
		menuBar.add(configSIM);
		
		//file.add(configTel);
		file.add(quitterMenu);

		quitterMenu.addActionListener(new QuitterListener());
		//configTel.addActionListener(new ConfigTelListener());

		centerPanel.add(northPanel);
		//centerPanel.add(southPanel);

		//northPanel.add(leftPanelN);
		northPanel.add(rightPanelN);

		leftPanel.add(startTest);
		leftPanel.add(pushMsg);
		leftPanel.add(logTest);
		//leftPanel.add(loadReport);

		//loadReport.addActionListener(new LoadReportListener(rmt));
		pushMsg.addActionListener(new PushMsgListener(page));
		startTest.addActionListener(new StartServerOTAListener(server));
		logTest.addActionListener(new LoadLogListener(server));
			
		
		NewCommand.addActionListener(new AddNewCommandBoxListener());
		
		configTest.add(NewCommand);
		//configTest.add(Install);
		
		configCNTR.addActionListener(new ConfigCNTRListener());
		declareSIM.addActionListener(new ConfigDeclareSIMListener());
		
		//userTel.addActionListener(new ConfigUserTelListener());
		//testTel.addActionListener(new ConfigTestTelListener());
		//configTele.add(userTel);
		//configTele.add(testTel);
		
		configSIM.add(configCNTR);
		configSIM.add(declareSIM);
		//configSIM.add(configTele);

		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
		this.setSize(frameW, frameH);
		this.setMinimumSize(getSize());
		configure();
	}

	/*
	 * ! \brief Configures some element the Interface.
	 * 
	 * Makes the text areas non editable and start the thread 'blink'.
	 */
	public void configure() {
		leftText.setEditable(false);
		rightText.setEditable(false);
		Font font = new Font("Time New Roman", Font.BOLD, 12);
		leftText.setFont(font);
	}

	/* ! \brief Refreshes the text areas. */
	public void refresh() {
		leftText.repaint();
		rightText.repaint();
		this.scrollToEnd(leftText);
		this.scrollToEnd(rightText);
	}

	/* ! \brief Clears the text areas. */
	public void clearScreen() {
		leftText.reset();
		leftText.setText("");
		rightText.setText("");
		refresh();
	}

	/*
	 * ! \brief Add a String at the end of the left. Starts with a new line.
	 * \param texte String to add.
	 */
	public void addTextLeftLN(String texte) {
		leftText.setText(leftText.getText() + "\n   " + texte);
		this.scrollToEnd(leftText);
	}

	/*
	 * ! \brief Add a String at the end of the left text area. \param texte
	 * String to add.
	 */
	public void addTextLeft(String texte) {

		leftText.setText(leftText.getText() + texte);
		this.scrollToEnd(leftText);

	}

	/*
	 * ! \brief Add a String at the end of the right text area. Starts with a
	 * new line. \param texte String to add.
	 */
	public void addTextRightLN(String texte) {
		rightText.setText(rightText.getText() + "\n" + texte);
		this.scrollToEnd(rightText);

		Rectangle rect = new Rectangle(rightText.getHeight(), 1, 1, 1);

	}

	/*
	 * ! \brief Add a String at the end of the right text area. \param texte
	 * String to add.
	 */
	public void addTextRight(String texte) {
		rightText.setText(rightText.getText() + texte);
		this.scrollToEnd(rightText);
	}

	/*
	 * ! \brief This method scrolls a JTextPaneG to the end. \param JText The
	 * JTextPaneG to scroll.
	 */
	private void scrollToEnd(JTextPaneG textP) {
		textP.setSelectionStart(textP.getText().length());
	}

	public boolean getModemMode() {
		return true;//Delete.isSelected();
	}

	/*
	 * ! \brief Add a status indicator at the begining of the specified line.
	 * \param pos Display the indicator at this position in the left text area.
	 * \param state Integer that represents the color of the indicator.
	 */

	public void setTestState(int pos, int state) {
		switch (state) {
		case 0:
			leftText.setColorTest(Color.RED, pos);
			break;
		case 1:
			leftText.setColorTest(Color.GREEN, pos);
			break;
		case 2:
			leftText.setColorTest(Color.ORANGE, pos);
			break;
		case 3:
			leftText.setColorTest(Color.GRAY, pos);
			break;
		case 99:
			leftText.setColorTest(Color.WHITE, pos);
			break;
		}
		leftText.repaint();
	}

	/* ! \brief Get the higher position of statuts'indicator. */
	public int getHigherPosition() {
		return leftText.getHigherPos();
	}

	/*
	 * ! \brief Displays a text in a message box (Information message). \param
	 * msg Message to display. \param title Title of the message box.
	 */

	public void msgBox_OK(String msg, String title) {
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.INFORMATION_MESSAGE);

	}

	/*
	 * ! \brief Displays a text in a message box (Error message). \param msg
	 * Message to display. \param title Title of the message box.
	 */
	public void msgBox_Error(String msg, String title) {
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.ERROR_MESSAGE);

	}

	/*
	 * ! \brief Displays a text in a message box with two choices for the user.
	 * \param msg Messsage to display. \param title Title of the message box.
	 * 
	 * Returns xx if the user clicks 'YES' and xx if the user clicks 'NO'.
	 */
	public int msgBox_YES_NO(String msg, String title) {
		return JOptionPane.showConfirmDialog(this, msg, title,
				JOptionPane.OK_CANCEL_OPTION);
	}

	/*
	 * ! \brief Shows an input box. \param msg Text to display before the text
	 * input area.
	 */
	public String getResponseG(String msg) {
		String response = JOptionPane.showInputDialog(this, msg,
				"Information request", JOptionPane.PLAIN_MESSAGE);
		return response;
	}

	/*
	 * ! \brief Sets the SS3 indicator color. \param c Color to set.
	 */
	public void setSS3Color(Color c) {
		leftPanel.setSS3Color(c);
		leftPanel.repaint();

	};

	/* ! \brief Returns the left panel. */
	public JTextPaneG getLeftTextPanel() {
		return leftText;
	}

	/* ! \brief Returns the right panel. */
	public JTextPaneG getRightTextPanel() {
		return rightText;

	}

	/* ! \brief Activate the button 'New Test'. */
	public void activatePushMsg() {
		pushMsg.setEnabled(true);
	}

	/* ! \brief Not used. */
	public void activateSkipTest() {
		skipTest.setEnabled(true);
	}

	/* ! \brief Activate the button 'Load report'. */
	public void activateLoadReport() {
		loadReport.setEnabled(true);
	}

	/* ! \brief Activate the button 'Start test'. */
	public void activateStartTest() {
		startTest.setEnabled(true);
	}

	public void activateFileMenu() {
		file.setEnabled(true);
	}

	public void activateConfigMenu() {
		configTest.setEnabled(true);
	}
	
	public void activateConfigSIM(){
		configSIM.setEnabled(true);
	}

	/* ! \brief Deactivate the buton 'Start test'. */
	public void deactivateStartTest() {
		startTest.setEnabled(false);
	}
	/* ! \brief Deactivate the button 'New test'. */
	public void deactivatePushMsg() {
		pushMsg.setEnabled(false);
	}
	
	/* ! \brief Deactivate the button 'Load report'. */
	public void deactivateLoadReport() {
		loadReport.setEnabled(false);
	}

	/* ! \brief Deactivate the check box 'Modem'. */
	public void deactivateModem() {
		//Delete.setEnabled(false);
		Install.setEnabled(false);
	}


	public void deactivateFileMenu() {
		file.setEnabled(false);
	}

	public void deactivateConfigMenu() {
		configTest.setEnabled(false);
	}
	public void deactivateConfigSIM(){
		configSIM.setEnabled(false);
	}

	public void clearRightText(){
		this.rightText.reset();
		leftText.repaint();
		this.rightText.setText("");
	}

	public void clearAllText() {
		this.rightText.setText("");
		this.scrollToEnd(rightText);
		this.leftText.setText("");
		this.scrollToEnd(leftText);
		leftText.reset();
		leftText.repaint();
	}
}
