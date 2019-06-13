package processing.textStructure;

import processing.parsingRules.IparsingRule;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class represents a single file within a Corpus
 */
public class Entry implements Iterable<Block>{
    private static final String SEPARATOR = "\\";
    private static final String READING = "r";

    private static String theFilePath;
    private static IparsingRule theParseRule;
    private static List<Block> blockList;
    /**
	 * Main constructor
	 * @param filePath  The path to the file this entry represents
	 * @param parseRule The parsing rule to be used for this entry
	 */
    public Entry(String filePath, IparsingRule parseRule) throws FileNotFoundException {
        theFilePath = filePath;
        theParseRule = parseRule;
        blockList = crateBlockList();
        setNameForBlock();

    }

    /**
     * Iterate over Block objects in the Entry
     * @return  A Block object iterator
     */
    @Override
    public Iterator<Block> iterator() {
	    return blockList.iterator();
    }

    /**
     * A method that returns this file name
     * @return this file name
     */
    protected String getEntryName(){
        String[] pathArray = theFilePath.split(Pattern.quote(SEPARATOR));
        return pathArray[pathArray.length-1];
    }

    /**
     * A method that creates a list of blocks
     * @return a list of blocks
     * @throws FileNotFoundException
     */
    protected List<Block> crateBlockList() throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile(theFilePath, READING);
        return theParseRule.parseFile(raf);
    }

    /**
     * A method that sets the entry name in each Block object.
     */
    protected void setNameForBlock(){
        for(Block block: blockList){
            block.setEntryName(getEntryName());
        }
    }



}
