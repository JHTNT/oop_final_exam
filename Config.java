import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {
    String diplayOptions[] = { "show_name", "show_start", "show_end", "show_degree",
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
}
