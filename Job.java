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
        String title = Control.formatInt(id, 4);
        if (show_name)
            title += " " + Control.formatString(name, 12);
        if (show_start)
            title += " " + Control.formatString(start, 8);
        if (show_end)
            title += " " + Control.formatString(end, 8);
        if (show_degree)
            title += " " + Control.formatString(degree, 8);
        if (show_state)
            title += " " + Control.formatString(state, 12);
        if (show_number)
            title += " " + Control.formatString(number, 8);
        if (show_catalog)
            title += " " + Control.formatString(catalog, 12);
        if (show_work)
            title += " " + work;
        System.out.println(title);
    }
}
