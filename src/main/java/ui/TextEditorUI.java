package ui;

import highlight.SyntaxAwareDocument;
import io.IOAgent;
import search.FindDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import javax.swing.JTextPane;

import javax.swing.border.EmptyBorder;
import javax.swing.text.*;

/**
 * Main class for launching the application
 *
 * @author Major: Hongfei Ju, Paulo Jaime, Zitong Wei, Zelin Bao, Binbin Yan
 * @version 2.2
 */

public class TextEditorUI extends JFrame {
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newFileAction;
    private JMenuItem openFileAction;
    private JMenuItem saveFileAction;
    private JMenuItem closeCurTabAction;
    private JMenuItem exitAction;

    private JMenu editMenu;
    private JMenuItem copyAction;
    private JMenuItem pasteAction;
    private JMenuItem findAction;

    private JMenu syntaxMenu;
    private JMenuItem javaAction;
    private JMenuItem plainTextAction;

    private JMenu windowMenu;
    private JMenuItem minimizeAction;
    private JMenuItem zoomAction;

    private JMenu langMenu;
    private JMenuItem engLangAction;
    private JMenuItem frnLangAction;
    private JMenuItem spaLangAction;
    private JMenuItem porLangAction;
    private JMenuItem chnLangAction;

    private JMenu settingsMenu;
    private JMenuItem fontAction;

    private JMenu modeMenu;
    private ButtonGroup modeGroup;

    public JRadioButtonMenuItem dayModeAction;
    public JRadioButtonMenuItem nightModeAction;

    private JMenu helpMenu;
    private JMenuItem openIntroductionAction;
    private JMenuItem openCooperationAction;

    private JTabbedPane tabbedPane;
    private IOAgent ioAgent;

    private JToolBar quickMenu;
    private JButton quickNew;
    private JButton quickOpen;
    private JButton quickSave;
    private JButton quickClose;
    private JButton quickSyntax;
    private JButton quickCopy;
    private JButton quickPaste;
    private JButton quickFont;
    private JButton quickFind;
    private JButton quickLanguage;
    private JButton quickTheme;
    private String lang;
    private String syntax;

    private TerminalUI terminal;

    /**
     * mode is the theme code:
     * 0 - bright
     * 1 - dark
     */
    private int mode;

    private static final Logger log = Logger.getLogger("Log");

    private enum LANG {
        ENG, FRA, SPA, POR, CHN
    }

    /**
     * Constructor init steps:
     * 1. Init UI -- All UI components
     * 2. Init Agent -- The I/O Agent to manipulate underlying I/O operations
     * 3. Init Actions -- Action Listeners
     * 4. Assemble All UI components -- Add UI components into UI containers
     */
    private TextEditorUI() {
        initUI();
        initAgent();
        setupQuickMenu();
        initActions();
        assembleUIComponents();
        setupQuickMenu();
        quickMenuAction();
        decorateUI();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }


    public static void main(String[] args) {
        new TextEditorUI();

    }

    /**
     * Read icon resources from properties file
     * @return return a Map contains icon resources
     */
    private Map<String, ImageIcon> readIconRes() {
        Map<String, ImageIcon> resource = new HashMap<>();
        Properties prop = new Properties();
        InputStream input;

        try {
            input = Thread.currentThread().getContextClassLoader().getResourceAsStream("resPath.properties");
            prop.load(input);
            input.close();
        } catch (IOException e) {
            log.warning("Read icons failed");
        }

        resource.put("paste", new ImageIcon(getIconPath(prop.getProperty("PasteIcon"))));
        resource.put("cut", new ImageIcon(getIconPath(prop.getProperty("CutIcon"))));
        resource.put("copy", new ImageIcon(getIconPath(prop.getProperty("CopyIcon"))));
        resource.put("exit", new ImageIcon(getIconPath(prop.getProperty("ExitIcon"))));
        resource.put("find", new ImageIcon(getIconPath(prop.getProperty("FindIcon"))));

        resource.put("langEng", new ImageIcon(getIconPath(prop.getProperty("LangEngIcon"))));
        resource.put("langFrn", new ImageIcon(getIconPath(prop.getProperty("LangFrnIcon"))));
        resource.put("langSpa", new ImageIcon(getIconPath(prop.getProperty("LangSpaIcon"))));
        resource.put("langPor", new ImageIcon(getIconPath(prop.getProperty("LangPorIcon"))));
        resource.put("langChn", new ImageIcon(getIconPath(prop.getProperty("LangChnIcon"))));

        resource.put("new", new ImageIcon(getIconPath(prop.getProperty("NewIcon"))));
        resource.put("open", new ImageIcon(getIconPath(prop.getProperty("OpenIcon"))));
        resource.put("save", new ImageIcon(getIconPath(prop.getProperty("SaveIcon"))));
        resource.put("closeTab", new ImageIcon(getIconPath(prop.getProperty("CloseTab"))));
        resource.put("theme", new ImageIcon(getIconPath(prop.getProperty("ThemeIcon"))));
        resource.put("theme2", new ImageIcon(getIconPath(prop.getProperty("ThemeIcon2"))));
        resource.put("font", new ImageIcon(getIconPath(prop.getProperty("FontIcon"))));
        resource.put("java", new ImageIcon(getIconPath(prop.getProperty("JavaIcon"))));
        resource.put("txt", new ImageIcon(getIconPath(prop.getProperty("TxtIcon"))));

        return resource;
    }

