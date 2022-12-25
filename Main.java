import java.util.Scanner;

public class Main {
    private static final AccountMgr accMgr = new AccountMgr();
    private static final DataMgr dataMgr = new DataMgr();
    public static final Scanner scanner = new Scanner(System.in);

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
                    break;
                case 99:
                    running = false;
                    break;
                default:
                    cmdErr = true;
                    System.out.println("Error_wrong_command\nPlease_enter_again:");
                    break;
            }
        }
    }
}