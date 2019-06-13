package processing.textStructure;

import processing.parsingRules.IparsingRule;
import utils.MD5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a body of works - anywhere between one and thousands of documents sharing the
 * same structure and that can be parsed by the same parsing rule.
 */
public class Corpus implements Iterable<Entry>{
	public static final String EMPTY_STRING = "";

	ArrayList<Entry> entryList;
	IparsingRule theParsingRule;

	public Corpus(String path, IparsingRule parsingRule) throws IOException {
        /*
        check if the path is a folder or file.
        if file - single entry corpus.
        otherwise, recursively scan the directory for all subdirectories and files.
        each entry in a corpus should hold the folder from which the file came.
         */
		entryList = new ArrayList<>();
		theParsingRule = parsingRule;
		recursiveEntry(path,theParsingRule);
	}

	/**
	 * A recursive function that added all files in the given path to the entryList
	 * @param path- the path to take the files from
	 */
    private void recursiveEntry(String path, IparsingRule parsingRule ) throws FileNotFoundException {
		File directory = new File(path);
		File[] allFilesInDirectory = directory.listFiles();
		if (allFilesInDirectory != null) {
			for(File file: allFilesInDirectory){
				if(file.isFile() && file.canRead()){
					Entry newEntry = new Entry(file.getPath(),parsingRule);
					entryList.add(newEntry);
					break;
				}
				if(file.isDirectory()){
					recursiveEntry(file.getPath(), parsingRule);
				}
			}
		}
	}

	/**
	 * Populates the entries of an empty corpus (a corpus with only entries and no blocks)
	 */
	public void populate(){
		//TODO implement me!!!
	}
	
	/**
	 * Return the parsing rule used for this corpus
	 * @return
	 */
	public IparsingRule getParsingRule() {
		return theParsingRule;
	}

    /**
     * Iterate over Entry objects in the Corpus
     * @return  An Entry iterator
     */
    @Override
    public Iterator<Entry> iterator() {
	    return entryList.iterator();
    }

    /**
     * Return the checksum of the entire corpus.
     * Can be calculated by getting the checksum of each file, then concating them to one string and
     * returning the checksum of that string.
     * @return
     */
    public String getChecksum() {
    	String sum= EMPTY_STRING;
	    for(Entry entry: entryList){
			for (Iterator<Block> it = entry.iterator(); it.hasNext(); ) {
				Block block = it.next();
				sum += MD5.getMd5(block.toString());
			}
		}
	    return sum;
    }


}