    private URL getIconPath(String iconProperty) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(iconProperty);
        log.info("" + url);
        return url;
    }


    /**
     * Assemble UI components into containers
     */
    private void assembleUIComponents() {
        add(tabbedPane);
        setJMenuBar(menuBar);

        fileMenu.add(newFileAction);
        fileMenu.add(openFileAction);
        fileMenu.add(saveFileAction);
        fileMenu.add(closeCurTabAction);
        fileMenu.add(exitAction);

        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(findAction);

        syntaxMenu.add(javaAction);
        syntaxMenu.add(plainTextAction);

        langMenu.add(engLangAction);
        langMenu.add(frnLangAction);
        langMenu.add(spaLangAction);
        langMenu.add(porLangAction);
        langMenu.add(chnLangAction);

        dayModeAction.setSelected(true);
        modeGroup.add(dayModeAction);
        modeGroup.add(nightModeAction);
        modeMenu.add(dayModeAction);
        modeMenu.add(nightModeAction);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(syntaxMenu);
        menuBar.add(windowMenu);
        menuBar.add(langMenu);
        menuBar.add(settingsMenu);
        menuBar.add(modeMenu);
        menuBar.add(helpMenu);
    }

    private void decorateUI(){
        changeMenuAndButtonBorder();
        setMenuAndButtonSizeAndAlignment();
        changeQuickMenuAndButtonBorder();
    }

    /**
     * Init all UI components
     */
    private void initUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            log.severe("Failed to set UIManager LAF");
        }

        setSize(new Dimension(800, 400));
        mode = 0;
        lang = "ENG";
        syntax = "txt";

        Map<String, ImageIcon> iconMap = readIconRes();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newFileAction = new JMenuItem("New                            Ctrl+N", iconMap.get("new"));
        openFileAction = new JMenuItem("Open                          Ctrl+O", iconMap.get("open"));
        saveFileAction = new JMenuItem("Save                           Ctrl+S", iconMap.get("save"));
        closeCurTabAction = new JMenuItem("Close Current Tab    Ctrl+T", iconMap.get("closeTab"));
        exitAction = new JMenuItem("Exit                           Ctrl+E", iconMap.get("exit"));

        editMenu = new JMenu("Edit");
        copyAction = new JMenuItem("Copy    Ctrl+C", iconMap.get("copy"));
        pasteAction = new JMenuItem("Paste    Ctrl+V", iconMap.get("paste"));
        findAction = new JMenuItem("Find    Ctrl+F", iconMap.get("find"));

        syntaxMenu = new JMenu("Syntax");
        javaAction = new JMenuItem("Java                    Ctrl+J", iconMap.get("java"));
        plainTextAction = new JMenuItem("Plain text           Ctrl+P", iconMap.get("txt"));

        windowMenu = new JMenu("Window");

        langMenu = new JMenu("Language");
        engLangAction = new JMenuItem("English", iconMap.get("langEng"));
        frnLangAction = new JMenuItem("Français", iconMap.get("langFrn"));
        spaLangAction = new JMenuItem("Español", iconMap.get("langSpa"));
        porLangAction = new JMenuItem("Português", iconMap.get("langPor"));
        chnLangAction = new JMenuItem("中文", iconMap.get("langChn"));
        fontAction = new JMenuItem("Font        Ctrl+F", iconMap.get("font"));

        settingsMenu = new JMenu("Settings");
        helpMenu = new JMenu("Help");

        tabbedPane = new JTabbedPane();

        openIntroductionAction = new JMenuItem("Introduction        Ctrl+I");
        openCooperationAction = new JMenuItem("Cooperators        Ctrl+R");
        minimizeAction = new JMenuItem("Minimize          Ctrl+M");
        zoomAction = new JMenuItem("Zoom                Ctrl+Z");

        helpMenu.add(openIntroductionAction);
        helpMenu.add(openCooperationAction);
        windowMenu.add(minimizeAction);
        windowMenu.add(zoomAction);
        settingsMenu.add(fontAction);

        modeMenu = new JMenu("Mode");
        modeGroup = new ButtonGroup();
        dayModeAction = new JRadioButtonMenuItem("Day");
        nightModeAction = new JRadioButtonMenuItem("Night");
    }

    /**
     * Init Agent
     */
    private void initAgent() {
        ioAgent = new IOAgent(tabbedPane);
    }

    private void setupQuickMenu(){
        quickMenu = new JToolBar();
        Map<String, ImageIcon> iconMap = readIconRes();
        quickNew = new JButton(iconMap.get("new"));
        quickOpen = new JButton(iconMap.get("open"));
        quickSave = new JButton(iconMap.get("save"));
        quickClose = new JButton(iconMap.get("closeTab"));
        quickSyntax = new JButton(iconMap.get("txt"));
        quickCopy = new JButton(iconMap.get("copy"));
        quickPaste = new JButton(iconMap.get("paste"));
        quickFind = new JButton(iconMap.get("find"));
        quickFont = new JButton(iconMap.get("font"));
        quickLanguage = new JButton(iconMap.get("langEng"));
        quickTheme = new JButton(iconMap.get("theme"));

        quickMenu.add(quickNew);
        quickMenu.add(quickOpen);
        quickMenu.add(quickSave);
        quickMenu.add(quickClose);
        quickMenu.add(quickSyntax);
        quickMenu.add(quickCopy);
        quickMenu.add(quickPaste);
        quickMenu.add(quickFind);
        quickMenu.add(quickFont);
        quickMenu.add(quickLanguage);
        quickMenu.add(quickTheme);

        add(quickMenu, BorderLayout.NORTH);
    }

    private void setTabs(JTextPane textPane) {
        FontMetrics fm = textPane.getFontMetrics(textPane.getFont());
        int charWidth = fm.charWidth(' ');
        int tabWidth = charWidth * 4;
        TabStop[] tabs = new TabStop[5];

        for (int j = 0; j < tabs.length; j++) {
            int tab = j + 1;
            tabs[j] = new TabStop(tab * tabWidth);
        }

        TabSet tabSet = new TabSet(tabs);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributes, tabSet);
        int length = textPane.getDocument().getLength();
        textPane.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
    }

    /**
     * Init ActionListeners
     */
    private void initActions() {
        Map<String, ImageIcon> iconMap = readIconRes();

        newFileAction.addActionListener(e -> {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            tabbedPane.addTab("new", jPanel);
            JTextPane textPane = new JTextPane(new SyntaxAwareDocument("Plain Text"));
            setTabs(textPane);


            terminal =  new TerminalUI();
            jPanel.add(terminal,BorderLayout.SOUTH);

            if(mode == 0){
                textPane.setBackground(Color.white);
            }
            else {textPane.setBackground(Color.darkGray);}

            EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
            textPane.setBorder(eb);
            JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            TextLineNumber tln = new TextLineNumber(textPane);

            assert textPane.getDocument() instanceof SyntaxAwareDocument;
            SyntaxAwareDocument doc = (SyntaxAwareDocument) textPane.getDocument();

            if (mode == 0){
                tln.setBackground(Color.white);
                tln.setForeground(Color.gray);
            } else {
                tln.setBackground(Color.darkGray);
                tln.setForeground(Color.white);
                doc.switchMode();
            }

            scrollPane.setRowHeaderView( tln );



            jPanel.add(scrollPane, BorderLayout.CENTER);
        });

        openFileAction.addActionListener(e -> {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            Map<String, String> titleAndContent = ioAgent.read();
            JTextPane textPane;

            if (titleAndContent == null) {
                jPanel = null;
                textPane = null;
                titleAndContent = null;
            } else {
                String name = titleAndContent.get("name");
                tabbedPane.addTab(name, jPanel);
                int pos = name.lastIndexOf('.');
                String syntax = pos == -1 ? "Plain text" : name.substring(pos + 1);
                textPane = new JTextPane(new SyntaxAwareDocument(syntax));
                setTabs(textPane);


                terminal =  new TerminalUI();
                jPanel.add(terminal,BorderLayout.SOUTH);

                if(mode == 0){
                    textPane.setBackground(Color.white);
                } else {textPane.setBackground(Color.darkGray);}

                EmptyBorder eb = new EmptyBorder((new Insets(10,10,10,10)));
                textPane.setBorder(eb);
                textPane.setText(titleAndContent.get("content"));

                JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                TextLineNumber tln = new TextLineNumber(textPane);

                assert textPane.getDocument() instanceof SyntaxAwareDocument;
                SyntaxAwareDocument doc = (SyntaxAwareDocument) textPane.getDocument();
                if (mode == 0){
                    tln.setBackground(Color.white);
                    tln.setForeground(Color.gray);
                } else {
                    tln.setBackground(Color.darkGray);
                    tln.setForeground(Color.white);
                    doc.switchMode();
                }

                scrollPane.setRowHeaderView( tln );
                jPanel.add(scrollPane, BorderLayout.CENTER);
            }

        });

        saveFileAction.addActionListener(e -> {
            String extensionName = ioAgent.save();
            JTextPane textPane = getCurrentTextPane();
            if (textPane != null) {
                ((SyntaxAwareDocument) textPane.getDocument()).switchSyntax(extensionName);
            }

        });

        closeCurTabAction.addActionListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected != null) {
                ioAgent.delete();
                tabbedPane.remove(selected);
            }
        });

        exitAction.addActionListener(e -> System.exit(0));

        copyAction.addActionListener(e -> {
            String str = getSelectedTextFromTextPane();
            StringSelection stringSelection = new StringSelection (str);
            Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
            clipboard.setContents (stringSelection, null);
        });


        pasteAction.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataFlavor flavor = DataFlavor.stringFlavor;
            if (clipboard.isDataFlavorAvailable(flavor)) {
                JTextPane textPane = getCurrentTextPane();
                textPane.paste();
            }

        });

        findAction.addActionListener(e -> new FindDialog(this, getCurrentTextPane(), mode));

        javaAction.addActionListener(e -> {
            if(getCurrentTextPane()!=null)
            {
                syntax = "java";
                quickSyntax.setIcon(iconMap.get("java"));
                JTextPane pane = getCurrentTextPane();
                assert pane.getDocument() instanceof SyntaxAwareDocument;
                SyntaxAwareDocument doc = (SyntaxAwareDocument) pane.getDocument();
                doc.switchSyntax("Java");
            }
        });

        plainTextAction.addActionListener(e -> {
            if(getCurrentTextPane()!=null) {
                syntax = "txt";
                quickSyntax.setIcon(iconMap.get("txt"));
                JTextPane pane = getCurrentTextPane();
                assert pane.getDocument() instanceof SyntaxAwareDocument;
                SyntaxAwareDocument doc = (SyntaxAwareDocument) pane.getDocument();
                doc.switchSyntax("Plain text");
            }

        });

        engLangAction.addActionListener(e -> {
            changeUIText(LANG.ENG);
            lang = "ENG";
            quickLanguage.setIcon(iconMap.get("langEng"));
            setMenuAndButtonSizeAndAlignment();
        });

        frnLangAction.addActionListener(e -> {
            changeUIText(LANG.FRA);
            lang = "FRA";
            quickLanguage.setIcon(iconMap.get("langFrn"));
            setMenuAndButtonSizeAndAlignment();
        });

        spaLangAction.addActionListener(e -> {
            changeUIText(LANG.SPA);
            lang = "SPA";
            quickLanguage.setIcon(iconMap.get("langSpa"));
            setMenuAndButtonSizeAndAlignment();
        });

        porLangAction.addActionListener(e -> {
            changeUIText(LANG.POR);
            lang = "POR";
            quickLanguage.setIcon(iconMap.get("langPor"));
            setMenuAndButtonSizeAndAlignment();
        });

        chnLangAction.addActionListener(e -> {
            changeUIText(LANG.CHN);
            lang = "CHN";
            quickLanguage.setIcon(iconMap.get("langChn"));
            setMenuAndButtonSizeAndAlignment();
        });

        zoomAction.addActionListener(e -> setExtendedState(JFrame.MAXIMIZED_BOTH));

        minimizeAction.addActionListener(e -> setExtendedState(JFrame.ICONIFIED));


        openIntroductionAction.addActionListener(e -> {
            IntroFrame t = new IntroFrame();
            t.setVisible(true);
        });

        openCooperationAction.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Zitong Wei\n"
                    + "Binbin Yan\n"
                    + "Paulo Jaime\n"
                    + "Yiru Hu\n"
                    + "Hongfei Ju\n"
                    + "Zelin Bao", "Cooperators",JOptionPane.INFORMATION_MESSAGE);
        });


        fontAction.addActionListener(e -> {
            JTextPane pane = getCurrentTextPane();
            if (pane != null) {
                FontFrame fontFrame = new FontFrame(pane);
                fontFrame.setVisible(true);
            }

        });

        dayModeAction.addActionListener(e -> {
            mode = 0;
            quickFont.setIcon(iconMap.get("font"));
            quickTheme.setIcon(iconMap.get("theme"));
            changeMenuAndButtonMode(UIManager.getColor("Panel.background"), Color.black);
            changeTextArea(Color.white, Color.black);
        });

        nightModeAction.addActionListener(e -> {
            mode = 1;
            quickFont.setIcon(iconMap.get("font"));
            quickTheme.setIcon(iconMap.get("theme2"));
            changeMenuAndButtonMode(Color.darkGray, Color.white);
            changeTextArea(Color.darkGray, Color.white);
        });
    }

    private  void quickMenuAction(){
        Map<String, ImageIcon> iconMap = readIconRes();

        quickNew. addActionListener(e -> {
            JPanel jpanel = new JPanel();

            JPanel jPanel1 = new JPanel();
            jpanel.setLayout(new BorderLayout());
            tabbedPane.addTab("new", jpanel);
            JTextPane textPane = new JTextPane(new SyntaxAwareDocument("Plain Text"));
            setTabs(textPane);


            JPanel jPanel2 = new JPanel();
            terminal =  new TerminalUI();
            jPanel2.add(terminal,BorderLayout.SOUTH);



            if(mode == 0){
                textPane.setBackground(Color.white);
            }
            else {textPane.setBackground(Color.darkGray);}

            EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
            textPane.setBorder(eb);
            JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            TextLineNumber tln = new TextLineNumber(textPane);

            assert textPane.getDocument() instanceof SyntaxAwareDocument;
            SyntaxAwareDocument doc = (SyntaxAwareDocument) textPane.getDocument();

            if (mode == 0){
                tln.setBackground(Color.white);
                tln.setForeground(Color.gray);
            } else {
                tln.setBackground(Color.darkGray);
                tln.setForeground(Color.white);
                doc.switchMode();
            }

            scrollPane.setRowHeaderView( tln );
            jPanel1.add(scrollPane, BorderLayout.CENTER);
            jpanel.setSize(jpanel.getParent().getSize());
            jpanel.add(jPanel1,BorderLayout.NORTH);
            jpanel.add(jPanel2,BorderLayout.SOUTH);
        });

        quickOpen. addActionListener(e -> {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            Map<String, String> titleAndContent = ioAgent.read();
            JTextPane textPane;

            //JPanel jpanel2 = new JPanel();
            //terminal =  new TerminalUI();
            //jpanel2.add(terminal,BorderLayout.SOUTH);

            if (titleAndContent == null) {
                jPanel = null;
                textPane = null;
                titleAndContent = null;
            } else {
                String name = titleAndContent.get("name");
                tabbedPane.addTab(name, jPanel);
                int pos = name.lastIndexOf('.');
                String syntax = pos == -1 ? "Plain text" : name.substring(pos + 1);
                textPane = new JTextPane(new SyntaxAwareDocument(syntax));
                setTabs(textPane);

                if(mode == 0){
                    textPane.setBackground(Color.white);
                } else {textPane.setBackground(Color.darkGray);}

                EmptyBorder eb = new EmptyBorder((new Insets(10,10,10,10)));
                textPane.setBorder(eb);
                textPane.setText(titleAndContent.get("content"));

                JScrollPane scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                TextLineNumber tln = new TextLineNumber(textPane);

                assert textPane.getDocument() instanceof SyntaxAwareDocument;
                SyntaxAwareDocument doc = (SyntaxAwareDocument) textPane.getDocument();
                if (mode == 0){
                    tln.setBackground(Color.white);
                    tln.setForeground(Color.gray);
                } else {
                    tln.setBackground(Color.darkGray);
                    tln.setForeground(Color.white);
                    doc.switchMode();
                }

                scrollPane.setRowHeaderView( tln );
                jPanel.add(scrollPane, BorderLayout.CENTER);
            }

        });

        quickSave.addActionListener(e -> {
            String extensionName = ioAgent.save();
            JTextPane textPane = getCurrentTextPane();
            if (textPane != null) {
                ((SyntaxAwareDocument) textPane.getDocument()).switchSyntax(extensionName);
            }

        });

        quickClose.addActionListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected != null) {
                ioAgent.delete();
                tabbedPane.remove(selected);
            }
        });

        quickSyntax.addActionListener(e -> {
            if(getCurrentTextPane()!=null)
            {
                JTextPane pane = getCurrentTextPane();
                if (syntax == "txt") {
                    syntax = "java";
                    quickSyntax.setIcon(iconMap.get("java"));
                    assert pane.getDocument() instanceof SyntaxAwareDocument;
                    SyntaxAwareDocument doc = (SyntaxAwareDocument) pane.getDocument();
                    doc.switchSyntax("Java");
                } else {
                    syntax = "txt";
                    quickSyntax.setIcon(iconMap.get("txt"));
                    assert pane.getDocument() instanceof SyntaxAwareDocument;
                    SyntaxAwareDocument doc = (SyntaxAwareDocument) pane.getDocument();
                    doc.switchSyntax("Plain text");
                }
            }

        });


        quickCopy.addActionListener(e -> {
            String str = getSelectedTextFromTextPane();
            StringSelection stringSelection = new StringSelection (str);
            Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
            clipboard.setContents (stringSelection, null);
        });


        quickPaste.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataFlavor flavor = DataFlavor.stringFlavor;
            if (clipboard.isDataFlavorAvailable(flavor)) {
                JTextPane textPane = getCurrentTextPane();
                textPane.paste();
            }

        });

        quickFont.addActionListener(e -> {
            JTextPane pane = getCurrentTextPane();
            if (pane != null) {
                FontFrame fontFrame = new FontFrame(pane);
                fontFrame.setVisible(true);
            }

        });

        quickFind.addActionListener(e -> new FindDialog(this, getCurrentTextPane(), mode));

        quickLanguage.addActionListener(e -> {
            if(lang == "ENG") {
                changeUIText(LANG.FRA);
                lang = "FRA";
                quickLanguage.setIcon(iconMap.get("langFrn"));
                setMenuAndButtonSizeAndAlignment();
            }
            else if(lang == "FRA") {
                changeUIText(LANG.SPA);
                lang = "SPA";
                quickLanguage.setIcon(iconMap.get("langSpa"));
                setMenuAndButtonSizeAndAlignment();
            }
            else if(lang == "SPA") {
                changeUIText(LANG.POR);
                lang = "POR";
                quickLanguage.setIcon(iconMap.get("langPor"));
                setMenuAndButtonSizeAndAlignment();
            }
            else if(lang == "POR") {
                changeUIText(LANG.CHN);
                lang = "CHN";
                quickLanguage.setIcon(iconMap.get("langChn"));
                setMenuAndButtonSizeAndAlignment();
            }
            else{
                changeUIText(LANG.ENG);
                lang = "ENG";
                quickLanguage.setIcon(iconMap.get("langEng"));
                setMenuAndButtonSizeAndAlignment();
            }
        });

        quickTheme.addActionListener(e -> {
            if(mode==0){
                mode = 1;
                nightModeAction.setSelected(true);
                quickFont.setIcon(iconMap.get("font"));
                quickTheme.setIcon(iconMap.get("theme2"));
                changeMenuAndButtonMode(Color.darkGray, Color.white);
                changeTextArea(Color.darkGray, Color.white);
            }
            else {
                mode = 0;
                dayModeAction.setSelected(true);
                quickFont.setIcon(iconMap.get("font"));
                quickTheme.setIcon(iconMap.get("theme"));
                changeMenuAndButtonMode(UIManager.getColor("Panel.background"), Color.black);
                changeTextArea(Color.white, Color.black);
            }

        });

    }

    /**
     * Change the UI label displaying language
     * @param lang use LANG Enum to specify which lang
     */
    private void changeUIText(LANG lang) {
        String language;

        switch (lang) {
            case ENG:
                language = "EN";
                break;
            case FRA:
                language = "FR";
                break;
            case SPA:
                language = "SP";
                break;
            case POR:
                language = "PR";
                break;
            case CHN:
                language = "CN";
                break;
            default:
                language = "EN";
        }

        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("langProp.properties"), "UTF-8" ));
        } catch (IOException ioe) {
            log.warning("Read language properties failed");
            throw new RuntimeException(ioe.getMessage());
        }

        fileMenu.setText(prop.getProperty("File" + language));
        newFileAction.setText(prop.getProperty("NewFile" + language));
        openFileAction.setText(prop.getProperty("OpenFile" + language));
        saveFileAction.setText(prop.getProperty("SaveFile" + language));
        closeCurTabAction.setText(prop.getProperty("CloseTab" + language));
        exitAction.setText(prop.getProperty("Exit" + language));
        editMenu.setText(prop.getProperty("Edit" + language));
        copyAction.setText(prop.getProperty("Copy" + language));
        pasteAction.setText(prop.getProperty("Paste" + language));
        syntaxMenu.setText(prop.getProperty("Syntax" + language));
        windowMenu.setText(prop.getProperty("Window" + language));
        langMenu.setText(prop.getProperty("Language" + language));
        settingsMenu.setText(prop.getProperty("Settings" + language));
        helpMenu.setText(prop.getProperty("Help" + language));
        modeMenu.setText(prop.getProperty("Mode" + language));
    }

    /**
     * Read Current text from TextEditor
     * @return
     */
    private JTextPane getCurrentTextPane() {
        try {
            Component component = tabbedPane.getSelectedComponent();
            JScrollPane scrollPane = (JScrollPane) ((JPanel) component).getComponents()[0];
            JViewport viewport = (JViewport) scrollPane.getComponent(0);
            return (JTextPane) viewport.getComponent(0);
        } catch (NullPointerException npe) {
            log.info("No tab found");
            return null;
        }

    }

    /**
     * change foreground and background color
     */
    private void changeMenuAndButtonMode(Color background, Color foreground) {
        getContentPane().setBackground(background);
        getContentPane().setForeground(foreground);
        ((JPanel) getContentPane()).setOpaque(true);

        getGlassPane().setBackground(background);
        getGlassPane().setForeground(foreground);
        ((JPanel) getGlassPane()).setOpaque(true);

        menuBar.setBackground(background);
        menuBar.setForeground(foreground);
        fileMenu.setBackground(background);
        editMenu.setBackground(background);
        syntaxMenu.setBackground(background);
        windowMenu.setBackground(background);
        langMenu.setBackground(background);
        settingsMenu.setBackground(background);
        modeMenu.setBackground(background);
        helpMenu.setBackground(background);
        fileMenu.setForeground(foreground);
        editMenu.setForeground(foreground);
        syntaxMenu.setForeground(foreground);
        windowMenu.setForeground(foreground);
        langMenu.setForeground(foreground);
        settingsMenu.setForeground(foreground);
        modeMenu.setForeground(foreground);
        helpMenu.setForeground(foreground);

        newFileAction.setBackground(background);
        openFileAction.setBackground(background);
        saveFileAction.setBackground(background);;
        closeCurTabAction.setBackground(background);;
        exitAction.setBackground(background);
        copyAction.setBackground(background);
        pasteAction.setBackground(background);
        findAction.setBackground(background);
        javaAction.setBackground(background);
        plainTextAction.setBackground(background);
        engLangAction.setBackground(background);
        frnLangAction.setBackground(background);
        spaLangAction.setBackground(background);
        porLangAction.setBackground(background);
        chnLangAction.setBackground(background);
        openIntroductionAction.setBackground(background);
        openCooperationAction.setBackground(background);
        minimizeAction.setBackground(background);
        zoomAction.setBackground(background);
        fontAction.setBackground(background);
        dayModeAction.setBackground(background);
        nightModeAction.setBackground(background);

        newFileAction.setForeground(foreground);
        openFileAction.setForeground(foreground);
        saveFileAction.setForeground(foreground);;
        closeCurTabAction.setForeground(foreground);
        exitAction.setForeground(foreground);
        copyAction.setForeground(foreground);
        pasteAction.setForeground(foreground);
        findAction.setForeground(foreground);
        javaAction.setForeground(foreground);
        plainTextAction.setForeground(foreground);
        engLangAction.setForeground(foreground);
        frnLangAction.setForeground(foreground);
        spaLangAction.setForeground(foreground);
        porLangAction.setForeground(foreground);
        chnLangAction.setForeground(foreground);
        tabbedPane.setForeground(foreground);
        openIntroductionAction.setForeground(foreground);
        openCooperationAction.setForeground(foreground);
        minimizeAction.setForeground(foreground);
        zoomAction.setForeground(foreground);
        fontAction.setForeground(foreground);
        dayModeAction.setForeground(foreground);
        nightModeAction.setForeground(foreground);

    }

    /**
     * add effect on the borders of menubar and buttons
     */
    private void changeMenuAndButtonBorder(){
        menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
        fileMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        editMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        syntaxMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        windowMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        langMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        settingsMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        modeMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        helpMenu.setBorder(BorderFactory.createRaisedBevelBorder());

        newFileAction.setBorder(BorderFactory.createRaisedBevelBorder());
        openFileAction.setBorder(BorderFactory.createRaisedBevelBorder());
        saveFileAction.setBorder(BorderFactory.createRaisedBevelBorder());
        closeCurTabAction.setBorder(BorderFactory.createRaisedBevelBorder());
        exitAction.setBorder(BorderFactory.createRaisedBevelBorder());
        copyAction.setBorder(BorderFactory.createRaisedBevelBorder());
        pasteAction.setBorder(BorderFactory.createRaisedBevelBorder());
        findAction.setBorder(BorderFactory.createRaisedBevelBorder());
        javaAction.setBorder(BorderFactory.createRaisedBevelBorder());
        plainTextAction.setBorder(BorderFactory.createRaisedBevelBorder());
        engLangAction.setBorder(BorderFactory.createRaisedBevelBorder());
        frnLangAction.setBorder(BorderFactory.createRaisedBevelBorder());
        spaLangAction.setBorder(BorderFactory.createRaisedBevelBorder());
        porLangAction.setBorder(BorderFactory.createRaisedBevelBorder());
        chnLangAction.setBorder(BorderFactory.createRaisedBevelBorder());
        openIntroductionAction.setBorder(BorderFactory.createRaisedBevelBorder());
        openCooperationAction.setBorder(BorderFactory.createRaisedBevelBorder());
        minimizeAction.setBorder(BorderFactory.createRaisedBevelBorder());
        zoomAction.setBorder(BorderFactory.createRaisedBevelBorder());
        fontAction.setBorder(BorderFactory.createRaisedBevelBorder());
        dayModeAction.setBorder(BorderFactory.createRaisedBevelBorder());
        nightModeAction.setBorder(BorderFactory.createRaisedBevelBorder());
        newFileAction.setBorder(BorderFactory.createRaisedBevelBorder());
        openFileAction.setBorder(BorderFactory.createRaisedBevelBorder());
        saveFileAction.setBorder(BorderFactory.createRaisedBevelBorder());
        closeCurTabAction.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    /**
     * adjust the size of buttons
     */
    private void setMenuAndButtonSizeAndAlignment(){
        int fileActionButtonWidth = System.getProperty("os.name").contains("Windows") ? 200 : 250;
        FontMetrics fontMetrics = fileMenu.getFontMetrics(fileMenu.getFont());
        fileMenu.setHorizontalAlignment(0);
        fileMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(fileMenu.getText()) + 20,30));
        langMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(langMenu.getText()) + 20,30));
        editMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(editMenu.getText()) + 20,30));
        syntaxMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(syntaxMenu.getText()) + 20,30));
        windowMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(windowMenu.getText()) + 20,30));
        langMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(langMenu.getText()) + 20,30));
        settingsMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(settingsMenu.getText()) + 20,30));
        modeMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(modeMenu.getText()) + 20,30));
        helpMenu.setPreferredSize(new Dimension(fontMetrics.stringWidth(helpMenu.getText()) + 20,30));

        newFileAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(newFileAction.getText()) + 50,35));
        openFileAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(openFileAction.getText()) + 50,35));
        saveFileAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(saveFileAction.getText()) + 50,35));
        closeCurTabAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(closeCurTabAction.getText()) + 50,35));
        exitAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(exitAction.getText()) + 50,35));
        copyAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(copyAction.getText()) + 50,35));
        pasteAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(pasteAction.getText()) + 50,35));
        findAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(findAction.getText()) + 50,35));
        javaAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(javaAction.getText()) + 50,35));
        plainTextAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(plainTextAction.getText()) + 50,35));
        engLangAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(engLangAction.getText()) + 50,35));
        frnLangAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(frnLangAction.getText()) + 50,35));
        spaLangAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(spaLangAction.getText()) + 50,35));
        porLangAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(porLangAction.getText()) + 50,35));
        chnLangAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(chnLangAction.getText()) + 50,35));
        openIntroductionAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(openIntroductionAction.getText()) + 20,35));
        openCooperationAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(openCooperationAction.getText()) + 20,35));
        minimizeAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(minimizeAction.getText()) + 20,35));
        zoomAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(zoomAction.getText()) + 20,35));
        fontAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(fontAction.getText()) + 50,35));
        dayModeAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(dayModeAction.getText()) + 50,35));
        nightModeAction.setPreferredSize(new Dimension(fontMetrics.stringWidth(nightModeAction.getText()) + 50,35));

    }

    private void changeQuickMenuAndButtonBorder(){
        quickMenu.setBorder(BorderFactory.createRaisedBevelBorder());
        quickNew.setBorder(BorderFactory.createRaisedBevelBorder());
        quickOpen.setBorder(BorderFactory.createRaisedBevelBorder());
        quickSave.setBorder(BorderFactory.createRaisedBevelBorder());
        quickClose.setBorder(BorderFactory.createRaisedBevelBorder());
        quickSyntax.setBorder(BorderFactory.createRaisedBevelBorder());
        quickCopy.setBorder(BorderFactory.createRaisedBevelBorder());
        quickPaste.setBorder(BorderFactory.createRaisedBevelBorder());
        quickFind.setBorder(BorderFactory.createRaisedBevelBorder());
        quickFont.setBorder(BorderFactory.createRaisedBevelBorder());
        quickLanguage.setBorder(BorderFactory.createRaisedBevelBorder());
        quickTheme.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void changeTextArea(Color background, Color foreground) {

        tabbedPane.setForeground(foreground);
        tabbedPane.setBackground(background);
        quickMenu.setForeground(foreground);
        quickMenu.setBackground(background);
        quickNew.setForeground(foreground);
        quickNew.setBackground(background);
        quickOpen.setForeground(foreground);
        quickOpen.setBackground(background);
        quickSave.setForeground(foreground);
        quickSave.setBackground(background);
        quickClose.setForeground(foreground);
        quickClose.setBackground(background);
        quickSyntax.setForeground(foreground);
        quickSyntax.setBackground(background);
        quickCopy.setForeground(foreground);
        quickCopy.setBackground(background);
        quickPaste.setForeground(foreground);
        quickPaste.setBackground(background);
        quickFind.setForeground(foreground);
        quickFind.setBackground(background);
        quickFont.setForeground(foreground);
        quickFont.setBackground(background);
        quickLanguage.setForeground(foreground);
        quickLanguage.setBackground(background);
        quickTheme.setForeground(foreground);
        quickTheme.setBackground(background);

        int totalTabs = tabbedPane.getTabCount();
        for(int i = 0; i <totalTabs; i++){
            Component tab = tabbedPane.getComponentAt(i);

            if(((JPanel) tab).getComponent(0) instanceof JScrollPane){



                JScrollPane scrollPane = (JScrollPane) ((JPanel) tab).getComponent(0);
                JViewport viewport = (JViewport) scrollPane.getComponent(0);
                JTextPane pane = (JTextPane) viewport.getComponent(0);
                TextLineNumber tln = new TextLineNumber(pane);
                tln.setBackground(background);
                if(background == Color.white){
                    tln.setForeground(Color.gray);
                }
                else{ tln.setForeground(foreground);}

                scrollPane.setRowHeaderView(tln);
                pane.setForeground(foreground);
                pane.setBackground(background);

                assert pane.getDocument() instanceof SyntaxAwareDocument;
                SyntaxAwareDocument doc = (SyntaxAwareDocument) pane.getDocument();
                if(background == Color.darkGray && doc.mode == SyntaxAwareDocument.MODE.dark || background == Color.white && doc.mode == SyntaxAwareDocument.MODE.bright ) {

                }
                else{
                    doc.switchMode();
                }

            }else{

            }
        }
    }

    private String getSelectedTextFromTextPane() {
        return getCurrentTextPane() == null ? null : getCurrentTextPane().getSelectedText();
    }

}
