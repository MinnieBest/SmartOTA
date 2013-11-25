package mainInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JTextPane;

/*! \brief A class to manage the display of information.
 * 
 *  The text areas are instances of this class. The possibility to
 *  display status indicators has been added.
 * */
public class JTextPaneG extends JTextPane {
	private static final long serialVersionUID = 1L;

	private int step = 16; /*
							 * !< Space between each colored indicator. Matches
							 * the policy type and size.
							 */
	// private int pos =1;
	private LinkedList<Color> colorTest = new LinkedList<Color>();/*
																 * !< List of
																 * status
																 * indicators.
																 */
	private LinkedList<Integer> colorTestPos = new LinkedList<Integer>();/*
																		 * !<
																		 * Y-axis
																		 * of
																		 * status
																		 * indicators
																		 * .
																		 */

	public JTextPaneG() {
		this.colorTest = new LinkedList<Color>();
		this.colorTestPos = new LinkedList<Integer>();
	}

	/*
	 * ! \brief Refresh the JtextPane.
	 * 
	 * This method draws the text and status indicators.
	 */

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < colorTest.size(); i++) {
			g.setColor(colorTest.get(i));
			g.fillOval(1, colorTestPos.get(i), 9, 9);
		}
	}

	// public void paintAllWhite(){
	// for(int i=0;i<=colorTest.size();i++){
	// colorTest.set(i, Color.WHITE);
	// }
	// }

	/*
	 * !\brief Useless??. \return
	 */
	public int getHigherPos() {
		int max = 0;
		for (int i = 0; i < colorTestPos.size(); i++) {
			if (colorTestPos.get(i) > max)
				max = colorTestPos.get(i);
		}
		return max;

	}

	/*
	 * ! \brief Add a status indicator. \param c Color of te stauts indicator.
	 * \param pos Position of the status indicator.
	 */
	public void setColorTest(Color c, int pos) {
		if (colorTestPos.contains(23 + pos * step)) {
			int i = colorTestPos.indexOf(23 + pos * step);

			Color old = colorTest.get(i);
			if (old.equals(Color.red) || old.equals(Color.RED))
				return;
			else if ((old.equals(Color.orange) || old.equals(Color.ORANGE))
					&& (c.equals(Color.green) || c.equals(Color.GREEN)))
				return;

			colorTest.remove(i);
			colorTestPos.remove(i);

			// return;
		}
		colorTest.add(c);
		colorTestPos.add(23 + pos * step);
		// pos++;
	}

	/*
	 * ! \brief Erase from memory the positions and color of status indicators
	 * saved .
	 */
	public void reset() {
		colorTest =null;
		colorTestPos = null;
		colorTest = new LinkedList<Color>();
		colorTestPos = new LinkedList<Integer>();

	}
}
