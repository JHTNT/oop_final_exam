import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
    String displayOptions[] = { "show_name", "show_start", "show_end", "show_degree",
            "show_state", "show_number", "show_catalog", "show_work" };
    Map<String, String> config = new HashMap<>();

    public Config() {
        try {
            Scanner read = new Scanner(new File("config.txt"));
            while (read.hasNextLine()) {
                String line = read.nextLine();
                if (line.length() != 0) {
                    String option[] = line.split(" *: *"); // There may be spaces around `:`
                    config.put(option[0], option[1]);
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getString(String str) {
        return config.get(str);
    }

    public boolean getBoolean(String str) {
        if (config.get(str).equals("true")) {
            return true;
        }
        return false;
    }

    public int nextId() {
        int nextId = Integer.parseInt(config.get("used_last_id")) + 1;
        config.put("used_last_id", String.format("%04d", nextId));
        saveConfig();
        return nextId;
    }

    public void saveConfig() {
        String result = "";
        for (Map.Entry<String, String> entry : config.entrySet()) {
            result += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        try {
            FileWriter fw = new FileWriter(new File("config.txt"));
            fw.write(result);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getShowField() {
        String conf[] = new String[8];
        for (int i = 0; i < 8; i++) {
            String c = displayOptions[i];
            conf[i] = c.substring(0, 1).toUpperCase() + c.substring(1);
            if (config.get(c).equals("true"))
                conf[i] += ":1";
            else
                conf[i] += ":0";
        }
        return conf;
    }

    public void setShowField(String newFields[]) {
        for (int i = 0; i < 8; i++) {
            config.put(displayOptions[i], newFields[i]);
        }
        saveConfig();
    }

    public String getSortOrder() {
        return config.get("show_sort_order");
    }

    public void setSortOrder(String order) {
        config.put("show_sort_order", order);
        saveConfig();
    }

    public void setSortField(String field) {
        config.put("show_sort_field", field);
        saveConfig();
    }
}
