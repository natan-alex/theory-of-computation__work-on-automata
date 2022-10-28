package main;

public class App {
    private static final String HELP = "You must give a path for a .jff file" +
            " as a parameter to this program.";

    private static final String PARAMS_MSG = "Invalid number of parameters.";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(PARAMS_MSG);
            System.out.println(HELP);
        }
    }
}
