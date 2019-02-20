package io;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The class is used to manipulating the I/O operations
 *
 * @author Zitong Wei, Zelin Bao
 * @version 1.1
 */
public class IOAgent {
    private JTabbedPane tabManager;
    private static final Logger log = Logger.getLogger("Log");


    public IOAgent(JTabbedPane tabMgr) {
        tabManager = tabMgr;
    }

    /**
     * Save file method
     */
    public String save() {
        int idx = tabManager.getSelectedIndex();
        if (idx == -1) {
            return "";
        }

        Map<String, String> nameAndContent = acquireTabContent(idx);
        String fileName = doSave(nameAndContent.get("name"), nameAndContent.get("content"));
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    /**
     * Read the title and content of the file
     * @return pair: "name" - title; "content" - content
     */
    public Map<String, String> read() {
        Map<String, String> result = new HashMap<>();
        JFileChooser jFileChooser = new JFileChooser();
        File file;
        String path;
        try {
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jFileChooser.showOpenDialog(null);
            file = jFileChooser.getSelectedFile();
            path = file.getPath();
        } catch (NullPointerException npe) {
            log.info("Read file failed");
            return null;
        }

        if (PathDB.containsPath(path)) {
            return result;
        }

        PathDB.addPath(path);
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            br.close();
            fileReader.close();
        } catch (IOException ex) {
            log.warning("Read file failed");
            log.warning("May caused by illegal path");
            return null;
        }

        result.put("name", file.getName());
        result.put("content", sb.toString());
        return result;
    }

    /**
     * Close current tab file
     */
    public void delete() {
        int idx = tabManager.getSelectedIndex();
        if (idx == -1) {
            return;
        }

        PathDB.delete(tabManager.getTitleAt(idx));
    }

    private String doSave(String title, String content){
        String path = PathDB.getPath(title);
        String fileName;

        if (path == null) {
            fileName = saveAs(content);
        } else {
            try {
                FileWriter fw = new FileWriter(path);
                fw.write(content);
                fw.flush();
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            fileName = path.substring(path.lastIndexOf('/'));
        }

        return fileName;
    }

    private String saveAs(String content) {
        JFileChooser chooser = new JFileChooser();
        JFrame saveAsFrame = new JFrame();
        String fileName = "";
        int res = chooser.showSaveDialog(saveAsFrame);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                tabManager.setTitleAt(tabManager.getSelectedIndex(), file.getName());
                PathDB.addPath(file.getPath());
                FileWriter fw = new FileWriter(file.getPath());
                fw.write(content);
                fw.flush();
                fw.close();
                fileName = file.getName().substring(file.getName().lastIndexOf('.') + 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return fileName;
    }

    private Map<String, String> acquireTabContent(int idx) {
        Map<String, String> result = new HashMap<>();
        Component component = tabManager.getComponentAt(idx);
        JScrollPane scrollPane = (JScrollPane) ((JPanel) component).getComponents()[0];
        JViewport viewport = (JViewport) scrollPane.getComponent(0);
        JTextPane textArea = (JTextPane) viewport.getComponent(0);
        result.put("name", tabManager.getTitleAt(idx));
        result.put("content", textArea.getText());
        return result;
    }

}
