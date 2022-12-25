import java.io.File;
import java.io.FileNotFoundException;
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
                String option[] = line.split(" *: *"); // There may be spaces around `:`
                config.put(option[0], option[1]);
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
}
