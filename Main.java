import java.util.Scanner;

public class Main {
    private static final AccountMgr accMgr = new AccountMgr();
    private static final DataMgr dataMgr = new DataMgr();
    public static final Scanner scanner = new Scanner(System.in);

    private static final String FIELDS[] = { "ID", "Name", "Start", "End", "Degree",
            "State", "Number", "Work" };

    public static void login() {
        for (int i = 0; i < 3; i++) {
            System.out.println("Account:");
            String acc = scanner.nextLine();
            System.out.println("Password:");
            String pw = scanner.nextLine();
            System.out.println("Verify_string:" + DataMgr.config.getString("verify_string"));
            System.out.println("Input_Verify_string:");
            String verify = scanner.nextLine();

            if (accMgr.login(acc, pw, verify)) {
                System.out.println("Login_success");
                return;
            }
            System.out.println("Error_wrong_account_password_or_verify_string");
        }
        System.exit(0);
    }

    public static void printMenu() {
        System.out.println("****************************************\n" +
                "1.Show_a 2.Show_p 3.Show_by_c 4.Search 5.Mod 6.Del 7.Add_job\n" +
                "8.Add_cat 9.Show_cat 10.Set_field 11.Set_page 12.Set_order 13.Set_sort\n" +
                "14.Show_r 15.Opt 16.Show_acc 17.Add_acc 18.Del_acc 19.Mod_acc 20.Logout 99.Exit\n" +
                "****************************************");
    }

    public static void printSubMenu() {
        System.out.println("[0].Go_back_to_main_menu [99].Exit_system");
    }

    public static void showSubMenu() {
        printSubMenu();
        int codes[] = { 0, 99 };
        int cmd = getCmd(codes);
        if (cmd == 99)
            System.exit(0);
    }

    public static void printCmdErrMsg() {
        System.out.println("Error_wrong_command\nPlease_enter_again:");
    }

    public static boolean isValidCmd(int codes[], int cmd) {
        for (int c : codes) {
            if (c == cmd)
                return true;
        }
        return false;
    }

    public static int getCmd(int codes[]) {
        int cmd;
        while (true) {
            try {
                cmd = scanner.nextInt();
                if (scanner.hasNextLine())
                    scanner.nextLine(); // clear new line character
                if (isValidCmd(codes, cmd))
                    break;
            } catch (Exception e) {
                scanner.nextLine();
            }
            printCmdErrMsg();
        }
        return cmd;
    }

    public static void printOptions(String options[]) {
        for (int i = 1; i <= options.length; i++) {
            if (i != 1)
                System.out.print(" ");
            System.out.printf("[%d].%s", i, options[i - 1]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int cmd = -1;
        boolean running = true;
        boolean cmdErr = false;

        login();
        while (running) {
            if (!cmdErr) {
                printMenu();
            }
            try {
                cmd = scanner.nextInt();
                cmdErr = false;
            } catch (Exception e) { // if input not int
                scanner.nextLine(); // clear buffer
                cmd = -1;
            }
            switch (cmd) {
                case 1:
                    dataMgr.showAll();
                    showSubMenu();
                    break;
                case 8:
                    dataMgr.addCatalog();
                    showSubMenu();
                    break;
                case 9:
                    dataMgr.showCatalog();
                    showSubMenu();
                    break;
                case 14:
                    dataMgr.showRawData();
                    showSubMenu();
                    break;
                case 99:
                    running = false;
                    break;
                default:
                    cmdErr = true;
                    printCmdErrMsg();
                    break;
            }
        }
    }
}
