import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataManager {
    ArrayList<People> peoples = new ArrayList<>();
    ArrayList<Catalog> catalogs = new ArrayList<>();
    public Config config;

    public void load() {
        loadPeoples();
        loadCatalogs();
    }

    public void printAllPeople() {
        boolean show_name = config.getBoolean("show_name");
        boolean show_phone = config.getBoolean("show_phone");
        boolean show_catalog = config.getBoolean("show_catalog");
        boolean show_email = config.getBoolean("show_email");
        boolean show_birthday = config.getBoolean("show_birthday");

        System.out.println(getPeopleTitle(show_name, show_phone, show_catalog, show_email, show_birthday));
        for(People people: getSortedList())
            people.print(show_name, show_phone, show_catalog, show_email, show_birthday);
    }
    
    public void printByID(int id) {
        boolean show_name = config.getBoolean("show_name");
        boolean show_phone = config.getBoolean("show_phone");
        boolean show_catalog = config.getBoolean("show_catalog");
        boolean show_email = config.getBoolean("show_email");
        boolean show_birthday = config.getBoolean("show_birthday");

        System.out.println(getPeopleTitle(show_name, show_phone, show_catalog, show_email, show_birthday));
        for(People people: getSortedList())
            if(people.id == id) 
                people.print(show_name, show_phone, show_catalog, show_email, show_birthday);
    }

    public void printByCatalog(String catalog) {
        boolean show_name = config.getBoolean("show_name");
        boolean show_phone = config.getBoolean("show_phone");
        boolean show_catalog = config.getBoolean("show_catalog");
        boolean show_email = config.getBoolean("show_email");
        boolean show_birthday = config.getBoolean("show_birthday");
        System.out.println(getPeopleTitle(show_name, show_phone, show_catalog, show_email, show_birthday));
        for(People people: peoples)
            if(people.catalog.equals(catalog))
                people.print(show_name, show_phone, show_catalog, show_email, show_birthday);
    }

    public void printRawData() {
        boolean show_name = config.getBoolean("show_name");
        boolean show_phone = config.getBoolean("show_phone");
        boolean show_catalog = config.getBoolean("show_catalog");
        boolean show_email = config.getBoolean("show_email");
        boolean show_birthday = config.getBoolean("show_birthday");

        System.out.println(getPeopleTitle(show_name, show_phone, show_catalog, show_email, show_birthday));
        for(People people: peoples) {
            people.print(show_name, show_phone, show_catalog, show_email, show_birthday);
        }

    }

    public void printRawData15() {
        System.out.println("[ID] [Name] [Phone] [Catalog] [Email] [BD]");
        for(People p: peoples)
            System.out.println(Control.formatNumber(p.id, 4) + p.name + " " + p.phone + " " + p.catalog+ " " + p.email + " " + p.birthday);

    }

    public void printPerPage(int page) {
        boolean show_name = config.getBoolean("show_name");
        boolean show_phone = config.getBoolean("show_phone");
        boolean show_catalog = config.getBoolean("show_catalog");
        boolean show_email = config.getBoolean("show_email");
        boolean show_birthday = config.getBoolean("show_birthday");
        int temp = page;
        int i = temp - page;
        boolean control = true;
        int count = 1;
        String input;
        int size = 0;
        if(peoples.size() % page != 0)
            size = (peoples.size() / page) + 1;
        else
            size = peoples.size() / page;
        while(true) {
            if(count < size)
                for(; i < temp; i++) 
                    getSortedList().get(i).print(show_name, show_phone, show_catalog, show_email, show_birthday);
            else if(count == size || temp > peoples.size())
                for(; i < peoples.size(); i++)
                    getSortedList().get(i).print(show_name, show_phone, show_catalog, show_email, show_birthday);

            if(count == 1 && count != size) {
                control = true;
                System.out.println("[2].Next_page [0].Go_back_to_main_menu [99].Exit_system");
                while(control) {
                    input = Main.scanner.nextLine();
                    switch(input) {
                        case "2":
                            temp += page;
                            i = temp - page;
                            count++;
                            control = false;
                            break;
                        case "0":
                            return;
                        case "99":
                            System.exit(0);
                        default:
                            System.out.println("Error_wrong_command");
                            System.out.println("Please_enter_again:");
                            break;
                    }
                }
                continue;
            }

            if(count > 1 && count < size) {
                control = true;
                System.out.println("[1].Last_page [2].Next_page [0].Go_back_to_main_menu [99].Exit_system");
                while(control) {
                    input = Main.scanner.nextLine();
                    switch(input) {
                        case "1":
                            temp -= page;
                            i = temp - page;
                            count--;
                            control = false;
                            break;
                        case "2":
                            temp += page;
                            i = temp - page;
                            count++;
                            control = false;
                            break;
                        case "0":
                            return;
                        case "99":
                            System.exit(0);
                        default:
                            System.out.println("Error_wrong_command");
                            System.out.println("Please_enter_again:");
                            break;
                    } 
                }
                continue;
            }

            if(count == size && count != 1) {
                control = true;
                System.out.println("[1].Last_page [0].Go_back_to_main_menu [99].Exit_system");
                while(control) {
                    input = Main.scanner.nextLine();
                    switch(input) {
                        case "1":
                            temp -= page;
                            i = temp - page;
                            count--;
                            control = false;
                            break;
                        case "0":
                            return;
                        case "99":
                            System.exit(0);
                        default:
                            System.out.println("Error_wrong_command");
                            System.out.println("Please_enter_again:");
                            break;
                    }
                }
                continue;
            }

            if(count == size && count == 1) {
                control = true;
                System.out.println("[0].Go_back_to_main_menu [99].Exit_system");
                
                while(control) {
                    input = Main.scanner.nextLine();
                    switch(input) {
                        case "0":
                            return;
                        case "99":
                            System.exit(0);
                        default:
                            System.out.println("Error_wrong_command");
                            System.out.println("Please_enter_again:");
                            break;
                    }
                }
            }

        }
    }

    public ArrayList<People> getSortedList() {
        ArrayList<People> out = new ArrayList<>(peoples);
        Collections.sort(out, new Comparator<People>() {
            @Override
            public int compare(People o1, People o2) {
                String orderby = config.getString("show_sort_field"); //feild
                boolean asc = config.getString("show_sort_order").equalsIgnoreCase("asc");

                if(orderby.equalsIgnoreCase("id")) {
                    if(asc)
                        return o1.id - o2.id;
                    else
                        return o2.id - o1.id;
                }

                if (orderby.equalsIgnoreCase("name")) {
                    if (asc) {
                        return o1.name.compareTo(o2.name);
                    } else {
                        return o2.name.compareTo(o1.name);
                    }
                }

                if (orderby.equalsIgnoreCase("phone")) {
                    if (asc) {
                        return o1.phone.compareTo(o2.phone);
                    } else {
                        return o2.phone.compareTo(o1.phone);
                    }
                }

                if (orderby.equalsIgnoreCase("catalog")) {
                    if (asc) {
                        return o1.catalog.compareTo(o2.catalog);
                    } else {
                        return o2.catalog.compareTo(o1.catalog);
                    }
                }

                if (orderby.equalsIgnoreCase("email")) {
                    if (asc) {
                        return o1.email.compareTo(o2.email);
                    } else {
                        return o2.email.compareTo(o1.email);
                    }
                }

                if (orderby.equalsIgnoreCase("birthday")) {
                    if (asc) {
                        return o1.birthday.compareTo(o2.birthday);
                    } else {
                        return o2.birthday.compareTo(o1.birthday);
                    }
                }
                return 0;
            }
        });
        return out;
    }

    public String getPeopleTitle(boolean show_name, boolean show_phone, boolean show_catalog, boolean show_email, boolean show_birthday) {
        String title = Control.formatString("[ID]", 4);
        if(show_name)
            title += Control.formatString("[Name]", 12);
        if(show_phone)
            title += Control.formatString("[Phone]", 11);
        if(show_catalog)
            title += Control.formatString("[Catalog]", 12);
        if(show_email)
            title += Control.formatString("[Email]", 24);
        if(show_birthday)
            title += "[BD]";
        return title;
    }
    
    public void loadCatalogs() {
        try {
            Scanner sc = new Scanner(new File("catalog.txt"));
            while(sc.hasNext()) {
                String line = sc.nextLine();
                catalogs.add(new Catalog(line));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //after 15 use down
    public void loadPeoples() {
        try {
            /*Scanner sc = new Scanner(new File("data.txt"));
            while(sc.hasNext()) {
                String line = sc.nextLine();
                peoples.add(new People(line));*/
            Scanner read = new Scanner(new File("data.txt"));
            while(read.hasNextLine()){
                String d[] = new String[6];
                String rf = read.nextLine();
                StringTokenizer st = new StringTokenizer(rf);
                int id = Integer.parseInt(st.nextToken());
                d[1] = st.nextToken();
                d[2] = st.nextToken();
                d[3] = st.nextToken();
                d[4] = st.nextToken();
                d[5] = st.nextToken();
                peoples.add(new People(id,d[1],d[2],d[3],d[4],d[5]));
            } 
            read.close();//sc
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String selectCatalog(boolean back, boolean n) {
        char i = 'a';
        System.out.print("Catalogs:");
        String o = "";
        for(Catalog catalog: catalogs) {
            o += ("[" + i + "]." + catalog.name + " ");
            i++;
        }
        o = o.trim();
        System.out.print(o + "\n");

        if(back) {
            System.out.println("[0].Main_menu [99].Exit_system");
            int input = Integer.parseInt(Main.scanner.nextLine());
            if(input == 0)
                return null;
            if(input == 99)
                System.exit(0);
        }

        if(n){
            System.out.println("New_catalog:");
        }
        else
            System.out.println("Catalog:");

        try {
            String input = Main.scanner.nextLine();
            char tran = input.charAt(0);
            while(tran < 'a' || tran > i - 1) {
                System.out.println("Error_wrong_data\n" + "Please_input_again:");
                String temp = Main.scanner.nextLine();
                tran = temp.charAt(0);
            }
            int out = (int) tran - 97;
            return catalogs.get(out).name;
        } catch (Exception e) {
            return "";
        }
    }

    public String selectCatalogToShow() {
        char i = 'a';
        String o = "";
        String reg = "[a-zA-Z]+";
        int out = 0;
        System.out.println("Catalogs:");
        for(Catalog catalog: catalogs) {
            o += ("[" + i + "]." + catalog.name + " ");
            i++;
        }
        o = o.trim();
        System.out.println(o);
        System.out.println("[0].Go_back_to_main_menu [99].Exit_system");
        System.out.println("Input_catalog_to_show:");
        try {
            String input = Main.scanner.nextLine();
            char tran = input.charAt(0);
            if(!(input.matches(reg)) && Integer.parseInt(input) == 0)
                return null;
            else if(!(input.matches(reg)) && Integer.parseInt(input) == 99)
                System.exit(0);
            while(tran < 'a' || tran > i - 1) {
                System.out.println("Error_wrong_data\n" + "Please_input_again:");
                String temp = Main.scanner.nextLine();
                tran = temp.charAt(0);
            }
            out = (int) tran - 97;
            return catalogs.get(out).name;
        }catch (Exception e) {
            return "";
        }
    }

    public String[] inputPeople(boolean n) {
        String[] cols = new String[] {(n ? "New_name" : "Name"), (n ? "New_phone" : "Phone_number")};
        ArrayList<String> datas = new ArrayList<>();
        String[] data1 = getInput(cols);
        for(String a: data1)
            datas.add(a);
        String catalogName = selectCatalog(false, n);
        String[] cols2 = new String[] {(n ? "New_email" : "Email"), (n ? "New_birthday" : "Birthday")};
        String[] data2 = getInput(cols2);
        datas.add(catalogName);
        for(String a: data2)
            datas.add(a);
        return datas.toArray(new String[datas.size()]);
    }


    public String[] getInput(String[] cols, String[] can, String error) {
        String[] inputs = new String[cols.length];
        out:
        for(int i = 0; i < cols.length; i++) {
            String col = cols[i];
            System.out.println(col + ":");
            inputs[i] = Main.scanner.nextLine();
            switch(col) {
                case "New_name":case "Name":
                    while(true) {
                        if(inputs[i].isEmpty() ||nameRegex(inputs[i])) 
                            break;
                        else {
                            System.out.println("Error_wrong_data\n" + "Please_input_again:");
                            inputs[i] = Main.scanner.nextLine();
                        }
                    }
                    break;
                case "New_phone":case "Phone_number":
                    while(true) {
                        if(inputs[i].isEmpty() ||phoneRegex(inputs[i])) 
                            break;
                        else {
                            System.out.println("Error_wrong_data\n" + "Please_input_again:");
                            inputs[i] = Main.scanner.nextLine();
                        }
                    }
                    break;
                case "New_email":case "Email":
                    while(true) {
                        if(inputs[i].isEmpty() ||emailRegex(inputs[i])) 
                            break;
                        else {
                            System.out.println("Error_wrong_data\n" + "Please_input_again:");
                            inputs[i] = Main.scanner.nextLine();
                        }
                    }
                    break;
                case "New_birthday":case "Birthday":
                    while(true) {
                        if(inputs[i].isEmpty() ||birthdayRegex(inputs[i])) 
                            break;
                        else {
                            System.out.println("Error_wrong_data\n" + "Please_input_again:");
                            inputs[i] = Main.scanner.nextLine();
                        }
                    }
                    break;
                default:
                    break;
            }
            if(can != null) {
                for(String c: can)
                    if(c.equals(inputs[i]))
                        continue out;
                System.out.println(error);
                i--;
            } 
        }
        return inputs;
    }

    public String[] getInput(String[] cols) {
        return getInput(cols, null, null);
    }

    public boolean addCat(String adc) {
        if(adc.length() > 12)
            return false;   
        for(Catalog c: catalogs) {
            if(adc.equals(c.name))
                return false;
        }
        return true;
    }

    public void saveCatalog() {
        String out = "";
        for(Catalog catalog: catalogs)
            out += catalog.name + "\n";
        out = out.trim();
        try {
            FileWriter fw = new FileWriter(new File("catalog.txt"));
            fw.write(out);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePeople() {
        String out = "";
        for(People people: peoples) 
            out += Control.formatNumber(people.id, 4) + people.name + " " + people.phone + " " + people.catalog + " " + people.email + " " + people.birthday + "\n";
        out = out.trim();
        try {
            FileWriter fw = new FileWriter(new File("data.txt"));
            fw.write(out);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean nameRegex(String name) {
        String pattern = "^[0-9]*$";
        return !(name.matches(pattern) || name == null || name.length() > 12);
    }

    public boolean birthdayRegex(String birthday) {
        int month = Integer.parseInt(birthday.substring(0, 2));
        int day = Integer.parseInt(birthday.substring(2));
        boolean flag = true;
        switch(month) {
            case 1:case 3:case 5:case 7:case 8:case 10:case 12:
                if(day < 1 || day > 31)
                    flag = false;
                break;
            case 2:
                if(day < 1 || day > 28)
                    flag = false;
                break;
            case 4:case 6:case 9:case 11:
                if(day < 1 || day > 30)
                    flag = false;
                break;
            default:
                flag = false;
                break;
        }
        return (flag && birthday != null && birthday.length() < 5);
    }

    public boolean phoneRegex(String phone) {
        String pattern = "[0-9]{4}-[0-9]{6}";
        return (phone.matches(pattern) && phone.length() < 12);
    }

    public boolean emailRegex(String email) {
        String pattern = ".+@.+\\..+";
        return (email.matches(pattern) && email.length() < 25);
    }
}
