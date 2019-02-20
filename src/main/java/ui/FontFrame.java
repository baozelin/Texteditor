package ui;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.util.logging.Logger;

public class FontFrame extends JFrame {

	private JPanel contentPane;
	private JComboBox familyChooseBox;
	private JComboBox styleChooseBox;
	private JComboBox sizeChooseBox;

	private static final Logger log = Logger.getLogger("Log");

	/**
	 * Create the frame.
	 */
	public FontFrame(JTextPane currentPane) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 296, 312);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton yesButton = new JButton("Yes");
		JButton cancelButton = new JButton("Cancel");
		JLabel sizeLable = new JLabel("Size");
		JLabel styleLable = new JLabel("Style");
		JLabel familylabel = new JLabel("Family");

		sizeChooseBox = new JComboBox();
		styleChooseBox = new JComboBox();
		familyChooseBox = new JComboBox();

		yesButton.setBounds(34, 241, 75, 29);
		cancelButton.setBounds(185, 241, 75, 29);
		sizeLable.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		sizeLable.setBounds(34, 66, 58, 23);

		contentPane.add(yesButton);
		contentPane.add(cancelButton);
		contentPane.add(sizeLable);

		int current_size = currentPane.getFont().getSize();

		sizeChooseBox.setModel(new DefaultComboBoxModel(new String[] { "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21" ,
				"23", "24","25", "26","27", "28", "29", "30"}));

		sizeChooseBox.setSelectedIndex(current_size - 5);
		sizeChooseBox.setBounds(104, 67, 111, 27);
		contentPane.add(sizeChooseBox);

		styleLable.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		styleLable.setBounds(33, 121, 58, 23);
		contentPane.add(styleLable);

		styleChooseBox.setModel(new DefaultComboBoxModel(new String[] {"Plain", "Bold", "Italic"}));
		styleChooseBox.setBounds(104, 122, 111, 27);
		contentPane.add(styleChooseBox);

		familylabel.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		familylabel.setBounds(34, 176, 58, 23);
		contentPane.add(familylabel);
		familyChooseBox.setModel(new DefaultComboBoxModel(new String[] {"Times New Roman", "Microsoft Yahei", "SimHei", "SimSun", "Lucida Grande"}));
		familyChooseBox.setBounds(104, 177, 111, 27);
		contentPane.add(familyChooseBox);

		yesButton.addActionListener(e -> {
			try {
				String family = familyChooseBox.getSelectedItem().toString();
				int size = Integer.parseInt(sizeChooseBox.getSelectedItem().toString());
				String style = styleChooseBox.getSelectedItem().toString();
				switch (style) {
					case "Plain":
						Font f1 = new Font(family, Font.BOLD, size);
						currentPane.setFont(f1);
						break;
					case "Bold":
						Font f2 = new Font(family, Font.BOLD, size);
						currentPane.setFont(f2);
						break;
					case "Italic":
						Font f3 = new Font(family, Font.BOLD, size);
						currentPane.setFont(f3);
						break;
				}
				FontFrame.this.dispose();
			} catch (NullPointerException npe) {
				log.info("No textpane");
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FontFrame.this.dispose();
			}
		});

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				FontFrame.this.dispose();
			}
		});
	}
}
