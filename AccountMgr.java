import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
                if (line.length() != 0) {
                    String acc[] = line.split(" ");
                    accounts.put(acc[0], acc[1]);
                }
            }
            read.close();
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

    public void showAccount() {
        System.out.println("[Account]    [Password]");
        for (Map.Entry<String, String> entry : accounts.entrySet()) {
            System.out.println(String.format("%-12s ", entry.getKey()) + entry.getValue());
        }
    }

    public void addAccount(String acc, String pw) {
        accounts.put(acc, pw);
        saveAccount();
    }

    public boolean removeAccount(String acc) {
        if (accounts.get(acc) != null) {
            accounts.remove(acc);
            saveAccount();
            return true;
        } else {
            return false;
        }
    }

    public void modifyAccount(String oldAcc, String newAcc, String newPw) {
        String oldPw = accounts.get(oldAcc);
        if (newAcc.equals("") && newPw.equals("")) {
            return;
        } else if (newAcc.equals("") && !newPw.equals("")) {
            accounts.put(oldAcc, newPw);
        } else if (!newAcc.equals("") && newPw.equals("")) {
            accounts.remove(oldAcc);
            accounts.put(newAcc, oldPw);
        } else {
            accounts.remove(oldAcc);
            accounts.put(newAcc, newPw);
        }
    }

    public void saveAccount() {
        String result = "";
        for (Map.Entry<String, String> entry : accounts.entrySet()) {
            result += entry.getKey() + " " + entry.getValue() + "\n";
        }
        try {
            FileWriter fw = new FileWriter(new File("account.txt"));
            fw.write(result);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
