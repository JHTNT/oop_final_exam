public class Control {
    public static String formatString(String str, int n) {
        return String.format("%-" + n + "s", str);  // align left
    }

    public static String formatInt(int i, int n) {
        return String.format("%0" + n + "d", i);
    }
}
