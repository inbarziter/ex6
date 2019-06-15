package processing.textStructure;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an arbitrary block of text within a file
 */
public class Block {
	private static final String EMPTY_BLOCK = "";

	public static final long serialVersionUID = 1L;
	private long startIdx;                  //index within the file where the block begins
	private long endIdx;                    //index within the file where the block ends
	private RandomAccessFile inputFile;     //the RAF object pointing to the physical file in the file system
	private List<String> metaData;
	private String entryName;

	/**
	 * Constructor
	 * @param inputFile     the RAF object backing this block
	 * @param startIdx      start index of the block within the file
	 * @param endIdx        end index of the block within the file
	 */
	public Block(RandomAccessFile inputFile, long startIdx, long endIdx) {
		this.inputFile = inputFile;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		metaData = new ArrayList<>();

	}

	///////// getters //////////

	/**
	 * The filename from which this block was extracted
	 * @return  filename
	 */
	public String getEntryName(){
		return entryName;
	}

	/**
	 * @return start index
	 */
	public long getStartIndex() {
		return startIdx;
	}

	/**
	 * @return  end index
	 */
	public long getEndIndex() {
		return endIdx;
	}

	/**
	 * @return the RAF object for this block
	 */
	public RandomAccessFile getRAF() {
		return inputFile;
	}

	/**
	 * Get the metadata of the block, if applicable for the parsing rule used
	 * @return  String of all metadata.
	 */
	public List<String> getMetadata() {
		return metaData;
	}
	
	/**
	 * Adds metadata to the block
	 * @param metaData A list containing metadata entries related to this block
	 */
	public void setMetadata(List<String> metaData){
		this.metaData = metaData;
	}


	/**
	 * Convert an abstract block into a string
	 * @return  string representation of the block
	 */
	@Override
	public String toString() {
		try {
			inputFile.seek(startIdx);
			byte [] fileBytes = new byte[(int)(endIdx - startIdx)];
			inputFile.readFully(fileBytes);
			String blockToString = new String(fileBytes);
			return blockToString;

		} catch (IOException e) {
			return EMPTY_BLOCK;
		}
	}

	/**
	 * A method that sets the entry name
	 * @param entryName - the name of the entry to set
	 */
	protected void setEntryName(String entryName){
		this.entryName = entryName;
	}
}
