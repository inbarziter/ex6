package processing.searchStrategies;
import processing.textStructure.Corpus;
//TODO :CHECK
import processing.textStructure.*;
//
import processing.textStructure.WordResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NaiveSearch implements IsearchStrategy {
	private static final String PERIOD = "\\.";

	private Corpus origin;
	public NaiveSearch(Corpus origin) {
		this.origin = origin;
	}

	
	@Override
	public List<WordResult> search(String query) {
		List<WordResult> ResultList = new ArrayList<>();

		//loop over each entry
		for (Iterator<Entry> it = origin.iterator(); it.hasNext(); ) {
			Entry entry = it.next();

			//loop over each block
			for (Iterator<Block> it1 = entry.iterator(); it1.hasNext(); ) {
				Block originalBlk = it1.next();
				Block cutBlk = originalBlk;

				Pattern pattern = Pattern.compile(query);
				Matcher match = pattern.matcher(originalBlk.toString());

				//loop over the block text while finding the required query
				while (match.find()) {
					long startInd = match.start();
					String blkFromStartInd = cutBlk.toString().substring((int) startInd);
					Pattern patternPeriod = Pattern.compile(PERIOD);
					Matcher matchPeriod = patternPeriod.matcher(blkFromStartInd);
					long endInd;

					// checks if the sentence ends with a period or else initialized the end index as the
					// end of this block's text
					if (matchPeriod.find()) {
						endInd = match.start() + startInd;
					}
					else {
						endInd = cutBlk.toString().length() - 1;
					}

					//creates the result string to add to the ResultList
					String[] result = {cutBlk.toString().substring((int)startInd,(int)endInd)};
					WordResult wordResult = new WordResult(originalBlk, result, startInd);
					ResultList.add(wordResult);

					//cuts the block string to continue searching for the query
					cutBlk.toString().substring((int)endInd);
					pattern = Pattern.compile(query);
					match = pattern.matcher(cutBlk.toString());
				}
			}
		}
		return ResultList;
	}

}
