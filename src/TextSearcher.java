import java.io.*;

/**
 * The main program - A text searching module that indexes and queries large corpuses for strings or word groups
 */
public class TextSearcher {

    private static final String CORPUS = "CORPUS";
    private static final String INDEXER = "INDEXER";
    private static final String PARSE_RULE = "PARSE_RULE";
    private static final String QUERY = "QUERY";


    private static File arguments_file;
    private static String corpus;
    private static String indexer;
    private static String parse_rule;
    private static String query;

    /**
     * A constructor for the TextSearcher object. receives an argument file, reads it and initialized the
     * data members.
     * @param file the argument file
     * @throws IOException
     */
    public TextSearcher(File file) throws IOException {
        arguments_file = file;
        Reader inFile = new FileReader(arguments_file);
        BufferedReader inBuffer = new BufferedReader(inFile);
        String line = (inBuffer).readLine();
        while (line!=null){

            // switch case over the lines of the given file
            switch (line){
                case CORPUS:
                    line = (inBuffer).readLine();
                    corpus = line;
                    line = (inBuffer).readLine();
                    break;
                case INDEXER:
                    line = (inBuffer).readLine();
                    indexer = line;
                    line = (inBuffer).readLine();
                    break;
                case PARSE_RULE:
                    line = (inBuffer).readLine();
                    parse_rule = line;
                    line = (inBuffer).readLine();
                    break;
                case QUERY:
                    line = (inBuffer).readLine();
                    query = line;
                    line = (inBuffer).readLine();
            }
        }
    }


    /**
     * Main method. Reads and parses a command file and if a query exists, prints the results.
     * @param args
     */
    public static void main(String[] args) {

    }


}
