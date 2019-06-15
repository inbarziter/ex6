package processing.textStructure;

import processing.parsingRules.IparsingRule;
import utils.MD5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a body of works - anywhere between one and thousands of documents sharing the
 * same structure and that can be parsed by the same parsing rule.
 */
public class Corpus implements Iterable<Entry>, Serializable {
	public static final String EMPTY_STRING = "";
	public static final long serialVersionUID = 1L;

	private ArrayList<Entry> entryList;
	private IparsingRule parsingRule;
	private String corpusPath;


	public Corpus(String path, IparsingRule parsingRule) throws IOException {
        /*
        check if the path is a folder or file.
        if file - single entry corpus.
        otherwise, recursively scan the directory for all subdirectories and files.
        each entry in a corpus should hold the folder from which the file came.
         */
		entryList = new ArrayList<>();
		this.parsingRule = parsingRule;
		this.corpusPath = path;
		recursiveEntry(this.corpusPath, this.parsingRule);

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
	public void populate() throws FileNotFoundException {
		for(Entry entry: entryList){
			entry.createBlocks();
		}
	}
	
	/**
	 * Return the parsing rule used for this corpus
	 * @return
	 */
	public IparsingRule getParsingRule() {
		return this.parsingRule;
	}

    /**
     * Iterate over Entry objects in the Corpus
     * @return  An Entry iterator
     */
    @Override
    public Iterator<Entry> iterator() {
	    return this.entryList.iterator();
    }

    /**
     * Return the checksum of the entire corpus.
     * Can be calculated by getting the checksum of each file, then concating them to one string and
     * returning the checksum of that string.
     * @return
     */
    public String getChecksum() throws IOException {
    	String sum= EMPTY_STRING;
	    for(Entry entry: entryList){
			for (Iterator<Block> it = entry.iterator(); it.hasNext(); ) {
				Block block = it.next();
				sum += MD5.getMd5(block.toString());
			}
		}
	    return sum;
    }

	/**
	 * The path to the corpus folder
	 * @return A String representation of the absolute path to the corpus folder
	 */
	public String getPath() {
		return corpusPath;
	}

	/**
	 * Update the RandomAccessFile objects for the Entries in the corpus, if it was loaded from cache.
	 */
	public void updateRAFs() {
		//TODO
	}

}
