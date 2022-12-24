import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class AccountMgr {
    String verifyString;
    Map<String, String> accounts = new HashMap<>();

    public AccountMgr() {
        try {
            Scanner read = new Scanner(new File("account.txt"));
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String acc[] = line.split(" ");
                accounts.put(acc[0], acc[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        verifyString = DataMgr.config.getString("verify_string");
    }

    public boolean login(String acc, String pw, String verify) {
        if (!verify.equals(verifyString)) {
            return false;
        }
        if (accounts.get(acc) != null && accounts.get(acc).equals(pw)) {
            return true;
        }
        return false;
    }
}
