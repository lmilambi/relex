import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.ListIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Random;
import java.lang.StringBuilder;
import java.util.UUID;
import java.util.HashSet;
import java.util.Map;


/**Splits input into morphemes.
 * @author lareina
 *
 */
// public class MorphemeSplitter implements MorphemeSplitterInterface {
public class MorphemeSplitter {
	
		//get random number
		Random r = new Random();

    UUID guidSentenceNode = UUID.randomUUID();

	public String Start(String line, int max_parses)
	{
		try
		{
			ArrayList<String> listOfWords = new ArrayList<String>();

			ArrayList<String> wordsInSentence = SplitSentence(line);
			listOfWords.addAll(wordsInSentence);
			int count = 0;
         String out = "";
         while (count < max_parses) {
			    out += CreateMorphemes(listOfWords, count); 
				count ++;
         }

   	String sentenceID = "sentence@" + guidSentenceNode;
		out += "\n(ListLink (stv 1 1)\n" +
		"   (AnchorNode \"# New Parsed Sentence\")\n" +
		"   (SentenceNode \"" + sentenceID + "\"))";
		
			return out;
		
		} catch (Exception ioe) {
			System.out.println("Read/Write Error, " + ioe.toString());
		}
		return null;
	}
		
	public ArrayList<String> SplitSentence(String sentence)
	{
		//remove punctuation in each word
		sentence = sentence.replace("!", "");
		sentence = sentence.replace(".", "");
		sentence = sentence.replace("{", "");
		sentence = sentence.replace("}", "");
		sentence = sentence.replace("'", "");
		sentence = sentence.replace("(", "");
		sentence = sentence.replace(")", "");
		sentence = sentence.replace(",", "");
		sentence = sentence.replace("-", "");
		sentence = sentence.replace("_", "");
        
		ArrayList<String> listOfWords = new ArrayList<String>();
		//ArrayList<String> listOfMorphemes = new ArrayList<String>();
    	
		//set the most recently read line to a String Tokenizer object so that the line can be split into words
		StringTokenizer sntc = new StringTokenizer(sentence);
      
      
		//while the current line/String Tokenizer object has more tokens
		while (sntc.hasMoreTokens()) 
		{
			//declare variables currentWord
			String currentWord;
			
			//read next word in line and add it to listOfWord
			currentWord = sntc.nextToken();
			listOfWords.add(currentWord);
                        
		} 
		return listOfWords;
	}
    
	public String CreateMorphemes(ArrayList<String> listOfWords, int parseno) 
	{
		System.out.println("Creating parse number " + parseno);
		
      if (0 == parseno) guidSentenceNode = UUID.randomUUID();

   	String sentenceID = "sentence@" + guidSentenceNode;
	
		String parseID = sentenceID + "_parse_" + parseno;

		//iterate through words, and each word into two morphemes
		ListIterator<String> wordIterator = listOfWords.listIterator();
		StringBuilder output = new StringBuilder();
		
		output.append("\n(ParseLink (stv 1 1)\n" +
				"   (ParseNode \"" + parseID + "\")\n" +
				"   (SentenceNode \"" + sentenceID + "\")\n" +
				")\n");
		
		while (wordIterator.hasNext()) 
		{
			String word =  wordIterator.next();
	
			//get word length
			int wordLength;
			wordLength = word.length();
	
			if (1 == wordLength) continue;

			//get random character index between 1 and word length
			int splitIndex = r.nextInt(wordLength-1) + 1;
		
			//split word at randomly found character index, and move morphemes into an arraylist
			String morpheme1 = word.substring(0,splitIndex);
			String morpheme2 = word.substring((splitIndex), wordLength);
		
			//add first morpheme of the list of morphemes
			//listOfMorphemes.add(morpheme1);
			//add second morpheme of the list of morphemes
			//listOfMorphemes.add(morpheme2);
			
			
			
			UUID guidEvaluationLinkMorpheme1 = UUID.randomUUID();
			String ELMorph1ID = morpheme1 + "@" + guidEvaluationLinkMorpheme1;
			
			UUID guidEvaluationLinkMorpheme2 = UUID.randomUUID();
			String ELMorph2ID = morpheme2 + "@" + guidEvaluationLinkMorpheme2;
			
			
			
			output.append("\n(EvaluationLink (stv 1.0 1.0)\n (LinkGrammarRelationshipNode \"MOR\")\n (ListLink \n" +
			"   (WordInstanceNode \"" + ELMorph1ID + "\")\n" +
			"   (WordInstanceNode \"" + ELMorph2ID + "\")\n" +
			"	)\n" +
			")\n");
			
			output.append("\n(ReferenceLink (stv 1.0 1.0)\n" +
			"   (WordInstanceNode \"" + ELMorph1ID + "\")\n" +
			"   (WordNode \"" + morpheme1 + "\")\n" +
			")\n" +
			"(WordInstanceLink (stv 1.0 1.0)\n" +
			"   (WordInstanceNode \"" + ELMorph1ID + "\")\n" +
			"   (ParseNode \"" + parseID + "\")\n" +
			")\n");
		
			output.append("\n(ReferenceLink (stv 1.0 1.0)\n" +
			"   (WordInstanceNode \"" + ELMorph2ID + "\")\n" +
			"   (WordNode \"" + morpheme2 + "\")\n" +
			")\n" +
			"(WordInstanceLink (stv 1.0 1.0)\n" +
			"   (WordInstanceNode \"" + ELMorph2ID + "\")\n" +
			"   (ParseNode \"" + parseID + "\")\n" +
			")\n");
			
			 
		}

		System.out.println(output.toString());
		return output.toString();
	}
	
}
