package keyword;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.util.logging.Logger;

/**
 * The class used to read color information
 *
 * @author Yiru Hu
 * @version 1.3
 */
public class KeywordDB {
	private Map<String, String> map;
	private static final Logger log = Logger.getLogger("Log");

	private String commentTag;
	private String[] mCommentPair;
	private String[] stringTag;
	private boolean isPlainText;

	public KeywordDB() {
	    this("Plain text");
        commentTag = null;
        mCommentPair = null;
        stringTag = null;
        isPlainText = true;
    }

	public KeywordDB(String syntax) {
        map = new HashMap<>();
        switchSyntax(syntax);
    }

    public boolean isPlainText() {
	    return isPlainText;
    }

    /**
     * Return the comment tag of the current syntax. E.g. for java it will return '//'
     * @return commentTag
     */
    public String getCommentTag() { return commentTag;}

    /**
     * Return the multiple line comments tag of the current syntax.
     * @return mCommentPair
     */
    public String[] getMCommentTags() { return mCommentPair;}

    /**
     * Return the string tag of the current syntax.
     * @return commentTag
     */
    public String[] getStringTag(){ return stringTag;}

    /**
     * Get the Color of the current keyword
     * @param keyword the word
     * @return java.awt.Color
     */
    public Color matchColor(String keyword) {
	    String colorName = map.getOrDefault(keyword, "Black");
        switch (colorName) {
            case "Red":
                return Color.red;
            case "Blue":
                return Color.blue;
            case "Purple":
                return Color.magenta;
            case "Grey":
                return Color.gray;
            default:
                return Color.black;
        }

    }

    /**
     * Get the Color of the current keyword in dark mode
     * @param keyword the word
     * @return java.awt.Color
     */
    public Color matchDarkMode(String keyword){
        String colorName = map.getOrDefault(keyword, "Black");
        switch (colorName) {
            case "Red":
                return Color.CYAN;
            case "Blue":
                return Color.yellow;
            case "Purple":
                return Color.green;
            case "Grey":
                return Color.lightGray;
            default:
                return Color.white;
        }

    }

    /**
     * Switch the syntax
     * @param syntax the syntax want to change
     */
    public void switchSyntax(String syntax) {
        map.clear();
        String content;
        try {
            content = readFile(syntax + "Keyword.json");
        } catch (Exception e) {
            log.info("No syntax support for + " + syntax);
            map.clear();
            stringTag = new String[0];
            mCommentPair = new String[0];
            commentTag = "";
            isPlainText = true;
            return;
        }

        JSONObject jsonObject = new JSONObject(content);
        for (String k : jsonObject.keySet()) {
            JSONArray arr = (JSONArray) jsonObject.get(k);
            if (k.equals("comment")) {
                commentTag = (String) arr.get(0);
            } else if (k.equals("multi-comment")) {
                mCommentPair = new String[] {(String) arr.get(0), (String) arr.get(1)};
            } else if(k.equals("string")){
                stringTag = new String[] {(String) arr.get(0), (String) arr.get(1)};
            }
            else {
                for (Object anArr : arr) {
                    map.put(anArr.toString(), k);
                }

            }

        }

        isPlainText = false;

	}

	private String readFile(String name) throws Exception {
        InputStream bis = (InputStream) Thread.currentThread().getContextClassLoader().getResource(name).getContent();
        DataInputStream dis = new DataInputStream(bis);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = dis.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

}

