package keyword;

import org.json.JSONObject;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.logging.Logger;

/**
 * The class used to convert plain text -> Json
 *
 * @author Binbin Yan
 * @version 1.0
 */
public class ToJson {

    public static void main(String[] args){
        try{
            Logger log = Logger.getLogger("Log");
            JSONObject color1 = new JSONObject();
            JSONObject color2 = new JSONObject();
            JSONObject color3= new JSONObject();
            int key = 1;
            String path = "";
            try {
                path = Thread.currentThread().getContextClassLoader().getResource("CKeyword.txt").getPath();
            } catch (NullPointerException e) {
                log.info("Cannot find this file");
                return;
            }
            File filename = new File(path);
            InputStreamReader read = new InputStreamReader(new FileInputStream(filename));
            BufferedReader temp = new BufferedReader(read);
            String get = "";
            get = temp.readLine();
            if(get.equals("Red")){
                while(true){
                    get = temp.readLine();
                    if(get.equals("Blue")){
                        break;
                    }
                    else{color1.put(key+"",get);}
                    key ++;
                }
            }
            if(get.equals("Blue")){
                key = 1;
                while(true){
                    get = temp.readLine();
                    if(get.equals("Purple")){
                        break;
                    }
                    else{color2.put(key+"",get);}
                    key ++;
                }
            }
            if(get.equals("Purple")){
                key = 1;
                while(true){
                    get = temp.readLine();
                    if(get == null){
                        break;
                    }
                    color3.put(key+"",get);
                    key ++;
                }
            }
            String keyword = "{" + "\"Red\":" + color1 + "," + "\"Blue\":" + color2 + "," + "\"Purple\":" + color3 + "}";
            File jsonfile = new File(".\\src\\main\\resource\\cKeyword.json");
            jsonfile.createNewFile();
            BufferedWriter output = new BufferedWriter(new FileWriter(jsonfile));
            output.write(keyword);
            output.flush();
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
