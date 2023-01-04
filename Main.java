import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final AccountMgr accMgr = new AccountMgr();
    private static final DataMgr dataMgr = new DataMgr();
    public static final Scanner scanner = new Scanner(System.in);

    private static final String FIELDS[] = { "ID", "Name", "Start", "End", "Degree",
            "State", "Number", "Work" };
    private static final String CMD_ERR_MSG = "Error_wrong_command\nPlease_enter_again:";
    private static final String DATA_ERR_MSG = "Error_wrong_data\nPlease_input_again:";

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
        int cmd = getCmd(codes, CMD_ERR_MSG);
        if (cmd == 99)
            System.exit(0);
    }

    public static boolean isValidCmd(int codes[], int cmd) {
        for (int c : codes) {
            if (c == cmd)
                return true;
        }
        return false;
    }

    public static int getCmd(int codes[], String errMsg) {
        int cmd;
        while (true) {
            try {
                cmd = Integer.parseInt(scanner.nextLine());
                if (isValidCmd(codes, cmd))
                    break;
            } catch (Exception e) {}
            System.out.println(errMsg);
        }
        return cmd;
    }

    public static void showPerPage() {
        int page = 5;
        String cmd;
        System.out.println("Choose_show_per_page:");
        System.out.println("[3].3_data_per_page [5].5_data_per_page [10].10_data_per_page");
        System.out.print("[d].default ");
        printSubMenu();
        while (true) {
            cmd = scanner.nextLine();
            if (cmd.equals("0") || cmd.equals("3") || cmd.equals("5") || cmd.equals("10") ||
                    cmd.equals("d") || cmd.equals("99"))
                break;
        }
        if (cmd.equals("0")) {
            return;
        } else if (cmd.equals("3")) {
            page = 3;
        } else if (cmd.equals("5")) {
            page = 5;
        } else if (cmd.equals("10")) {
            page = 10;
        } else if (cmd.equals("d")) {
            page = Integer.parseInt(DataMgr.config.getShowPerPage());
        } else {
            System.exit(0);
        }
        dataMgr.showPerPage(page);
    }

    public static void printOptions(String options[]) {
        for (int i = 1; i <= options.length; i++) {
            if (i != 1)
                System.out.print(" ");
            System.out.printf("[%d].%s", i, options[i - 1]);
        }
        System.out.println();
    }

    public static void showByCatalog() {
        int cmd;
        String catalog;
        String options[] = dataMgr.catalogs.toArray(new String[dataMgr.catalogs.size()]);
        int codes[] = new int[options.length + 2];
        codes[0] = 0;
        codes[1] = 99;
        for (int i = 1; i <= options.length; i++)
            codes[i + 1] = i;

        System.out.println("Catalogs:");
        printOptions(options);
        printSubMenu();
        System.out.println("Input_catalog_to_show:");
        while (true) {
            cmd = getCmd(codes, DATA_ERR_MSG);
            if (cmd == 0) {
                return;
            } else if (cmd == 99) {
                System.exit(0);
            } else {
                catalog = options[cmd - 1];
                break;
            }
        }
        dataMgr.showJobs(dataMgr.searchJob(8, catalog));
    }

    public static void search() {
        int codes[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 99 };
        boolean running = true;
        String value;
        while (running) {
            System.out.println("Search by:");
            printOptions(FIELDS);
            printSubMenu();
            int field = getCmd(codes, CMD_ERR_MSG);
            if (field == 0)
                return;
            else if (field == 99)
                System.exit(0);
            System.out.println("Input_target:");
            ArrayList<Job> result = new ArrayList<>();

            while (true) {
                value = scanner.nextLine();
                if (!dataMgr.checkData(field, value))
                    System.out.println(DATA_ERR_MSG);
                else
                    break;
            }

            result = dataMgr.searchJob(field, value);
            if (result.size() == 0) {
                System.out.println("Error_no_result");
            } else {
                System.out.println("Search_result:");
                dataMgr.showJobs(result);
            }

            while (true) {
                System.out.print("[1].Restart_search ");
                printSubMenu();
                int cmd = getCmd(new int[] { 0, 1, 99 }, CMD_ERR_MSG);
                if (cmd == 0)
                    return;
                else if (cmd == 99)
                    System.exit(0);
                else if (cmd == 1)
                    break;
            }
        }
    }

    public static void modifyJob() {
        System.out.println("Input_ID_to_be_modified:");
        String id = scanner.nextLine();
        ArrayList<Job> result = dataMgr.searchJob(1, id);
        Job oldJob = result.get(0);
        System.out.println("Search_result:");
        dataMgr.showJobs(result);
        String name, start, end, degree, state, number, catalog, work;
        System.out.println("New_name:");
        name = scanner.nextLine();
        if (name.equals(""))
            name = oldJob.name;
        System.out.println("New_start:");
        start = scanner.nextLine();
        if (start.equals(""))
            start = oldJob.start;
        System.out.println("New_end:");
        end = scanner.nextLine();
        if (end.equals(""))
            end = oldJob.end;
        System.out.println("New_degree:");
        degree = scanner.nextLine();
        if (degree.equals(""))
            degree = oldJob.degree;
        System.out.println("New_state:");
        state = scanner.nextLine();
        if (state.equals(""))
            state = oldJob.state;
        System.out.println("New_number:");
        number = scanner.nextLine();
        if (number.equals(""))
            number = oldJob.number;
        System.out.print("Catalogs:");
        String options[] = dataMgr.catalogs.toArray(new String[dataMgr.catalogs.size()]);
        printOptions(options);
        System.out.println("New_catalog:");
        String cmd = scanner.nextLine();
        if (cmd.equals(""))
            catalog = oldJob.catalog;
        else
            catalog = options[Integer.parseInt(cmd) - 1];
        System.out.println("New_work:");
        work = scanner.nextLine();
        if (work.equals(""))
            work = oldJob.work;
        Job newJob = new Job(Integer.parseInt(id), name, start, end, degree,
                state, number, catalog, work);
        dataMgr.modifyJob(oldJob, newJob);
        System.out.println("Modify_data_success");
    }

    public static void deleteJob() {
        String id;
        System.out.println("Input_ID_to_be_deleted:");
        while (true) {
            id = scanner.nextLine();
            if (dataMgr.idRegex(id))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        dataMgr.deleteJob(id);
    }

    public static void addJob() {
        String name, start, end, degree, state, number, work;
        System.out.println("Name:");
        while (true) {
            name = scanner.nextLine();
            if (dataMgr.nameRegex(name))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        System.out.println("Start:");
        while (true) {
            start = scanner.nextLine();
            if (dataMgr.timeRegex(start))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        System.out.println("End:");
        while (true) {
            end = scanner.nextLine();
            if (dataMgr.timeRegex(end))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        System.out.println("Degree:");
        while (true) {
            degree = scanner.nextLine();
            if (dataMgr.degreeRegex(degree))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        System.out.println("State:");
        while (true) {
            state = scanner.nextLine();
            if (dataMgr.stateRegex(state))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        System.out.println("Number:");
        while (true) {
            number = scanner.nextLine();
            if (dataMgr.numberRegex(number))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        System.out.print("Catalogs:");
        String options[] = dataMgr.catalogs.toArray(new String[dataMgr.catalogs.size()]);
        int codes[] = new int[options.length + 2];
        codes[0] = 0;
        codes[1] = 99;
        for (int i = 1; i <= options.length; i++)
            codes[i + 1] = i;
        printOptions(options);
        System.out.println("Catalog:");
        int cmd = getCmd(codes, DATA_ERR_MSG);
        String catalog = options[cmd - 1];
        System.out.println("Work:");
        while (true) {
            work = scanner.nextLine();
            if (dataMgr.workRegex(work))
                break;
            else
                System.out.println(DATA_ERR_MSG);
        }
        dataMgr.addJob(name, start, end, degree, state, number, catalog, work);
        System.out.println("Add_contact_success");
    }

    public static void setShowField() {
        int codes[] = { 0, 1 };
        String errMsg = "Input_error_plaese_input_0_or_1:";
        String newFields[] = new String[8];
        String fields[] = DataMgr.config.displayOptions;
        printOptions(DataMgr.config.getShowField());
        for (int i = 0; i < 8; i++) {
            System.out.println("New_" + fields[i] + "(0/1):");
            while (true) {
                int cmd = getCmd(codes, errMsg);
                if (cmd == 1)
                    newFields[i] = "true";
                else
                    newFields[i] = "false";
                break;
            }
        }
        DataMgr.config.setShowField(newFields);
        printOptions(DataMgr.config.getShowField());
    }

    public static void setShowPerPage() {
        int page;
        System.out.println("show_defalt_perpage:" + DataMgr.config.getShowPerPage());
        System.out.println("new_show_defalt_perpage:");
        while (true) {
            try {
                page = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(DATA_ERR_MSG);
            }
        }
        DataMgr.config.setShowPerPage(page);
        System.out.println("show_defalt_perpage:" + DataMgr.config.getShowPerPage());
    }

    public static void setShowOrder() {
        String order;
        System.out.println("show_sort_order:" + DataMgr.config.getSortOrder());
        System.out.println("Please_input_new_sort_order:");
        while (true) {
            order = scanner.nextLine();
            if (order.equals("asc") || order.equals("des")) {
                DataMgr.config.setSortOrder(order);
                break;
            }
            System.out.println("Input_error_plaese_input_asc_or_des:");
        }
        System.out.println("show_sort_order:" + DataMgr.config.getSortOrder());
    }

    public static void setSortField() {
        String fields[] = { "ID", "Name", "Start", "End", "Degree",
                "State", "Number", "Catalog", "Work" };
        printOptions(fields);
        printSubMenu();
        int cmd = getCmd(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 99 }, CMD_ERR_MSG);
        DataMgr.config.setSortField(fields[cmd - 1].toLowerCase());
        System.out.println("Sorted_by:" + fields[cmd - 1]);
    }

    public static void optimizeData() {
        System.out.println("Please_confirm_data_optimize_y_or_n:");
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("y")) {
                dataMgr.optimizeData();
                System.out.println("Data_optimize_success");
                break;
            } else if (s.equals("n")) {
                System.out.println("Data_optimize_denied");
                break;
            }
        }
    }

    public static void addAccount() {
        String acc, pw;
        System.out.println("New_account:");
        acc = scanner.nextLine();
        System.out.println("New_password:");
        pw = scanner.nextLine();
        accMgr.addAccount(acc, pw);
    }

    public static void removeAccount() {
        String acc;
        System.out.println("Delete_account:");
        while (true) {
            acc = scanner.nextLine();
            if (accMgr.removeAccount(acc))
                break;
            System.out.println("No_account_please_try_again:");
        }
        System.out.println("Delete_account_success");
    }

    public static void modifyAccount() {
        String oldAcc, newAcc, newPw;
        System.out.println("Modify_account:");
        oldAcc = scanner.nextLine();
        System.out.println("New_account:");
        newAcc = scanner.nextLine();
        System.out.println("New_password:");
        newPw = scanner.nextLine();
        accMgr.modifyAccount(oldAcc, newAcc, newPw);
        System.out.println("Modify_account_success");
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
                cmd = Integer.parseInt(scanner.nextLine());
                cmdErr = false;
            } catch (Exception e) { // if input not int
                cmd = -1;
            }
            switch (cmd) {
                case 1:
                    dataMgr.showAll();
                    showSubMenu();
                    break;
                case 2:
                    showPerPage();
                    break;
                case 3:
                    showByCatalog();
                    showSubMenu();
                    break;
                case 4:
                    search();
                    break;
                case 5:
                    modifyJob();
                    showSubMenu();
                    break;
                case 6:
                    deleteJob();
                    showSubMenu();
                    break;
                case 7:
                    addJob();
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
                case 10:
                    setShowField();
                    showSubMenu();
                    break;
                case 11:
                    setShowPerPage();
                    showSubMenu();
                    break;
                case 12:
                    setShowOrder();
                    showSubMenu();
                    break;
                case 13:
                    setSortField();
                    showSubMenu();
                    break;
                case 14:
                    dataMgr.showRawData();
                    showSubMenu();
                    break;
                case 15:
                    optimizeData();
                    showSubMenu();
                    break;
                case 16:
                    accMgr.showAccount();
                    showSubMenu();
                    break;
                case 17:
                    addAccount();
                    showSubMenu();
                    break;
                case 18:
                    removeAccount();
                    showSubMenu();
                    break;
                case 19:
                    modifyAccount();
                    showSubMenu();
                    break;
                case 99:
                    running = false;
                    break;
                default:
                    cmdErr = true;
                    System.out.println(CMD_ERR_MSG);
                    break;
            }
        }
    }
}
