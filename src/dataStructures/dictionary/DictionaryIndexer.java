package dataStructures.dictionary;

import dataStructures.Aindexer;
import processing.parsingRules.IparsingRule;
import processing.searchStrategies.DictionarySearch;
import processing.textStructure.Block;
import processing.textStructure.Corpus;
import processing.textStructure.Entry;
import processing.textStructure.Word;
import utils.Stemmer;
import utils.Stopwords;
import utils.WrongMD5ChecksumException;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An implementation of the abstract Aindexer class, backed by a simple hashmap to store words and their
 * locations within the files.
 */
public class DictionaryIndexer extends Aindexer<DictionarySearch> {
	private static final String SPACE = " ";
	private static final String UNDER_LINE = "_";
	private static String PATH_FOR_CACHE = "/temp/";
	private static String CACHE_ENDING = ".cache";
	private static String cachePath ;


	private Stopwords stopwords = new Stopwords();
	private static final Stemmer STEMMER = new Stemmer();
	public static final IndexTypes TYPE_NAME = IndexTypes.DICT;
	private static HashMap<String, List<Word>> dictionaryIndexer;

	/**
	 * Basic constructor, sets origin Corpus and initializes backing hashmap
	 * @param origin    the Corpus to be indexed by this DS.
	 */
	public DictionaryIndexer(Corpus origin) {
		super(origin);
		dictionaryIndexer = new HashMap<>();
		cachePath = findCacheName();
	}


	@Override
	protected void readIndexedFile() throws WrongMD5ChecksumException, IOException, ClassNotFoundException {
		//TODO: CHECK IF IT IS THE CORRECT FILE NAME
		File fileToCache = new File(origin.getPath());
		InputStream fis = new FileInputStream(PATH_FOR_CACHE + findCacheName(fileToCache));
		ObjectInputStream ois = new ObjectInputStream(fis);
		Corpus corpusCache = (Corpus) ois.readObject();
		if(corpusCache.getChecksum().equals(this.origin.getChecksum())){
			dictionaryIndexer = (HashMap<String, List<Word>>) ois.readObject();
		}
		else {
			indexCorpus();
		}

	}
	@Override
	protected void writeIndexFile(){

		File fileToCache = new File(origin.getPath());
		if(fileToCache.isFile()){
			String parentPath=  fileToCache.getParent();
		}

		try{
			String pathCache = PATH_FOR_CACHE+findCacheName(fileToCache);
			OutputStream fos = new FileOutputStream(pathCache);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(origin);
			oos.writeObject(dictionaryIndexer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String findCacheName(File fileToCache){
		return TYPE_NAME +UNDER_LINE+ getParseRule()+UNDER_LINE+ fileToCache.getName() + CACHE_ENDING;
	}

	/**
	 * A method that converts an array to set in order to remove duplicate
	 * @param wordArray the array to convert
	 * @return a set
	 */
	private static Set<String> convertArrayToSet(String[] wordArray) {
		Set<String> wordSet = new HashSet<>();
		for (int i = 0; i < wordArray.length; i++) {
			wordSet.add(wordArray[i]);
		}
		return wordSet;
	}

	/**
	 * A method that finds all the indexes in which a given word found in the block
	 * @param word- the word to search
	 * @param blk- the block to search
	 * @param wordIndexList- the result containing the the indexes in which a given word found in the block
	 */
	private void findWordInText(String word, Block blk, List<Word> wordIndexList) {
		Pattern pattern = Pattern.compile(word);
		Matcher match = pattern.matcher(blk.toString());

		//loop over the block text while finding the required word
		while (match.find()) {
			long startInd = match.start();
			wordIndexList.add(new Word(blk,startInd,startInd+word.length()-1));
		}
	}

	/**
	 * A method that adds additional indexes to a given key in the dictionary
	 * @param wordIndexList- the indexes to add
	 * @param word- the given key
	 */
	private void AddToExistingKey(List<Word> wordIndexList, String word){
		List<Word> excitingList =dictionaryIndexer.get(STEMMER.stem(word));
		excitingList.addAll(wordIndexList);
		dictionaryIndexer.put(STEMMER.stem(word),excitingList);
	}


	@Override
	protected void indexCorpus() {
		//loop over each entry
		for (Iterator<Entry> it = origin.iterator(); it.hasNext(); ) {
			Entry entry = it.next();

			//loop over each block
			for (Iterator<Block> it1 = entry.iterator(); it1.hasNext(); ) {
				Block blk = it1.next();

				String[] wordArray = blk.toString().split(SPACE);
				Set<String> wordSet = convertArrayToSet(wordArray);

				// loop over each word in the set
				for (String word : wordSet) {

					//only in case its not a stop word
					if (!stopwords.isStopword(word)) {
						List<Word> WordList= new ArrayList<>();
						findWordInText(word, blk, WordList);

						//in case the word is already in the dictionary
						if (dictionaryIndexer.containsKey(STEMMER.stem(word))) {
							AddToExistingKey(WordList , word);
						}
						//in case the word is not in the dictionary
						else{
							dictionaryIndexer.put(STEMMER.stem(word),WordList);
						}
					}
				}
			}
		}
	}


	@Override
	public IparsingRule getParseRule() {
		return this.origin.getParsingRule();
	}


	@Override
	public DictionarySearch asSearchInterface() {
		return new DictionarySearch(dictionaryIndexer);
	}
}




