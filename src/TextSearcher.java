import java.io.*;

/**
 * The main program - A text searching module that indexes and queries large corpuses for strings or word groups
 */
public class TextSearcher {

    private static final String CORPUS = "CORPUS";
    private static final String INDEXER = "INDEXER";
    private static final String PARSE_RULE = "PARSE_RULE";
    private static final String QUERY = "QUERY";
    private static final String SEPARATOR = "#";
    private static final int NO_FILTER =1;
    private static final int ONE_FILTER=2;
    private static final int TWO_FILTER=3;


    private static File arguments_file;
    private static String corpus;
    private static String indexer;
    private static String parse_rule;
    private static String query;
    private static QueryType queryType;

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
        // initialized the Type of the query
        queryType = buildQuick();
    }

    /**
     * Finds the type of the query
     * @return the type of the query
     */
    public QueryType buildQuick(){
        if(query!=null){
            String [] splitQuery = query.split(SEPARATOR);
            if(splitQuery.length == ONE_FILTER){
                String type = splitQuery[ONE_FILTER-1];
                if(type.equals(QueryType.CASE.name())){
                    return QueryType.CASE;
                }
                else {
                    return QueryType.QUICK;
                }
            }
            if(splitQuery.length == TWO_FILTER){
                return QueryType.QUICK_CASE;
            }
        }
        return QueryType.NO_FILTER;
    }

    /**
     * Main method. Reads and parses a command file and if a query exists, prints the results.
     * @param args
     */
    public static void main(String[] args) {

    }


}
