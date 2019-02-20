package ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Main class for terminal interface
 *
 * @author Major:Zelin Bao, yiru Hu
 * @version 1.2
 */
public class TerminalUI extends JPanel {
	private JTextPane terminalPane;
	int lastIndex;
	String executedContent;
	JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public TerminalUI() {
		setLayout(null);
		lastIndex = 0;

		terminalPane = new JTextPane();
        setPreferredSize(new Dimension(600, 100));
		terminalPane.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				Document docs = terminalPane.getDocument();
				SimpleAttributeSet attrset = new SimpleAttributeSet();
				StyleConstants.setFontSize(attrset,10);
				StyleConstants.setForeground(attrset,Color.BLACK);

				if(e.getKeyChar()==KeyEvent.VK_ENTER ){

					try {

						String cmd = terminalPane.getText().substring(lastIndex, terminalPane.getText().length());
						executedContent = executeCmd(cmd);
						docs.insertString(docs.getLength(), "\n" + executedContent, attrset);
						lastIndex = terminalPane.getText().length();

					} catch (Exception e1) {
                        try {

                            docs.insertString(docs.getLength(), "\n" + e1.getMessage(), attrset);
                            lastIndex = terminalPane.getText().length();
                            docs.insertString(docs.getLength(), "\n", attrset);

                        } catch (BadLocationException e2) {
                            e2.printStackTrace();
                        }
                        e1.printStackTrace();
                    }
				}
			}
		});

		setLayout(new BorderLayout());
		scrollPane = new JScrollPane(terminalPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
	}

	public static String executeCmd(String strCmd)throws Exception{

		if(strCmd.length() > 1) {
			Process p = Runtime.getRuntime().exec(strCmd);
			StringBuilder sbCmd = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
			String line;
			while ((line = br.readLine()) != null) {
				sbCmd.append(line + "\n");
			}
			return sbCmd.toString();
		}
		return "";
	}

}
