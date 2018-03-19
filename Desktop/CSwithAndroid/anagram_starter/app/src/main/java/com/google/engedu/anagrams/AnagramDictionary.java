 /* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Arrays;


public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    public ArrayList wordList;
    public HashSet wordSet;
    public HashMap lettersToWords;


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<String>();
        wordSet = new HashSet();
        lettersToWords = new HashMap();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetters(word);
            wordSet.add(word);
            if(lettersToWords.containsKey(sortedWord)) {
                ArrayList anagrams;
                anagrams = (ArrayList) lettersToWords.get(sortedWord);
                anagrams.add(word);
                lettersToWords.put(sortedWord, anagrams);
            }
            else {
                ArrayList anagrams = new ArrayList();
                anagrams.add(word);
                lettersToWords.put(sortedWord, anagrams);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return true;
    }

    public String sortLetters(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String sortedWord = new String(chars);
        return sortedWord;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortLetters(targetWord);
//        for (int i = 0; i < wordSet.size(); i++) {
//            if(wordSet.get(i).toString().length() == targetWord.length()) {
//                String sortedWordListWord = sortLetters(wordList.get(i));
//                if (sortedTargetWord.equals(sortedWordListWord)) {
//                    result.add(wordList.get(i));
//                }
//            }
//        }
        Iterator iter = wordSet.iterator();
        while(iter.hasNext()){
            if(iter.next().toString().length() == targetWord.length()) {
                String sortedWordListWord = sortLetters(iter.next().toString());
                if (sortedTargetWord.equals(sortedWordListWord)) {
                    result.add(iter.next().toString()));
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char letter = 'a'; letter <= 'z'; letter++) {
            if(lettersToWords.containsKey(sortLetters(word + letter)) {
                ArrayList<String> listAnagrams = lettersToWords.get(sortLetters(word + letter));
                for (int i = 0; i < listAnagrams.size(); i++) {
                    if(isGoodWord(word, listAnagrams.get(i))) {
                        result.add(listAnagrams.get(i));
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        return "stop";
    }
}
