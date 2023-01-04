import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataMgr {
    public static Config config = new Config();
    public static ArrayList<Job> jobs = new ArrayList<>();
    public ArrayList<String> catalogs = new ArrayList<>();

    public DataMgr() {
        loadCatalogs();
        loadJobs();
    }

    public void showJobs(ArrayList<Job> data) {
        boolean name = config.getBoolean("show_name");
        boolean start = config.getBoolean("show_start");
        boolean end = config.getBoolean("show_end");
        boolean degree = config.getBoolean("show_degree");
        boolean state = config.getBoolean("show_state");
        boolean number = config.getBoolean("show_number");
        boolean catalog = config.getBoolean("show_catalog");
        boolean work = config.getBoolean("show_work");

        System.out.println(getJobTitle(name, number, catalog, state, start, work, degree, end));
        for (Job job : data) {
            job.print(name, start, end, degree, state, number, catalog, work);
        }
    }

    public void showPerPage(int page) {
        boolean name = config.getBoolean("show_name");
        boolean start = config.getBoolean("show_start");
        boolean end = config.getBoolean("show_end");
        boolean degree = config.getBoolean("show_degree");
        boolean state = config.getBoolean("show_state");
        boolean number = config.getBoolean("show_number");
        boolean catalog = config.getBoolean("show_catalog");
        boolean work = config.getBoolean("show_work");
        int temp = page;
        int i = temp - page;
        int count = 1;
        int size = 0; // amount of pages
        String input;
        ArrayList<Job> data = getSortedList();
        if (jobs.size() % page != 0)
            size = (jobs.size() / page) + 1;
        else
            size = jobs.size() / page;
        while (true) {
            if (count < size)
                for (; i < temp; i++)
                    data.get(i).print(name, start, end, degree, state, number, catalog, work);
            else if (count == size || temp > jobs.size())
                for (; i < jobs.size(); i++)
                    data.get(i).print(name, start, end, degree, state, number, catalog, work);

            if (count == 1 && count != size) {
                System.out.println("[2].Next_page [0].Go_back_to_main_menu [99].Exit_system");
                while (true) {
                    input = Main.scanner.nextLine();
                    if (input.equals("2")) {
                        temp += page;
                        i = temp - page;
                        count++;
                        break;
                    } else if (input.equals("0")) {
                        return;
                    } else if (input.equals("99")) {
                        System.exit(0);
                    } else {
                        System.out.println("Error_wrong_command\nPlease_enter_again:");
                    }
                }
                continue;
            }

            if (count > 1 && count < size) {
                System.out.println("[1].Last_page [2].Next_page [0].Go_back_to_main_menu [99].Exit_system");
                while (true) {
                    input = Main.scanner.nextLine();
                    if (input.equals("1")) {
                        temp -= page;
                        i = temp - page;
                        count--;
                        break;
                    } else if (input.equals("2")) {
                        temp += page;
                        i = temp - page;
                        count++;
                        break;
                    } else if (input.equals("0")) {
                        return;
                    } else if (input.equals("99")) {
                        System.exit(0);
                    } else {
                        System.out.println("Error_wrong_command\nPlease_enter_again:");
                    }
                }
                continue;
            }

            if (count == size && count != 1) {
                System.out.println("[1].Last_page [0].Go_back_to_main_menu [99].Exit_system");
                while (true) {
                    input = Main.scanner.nextLine();
                    if (input.equals("1")) {
                        temp -= page;
                        i = temp - page;
                        count--;
                        break;
                    } else if (input.equals("0")) {
                        return;
                    } else if (input.equals("99")) {
                        System.exit(0);
                    } else {
                        System.out.println("Error_wrong_command\nPlease_enter_again:");
                    }
                }
                continue;
            }

            if (count == size && count == 1) {
                System.out.println("[0].Go_back_to_main_menu [99].Exit_system");
                while (true) {
                    input = Main.scanner.nextLine();
                    if (input.equals("0")) {
                        return;
                    } else if (input.equals("99")) {
                        System.exit(0);
                    } else {
                        System.out.println("Error_wrong_command\nPlease_enter_again:");
                    }
                }
            }

        }
    }

    public void showAll() {
        showJobs(getSortedList());
    }

    public void showRawData() {
        System.out.println("[ID] [Name]       [Start]  [End]    [Degree] [State]      [Number] [Catalog]    [Work]");
        for (Job job : jobs) {
            job.print(true, true, true, true, true, true, true, true);
        }
    }

    public String getJobTitle(boolean name, boolean number, boolean catalog,
            boolean state, boolean start, boolean work, boolean degree, boolean end) {
        String title = String.format("%-4s", "[ID]");
        if (name)
            title += " " + String.format("%-12s", "[Name]");
        if (start)
            title += " " + String.format("%-8s", "[Start]");
        if (end)
            title += " " + String.format("%-8s", "[End]");
        if (degree)
            title += " " + String.format("%-8s", "[Degree]");
        if (state)
            title += " " + String.format("%-12s", "[State]");
        if (number)
            title += " " + String.format("%-8s", "[Number]");
        if (catalog)
            title += " " + String.format("%-12s", "[Catalog]");
        if (work)
            title += " [Work]";
        return title;
    }

    public ArrayList<Job> getSortedList() {
        ArrayList<Job> result = new ArrayList<>(jobs);
        Collections.sort(result, new Comparator<Job>() {
            @Override
            public int compare(Job o1, Job o2) {
                String orderby = config.getString("show_sort_field");
                boolean asc = config.getString("show_sort_order").equals("asc");

                // comparator return value:
                // positive -> o1 > o2
                // zero -> o1 == o2
                // negative -> o1 < o2
                if (orderby.equalsIgnoreCase("id")) {
                    if (asc)
                        return o1.id - o2.id;
                    else
                        return o2.id - o1.id;
                }

                if (orderby.equalsIgnoreCase("name")) {
                    if (asc)
                        return o1.name.compareTo(o2.name);
                    else
                        return o2.name.compareTo(o1.name);
                }

                if (orderby.equalsIgnoreCase("start")) {
                    if (asc)
                        return o1.start.compareTo(o2.start);
                    else
                        return o2.start.compareTo(o1.start);
                }

                if (orderby.equalsIgnoreCase("end")) {
                    if (asc)
                        return o1.end.compareTo(o2.end);
                    else
                        return o2.end.compareTo(o1.end);
                }

                if (orderby.equalsIgnoreCase("degree")) {
                    if (asc)
                        return o1.degree.compareTo(o2.degree);
                    else
                        return o2.degree.compareTo(o1.degree);
                }

                if (orderby.equalsIgnoreCase("state")) {
                    if (asc)
                        return o1.state.compareTo(o2.state);
                    else
                        return o2.state.compareTo(o1.state);
                }

                if (orderby.equalsIgnoreCase("number")) {
                    if (asc)
                        return o1.number.compareTo(o2.number);
                    else
                        return o2.number.compareTo(o1.number);
                }

                if (orderby.equalsIgnoreCase("catalog")) {
                    if (asc)
                        return o1.catalog.compareTo(o2.catalog);
                    else
                        return o2.catalog.compareTo(o1.catalog);
                }

                if (orderby.equalsIgnoreCase("work")) {
                    if (asc)
                        return o1.work.compareTo(o2.work);
                    else
                        return o2.work.compareTo(o1.work);
                }
                return 0;
            }
        });
        return result;
    }

    public void loadJobs() {
        try {
            Scanner read = new Scanner(new File("data.txt"));
            while (read.hasNextLine()) {
                String tokens[] = new String[8];
                String line = read.nextLine();
                if (line.length() != 0) {
                    StringTokenizer st = new StringTokenizer(line);
                    int id = Integer.parseInt(st.nextToken());
                    tokens[0] = st.nextToken(); // name
                    tokens[1] = st.nextToken(); // start
                    tokens[2] = st.nextToken(); // end
                    tokens[3] = st.nextToken(); // degree
                    tokens[4] = st.nextToken(); // state
                    tokens[5] = st.nextToken(); // number
                    tokens[6] = st.nextToken(); // catalog
                    tokens[7] = st.nextToken(); // work
                    jobs.add(new Job(id, tokens[0], tokens[1], tokens[2], tokens[3],
                            tokens[4], tokens[5], tokens[6], tokens[7]));
                }
            }
            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadCatalogs() {
        try {
            Scanner read = new Scanner(new File("catalog.txt"));
            while (read.hasNextLine()) {
                String line = read.nextLine();
                if (line.length() != 0)
                    catalogs.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addJob(String name, String start, String end, String degree,
            String state, String number, String catalog, String work) {
        int id = config.nextId();
        jobs.add(new Job(id, name, start, end, degree, state, number, catalog, work));
        saveJobs();
    }

    public void modifyJob(Job oldJob, Job newJob) {
        jobs.remove(oldJob);
        jobs.add(newJob);
        saveJobs();
    }

    public void deleteJob(String id) {
        ArrayList<Job> result = searchJob(1, id);
        if (result.size() == 0) {
            System.out.println("Error_no_such_data");
        } else {
            jobs.removeAll(result);
            System.out.println("Delete_data_success");
        }
        saveJobs();
    }

    public void saveJobs() {
        String result = "";
        for (Job job : jobs) {
            result += String.format("%04d", job.id) + " " +
                    String.format("%-12s", job.name) + " " +
                    String.format("%-8s", job.start) + " " +
                    String.format("%-8s", job.end) + " " +
                    String.format("%-8s", job.degree) + " " +
                    String.format("%-12s", job.state) + " " +
                    String.format("%-8s", job.number) + " " +
                    String.format("%-12s", job.catalog) + " " +
                    job.work + "\n";
        }
        try {
            FileWriter fw = new FileWriter(new File("data.txt"));
            fw.write(result);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCatalog() {
        System.out.println("Please_input_new_catalog:");
        String catalog = Main.scanner.next();
        String c = catalog.substring(0, 1).toUpperCase() + catalog.substring(1);
        if (catalog.length() >= 12) {
            System.out.println("Error_catalog_to_long");
        } else if (catalogs.contains(c)) {
            System.out.println("Error_catalog_existed");
        } else {
            catalogs.add(c);
            Collections.sort(catalogs);
            saveCatalogs();
            System.out.println("Add_catalog_" + catalog + "_success");
        }
    }

    public void saveCatalogs() {
        String result = "";
        for (String catalog : catalogs) {
            result += catalog + "\n";
        }
        try {
            FileWriter fw = new FileWriter(new File("catalog.txt"));
            fw.write(result);
            fw.close(); // close() will also execute flush()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCatalog() {
        System.out.println("[Catalog]");
        for (String catalog : catalogs) {
            System.out.println(catalog);
        }
    }

    public ArrayList<Job> searchJob(int field, String value) {
        ArrayList<Job> result = new ArrayList<>();
        for (Job job : getSortedList()) {
            if (job.compareField(field, value))
                result.add(job);
        }
        return result;
    }

    public boolean checkData(int field, String data) {
        switch (field) {
            case 1:
                return idRegex(data);
            case 2:
                return nameRegex(data);
            case 3:
            case 4:
                return timeRegex(data);
            case 5:
                return degreeRegex(data);
            case 6:
                return stateRegex(data);
            case 7:
                return numberRegex(data);
            case 8:
                return workRegex(data);
            default:
                return false;
        }
    }

    public void optimizeData() {
        jobs = getSortedList();
        saveJobs();
    }

    public boolean idRegex(String id) {
        return id.matches("^[0-9]{1,4}$");
    }

    public boolean nameRegex(String name) {
        return !(name.matches("^[0-9]*$") || name == null || name.length() > 12);
    }

    public boolean timeRegex(String time) {
        if (!time.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}"))
            return false;
        String tokens[] = time.split(":");
        int hour = Integer.parseInt(tokens[0]);
        int min = Integer.parseInt(tokens[1]);
        int sec = Integer.parseInt(tokens[2]);
        if (!(hour >= 0 && hour < 24))
            return false;
        else if (!(min >= 0 && min < 60))
            return false;
        else if (!(sec >= 0 && sec < 60))
            return false;
        return true;
    }

    public boolean degreeRegex(String degree) {
        if (!degree.matches("[0-9]{1,3}"))
            return false;
        return Integer.parseInt(degree) >= 0 && Integer.parseInt(degree) <= 100;
    }

    public boolean stateRegex(String state) {
        return (state.equals("Doing") || state.equals("Finish") ||
                state.equals("Unfinish"));
    }

    public boolean numberRegex(String number) {
        return number.matches("[A-Z]{1}[0-9]{5}");
    }

    public boolean workRegex(String work) {
        return work.length() <= 26;
    }
}
