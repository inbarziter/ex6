package processing.textStructure;

import processing.parsingRules.IparsingRule;

import java.util.Iterator;

/**
 * This class represents a single file within a Corpus
 */
public class Entry implements Iterable<Block>{

    private static String theFilePath;
    private static IparsingRule theParseRule;
    /**
	 * Main constructor
	 * @param filePath  The path to the file this entry represents
	 * @param parseRule The parsing rule to be used for this entry
	 */
    public Entry(String filePath, IparsingRule parseRule) {
        theFilePath = filePath;
        theParseRule = parseRule;
    }

    /**
     * Iterate over Block objects in the Entry
     * @return  A Block object iterator
     */
    @Override
    public Iterator<Block> iterator() {
	    //TODO implement me!!!
    }

}
