package search;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindDialog extends JDialog {
    private JTextPane textPane;
    private JFrame owner;
    private JTextField findField;
    private JTextField replaceField;
    private JLabel findLabel;
    private JLabel replaceLabel;
    private JButton findNextBtn;
    private JLabel status;
    private JButton replaceBtn;
    private JButton replaceAllBtn;

    public FindDialog(JFrame frame, JTextPane tp, int mode) {
        super(frame, "Find");
        textPane = tp;
        owner = frame;
        initUI();
        render(mode);
    }

    private void render(int mode) {
        if (mode == 0) {
            getContentPane().setBackground(UIManager.getColor("Panel.background"));
            getContentPane().setForeground(Color.black);

            findField.setBackground(Color.white);
            findField.setForeground(Color.black);

            findLabel.setBackground(UIManager.getColor("Panel.background"));
            findLabel.setForeground(Color.black);

            findNextBtn.setOpaque(true);
            findNextBtn.setBackground(Color.white);
            findNextBtn.setForeground(Color.black);

            replaceField.setBackground(Color.white);
            replaceField.setForeground(Color.black);

            replaceLabel.setBackground(UIManager.getColor("Panel.background"));
            replaceLabel.setForeground(Color.black);

            replaceBtn.setOpaque(true);
            replaceBtn.setBackground(Color.white);
            replaceBtn.setForeground(Color.black);

            replaceAllBtn.setOpaque(true);
            replaceAllBtn.setBackground(Color.white);
            replaceAllBtn.setForeground(Color.black);

            status.setBackground(UIManager.getColor("Panel.background"));
            status.setForeground(Color.black);
        } else {
            getContentPane().setBackground(Color.darkGray);
            getContentPane().setForeground(Color.white);

            findField.setBackground(Color.darkGray);
            findField.setForeground(Color.white);

            findLabel.setBackground(Color.darkGray);
            findLabel.setForeground(Color.white);

            findNextBtn.setOpaque(true);
            findNextBtn.setBackground(Color.darkGray);
            findNextBtn.setForeground(Color.white);

            replaceField.setBackground(Color.darkGray);
            replaceField.setForeground(Color.white);

            replaceLabel.setBackground(Color.darkGray);
            replaceLabel.setForeground(Color.white);

            replaceBtn.setOpaque(true);
            replaceBtn.setBackground(Color.darkGray);
            replaceBtn.setForeground(Color.white);

            replaceAllBtn.setOpaque(true);
            replaceAllBtn.setBackground(Color.darkGray);
            replaceAllBtn.setForeground(Color.white);

            status.setBackground(Color.darkGray);
            status.setForeground(Color.white);
        }

    }

    private void initUI() {
        setLocation((int) (owner.getLocationOnScreen().getX() + owner.getWidth()) / 2,
                (int) (owner.getLocationOnScreen().getY() + owner.getHeight()) / 2);
        setSize(owner.getWidth() / 2 + 100, owner.getWidth() / 4);
        setLayout(null);

        findField = new JTextField();
        findLabel = new JLabel("Find: ");
        findLabel.setBounds(10, 10, 100, 20);
        findField.setBounds(findLabel.getX() + 100, findLabel.getY(), 150, findLabel.getHeight());
        add(findLabel);
        add(findField);

        replaceField = new JTextField();
        replaceLabel = new JLabel("Replace to: ");
        replaceLabel.setBounds(10, 40, 100, 20);
        replaceField.setBounds(replaceLabel.getX() + 100, replaceLabel.getY(), 150, replaceLabel.getHeight());

        add(replaceLabel);
        add(replaceField);
        status = new JLabel();
        status.setBounds(10, 70, 150, 20);
        add(status);

        findNextBtn = new JButton("Find Next");
        findNextBtn.setBounds(10, 100, 120, 20);
        findNextBtn.addActionListener(new ActionListener() {
            private String prevText = null;
            private Pattern pattern = null;
            private Matcher matcher = null;
            private int offset = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String word = findField.getText();
                if (word.equals("") || textPane == null) {
                    return;
                }

                if (prevText == null || !prevText.equals(word)) {
                    offset = 0;
                    prevText = word;
                    pattern = Pattern.compile(word);
                }

                matcher = pattern.matcher(textPane.getText());
                while (!matcher.find(offset)) {
                    if (offset == 0) {
                        status.setText("Cannot find: " + word);
                        status.repaint();
                        return;
                    } else {
                        offset = 0;
                    }

                }

                textPane.setSelectionStart(matcher.start());
                textPane.setSelectionEnd(offset = matcher.end());
                status.setText("");
                status.repaint();
            }

        });

        add(findNextBtn);

        replaceBtn = new JButton("Replace");
        replaceBtn.setBounds(findNextBtn.getX() + findNextBtn.getWidth() + 10,
                findNextBtn.getY(), findNextBtn.getWidth(), findNextBtn.getHeight());
        replaceBtn.addActionListener(new ActionListener() {
            private String prevText = null;
            private Pattern pattern = null;
            private Matcher matcher = null;
            private int offset = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                String word = findField.getText();
                if (word.equals("") || textPane == null) {
                    return;
                }

                if (prevText == null || !prevText.equals(word)) {
                    offset = 0;
                    prevText = word;
                    pattern = Pattern.compile(word);
                }

                matcher = pattern.matcher(textPane.getText());
                while (!matcher.find(offset)) {
                    if (offset == 0) {
                        status.setText("Cannot find: " + word);
                        status.repaint();
                        return;
                    } else {
                        offset = 0;
                    }

                }

                textPane.setText(matcher.replaceFirst(replaceField.getText()));
                status.setText("");
                status.repaint();
            }
        });

        add(replaceBtn);

        replaceAllBtn = new JButton("Replace All");
        replaceAllBtn.setBounds(replaceBtn.getX() + findNextBtn.getWidth() + 10,
                findNextBtn.getY(), findNextBtn.getWidth(), findNextBtn.getHeight());
        replaceAllBtn.addActionListener(e -> {
            Pattern pattern = Pattern.compile(findField.getText());
            String word = findField.getText();
            Matcher matcher = pattern.matcher(textPane.getText());

            if (word.equals("") || textPane == null) {
                return;
            }

            int offset = 0;
            while (matcher.find(offset)) {
                offset = matcher.end();
            }

            textPane.setText(matcher.replaceAll(replaceField.getText()));
        });

        add(replaceAllBtn);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

}
