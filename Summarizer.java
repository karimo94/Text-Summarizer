/*Karim Oumghar
 * Summarization class for SumIt!
 * Updated Novemeber 2015
 * */
package com.karimo.sumit_final;

import java.util.*;
import java.util.regex.Pattern;

public class Summarizer 
{
	public Summarizer(){}//default constructor
	
	private Map<String, Integer> getWordCounts(String text)
	{
		Map<String,Integer> allWords = new HashMap<String, Integer>();
		
		int count;
		int singleIncrement = 0;
		/* start with raw frequencies
		 * scan entire text and record all words and word counts
		 * so if a word appears multiple times, increment the word count for that particular word
		 * if a word appears only once, add the new word to the Map
		 */
		text.trim();
		String[] words = text.split("\\s+");//split with white space delimiters
		
		for(int i = 0; i < words.length; i++)
		{
			count = 0;
			
			if(allWords.containsKey(words[i]))//do a check to see if a word already exists in the collection
			{
				allWords.put(words[i], singleIncrement += 1);
			}
			else
			{
				allWords.put(words[i], count++);
			}
		}
		return allWords;
	}
	private Map<String,Integer> filterStopWords(Map<String, Integer> d)
	{
		//filter any stop words here, so remove from the dictionary collection
        //return a dictionary, use the dictionary to store the frequency of a word and the word itself
        String[] stop_words = { "a","able","about","after","all","also","am",
        "an","and","any","are","as","at","be","because","been","but","by","can","cannot","could","did",
        "do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","I",
        "if","in","into","is","it","its","just","let","like","likely","may","me",
        "might","most","must","my","neither","no","nor","not","of","off",
        "often","on","only","or","other","our","own","said","say","says","she",
        "should","so","some","than","that","the","their","them","then","there",
        "these","they","this","they're","to","too","that's","us","was","we","were",
        "what","when","where","which","while","who","whom","why","will","with",
        "would","yet","you","your", "you're" };
        
        for(int i= 0; i < stop_words.length; i++)
        {
        	if(d.containsKey(stop_words[i]))
        	{
        		d.remove(stop_words[i]);
        	}
        }
        
        return d;
	}
	private List<String> sortByFreqThenDropFreq(Map<String,Integer> wordFrequencies)
	{
		//sort the dictionary, sort by frequency and drop counts ['code', language']
        //return a List<string>
		List<String> sortedCollection = new ArrayList<String>(wordFrequencies.keySet());
		Collections.sort(sortedCollection);
		Collections.reverse(sortedCollection);	//largest to smallest
		return sortedCollection;
	}
	private String[] getSentences(String text)
	{
		text = text.replace("Mr.", "Mr").replace("Ms.", "Ms").replace("Dr.", "Dr").replace("Jan.", "Jan").replace("Feb.", "Feb")
				.replace("Mar.", "Mar").replace("Apr.", "Apr").replace("Jun.", "Jun").replace("Jul.", "Jul").replace("Aug.", "Aug")
				.replace("Sep.","Sep").replace("Spet.", "Sept").replace("Oct.", "Oct").replace("Nov.", "Nov").replace("Dec.", "Dec")
				.replace("St.", "St").replace("Prof.", "Prof").replace("Mrs.", "Mrs").replace("Gen.", "Gen")
                .replace("Corp.", "Corp").replace("Mrs.", "Mrs").replace("Sr.","Sr").replace("Jr.", "Jr").replace("cm.", "cm")
                .replace("Ltd.", "Ltd").replace("Col.", "Col").replace("vs.", "vs").replace("Capt.", "Capt")
                .replace("Univ.", "University").replace("Sgt.", "Sgt").replace("ft.","ft").replace("in.","in")
                .replace("Ave.", "Ave").replace("Univ.", "University").replace("Lt.", "Lt").replace("etc.", "etc").replace("mm.", "mm")
                .replace("\n\n", "").replace("\n", "").replace("\r", "");
		//solved! now fix alphabet letters like A. B. etc...use a regex
		text = text.replaceAll("([A-Z])\\.", "$1");
		
		//split using ., !, ?, and omit decimal numbers
		String pattern = "(?<!\\d)\\.(?!\\d)|(?<=\\d)\\.(?!\\d)|(?<!\\d)\\.(?=\\d)";
		Pattern pt = Pattern.compile(pattern);

		String[] sentences = pt.split(text);
		return sentences;
	}
	private String search(String[] sentences, String word)
	{
		//search for a particular sentence containing a particular word
        //this function will return the first matching sentence that has a value word
		String first_matching_sentence = null;
		for(int i = 0; i < sentences.length; i++)
		{
			if(sentences[i].contains(word))
			{
				first_matching_sentence = sentences[i];
			}
		}
		return first_matching_sentence;
	}
	public String Summarize(String text, int maxSummarySize)
	{
		if(text.equals("") || text.equals(" ") || text.equals("\n"))
		{
			String msg = "Nothing to summarize...";
			return msg;
		}
		//start with raw freqs
		Map<String, Integer> wordFrequencies = getWordCounts(text);
		
		//filter
		Map<String, Integer> filtered = filterStopWords(wordFrequencies);
		
		//sort
		List<String> sorted = sortByFreqThenDropFreq(filtered);
		
		//split the sentences
		String[] sentences = getSentences(text);
		
		//we should have the first sentence be part of the summary
		String firstSentence = sentences[0];
		String datePatternString = "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)\\s\\d{1,2}\\s(January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{4}\\s\\d{1,2}\\.\\d{2}(\\sEST|\\sPST)";
		
		firstSentence = firstSentence.replace("Last modified on", "");
		firstSentence = firstSentence.replaceAll(datePatternString,"");
		
		
		//select up to maxSummarySize sentences, so create a List<String>
		List<String> setSummarySentences = new ArrayList<String>();
		for(String word : sorted)//foreach string in the sorted list
		{
			String first_matching_sentence = search(sentences, word);
			setSummarySentences.add(first_matching_sentence);//add to summary list
			if(setSummarySentences.size() == maxSummarySize)
			{
				break;
			}
		}
		//construct the summary size out of select sentences
		String summary = "";
		summary = summary + "• " + firstSentence +  System.getProperty("line.separator") + System.getProperty("line.separator");
		
		for(String sentence : sentences)//foreach string sentence in sentences list
		{
			if(setSummarySentences.contains(sentence))
			{
				//produce each sentence with a bullet point and good amounts of spacing
				summary = summary + "• " + sentence +  System.getProperty("line.separator") + System.getProperty("line.separator");
			}
		}
		return summary;
	}
}