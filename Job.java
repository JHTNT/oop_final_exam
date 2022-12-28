public class Job {
    int id;
    String name;
    String start;
    String end;
    String degree;
    String state;
    String number;
    String catalog;
    String work;

    public Job(int id, String name, String start, String end, String degree,
            String state, String number, String catalog, String work) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
        this.degree = degree;
        this.state = state;
        this.number = number;
        this.catalog = catalog;
        this.work = work;
    }

    public void print(boolean show_name, boolean show_start, boolean show_end,
            boolean show_degree, boolean show_state, boolean show_number,
            boolean show_catalog, boolean show_work) {
        String title = String.format("%04d", id);
        if (show_name)
            title += " " + String.format("%-12s", name);
        if (show_start)
            title += " " + String.format("%-8s", start);
        if (show_end)
            title += " " + String.format("%-8s", end);
        if (show_degree)
            title += " " + String.format("%-8s", degree);
        if (show_state)
            title += " " + String.format("%-12s", state);
        if (show_number)
            title += " " + String.format("%-8s", number);
        if (show_catalog)
            title += " " + String.format("%-12s", catalog);
        if (show_work)
            title += " " + work;
        System.out.println(title);
    }
}
