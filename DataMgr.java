import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class DataMgr {
    public static Config config = new Config();
    public static ArrayList<Job> jobs = new ArrayList<>();
    public static Set<String> catalogs = new TreeSet<>();

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
}
