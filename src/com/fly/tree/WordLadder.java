package com.fly.tree;

import java.io.*;
import java.util.*;

/**
 * Created by Fanliyan on 2017/5/24.
 */
public class WordLadder {
    public static List<String> readWords(BufferedReader in) throws IOException {
        String oneLine;
        List<String> lst = new ArrayList<>();
        while((oneLine = in.readLine()) != null){
            lst.add(oneLine);
        }
        return lst;
    }

    /**
     * 如果word1和word2有相同的长度且仅有一个字母不同，返回true
     * @param word1
     * @param word2
     * @return
     */
    private static boolean oneCharOff(String word1, String word2){
        if(word1.length() != word2.length()){
            return false;
        }
        int diffs = 0;
        for (int i = 0; i < word1.length(); i++) {
            if(word1.charAt(i) != word2.charAt(i)){
                if(++diffs > 1){
                    return false;
                }
            }
        }
        return diffs == 1;
    }

    private static <KeyType> void update(Map<KeyType, List<String>> m, KeyType key, String value){
        List<String> lst = m.get(key);
        if(lst == null){
            lst = new ArrayList<>();
            m.put(key, lst);
        }
        lst.add(value);
    }

    /**
     * 计算一个键是单词，值是同一个键但仅仅有一个字母不同的单词集合
     * @param theWords
     * @return
     */
    public static Map<String, List<String>> computeAdjAcentWordsSlow(List<String> theWords){
        Map<String, List<String>> adjWords = new HashMap<>();
        String[] words = new String[theWords.size()];
        theWords.toArray(words);
        for(int i = 0; i < words.length; i++){
            for(int j = i + 1; j < words.length; j++){
                if(oneCharOff(words[i], words[j])){
                    update(adjWords, words[i], words[j]);
                    update(adjWords, words[j], words[i]);
                }
            }
        }
        return adjWords;
    }

    /**
     * Computes a map in which the keys are words and values are Lists of words
     * that differ in only one character from the corresponding key.
     * Uses a quadratic algorithm (with appropriate Map), but speeds things up a little by
     * maintaining an additional map that groups words by their length.
     * @param theWords
     * @return
     */
    public static Map<String, List<String>> computeAdjacentWordsMedium(List<String> theWords){
        Map<String, List<String>> adjWords = new HashMap<>();
        Map<Integer, List<String>> wordsByLength = new HashMap<>();

        for(String w: theWords){
            update(wordsByLength, w.length(), w);
        }
        for(List<String> groupsWords: wordsByLength.values()){
            String[] words = new String[groupsWords.size()];
            groupsWords.toArray(words);
            for (int i = 0; i < words.length; i++) {
                for (int j = i + 1; j < words.length; j++) {
                    if(oneCharOff(words[i], words[j])){
                        update(adjWords, words[i], words[j]);
                        update(adjWords, words[j], words[i]);
                    }
                }
            }
        }
        return adjWords;
    }

    // Computes a map in which the keys are words and values are Lists of words
    // that differ in only one character from the corresponding key.
    // Uses an efficient algorithm that is O(N log N) with a TreeMap, or
    // O(N) if a HashMap is used.
    public static Map<String, List<String>> computeAdjacentWords(List<String> words){
        Map<String, List<String>> adjWords = new TreeMap<>();
        Map<Integer, List<String>> wordsByLength = new TreeMap<>();
        // Group the words by their length
        for (String w: words) {
            update(wordsByLength, w.length(), w);
        }
        for(Map.Entry<Integer, List<String >> entry: wordsByLength.entrySet()){
            List<String> groupsWords = entry.getValue();
            int groupNum = entry.getKey();

            for(int i = 0; i < groupNum; i++){
                Map<String, List<String>> repToWord = new HashMap<>();
                for(String str: groupsWords){
                    String rep = str.substring(0, i) + str.substring(i + 1);
                    update(repToWord, rep, str);
                }
                for(List<String> wordClique: repToWord.values()){
                    if(wordClique.size() >= 2){
                        for(String s1: wordClique){
                            for(String s2: wordClique){
                                if(s1 != s2){
                                    update(adjWords, s1, s2);
                                }
                            }
                        }
                    }
                }
            }
        }
        return adjWords;
    }

    // Find most changeable word: the word that differs in only one
    // character with the most words. Return a list of these words, in case of a tie.
    public static List<String> findMostChangeable(Map<String, List<String>> adjacentWords){
        List<String> mostChangeableWords = new ArrayList<>();
        int maxNumberOfAdjacentWords = 0;
        for(Map.Entry<String, List<String>> entry: adjacentWords.entrySet()){
            List<String> changes = entry.getValue();
            if(changes.size() > maxNumberOfAdjacentWords){
                maxNumberOfAdjacentWords = changes.size();
                mostChangeableWords.clear();
            }
            if(changes.size() == maxNumberOfAdjacentWords){
                mostChangeableWords.add(entry.getKey());
            }
        }
        return mostChangeableWords;
    }

    public static void printMostChangeables(List<String> mostChangeable, Map<String, List<String>> adjacentWords){
        for(String word: mostChangeable){
            System.out.print(word + ":");
            List<String> adjacents = adjacentWords.get(word);
            for(String str: adjacents){
                System.out.println(" " + str);
            }
            System.out.println(" (" + adjacents.size() + " words)");
        }
    }

    public static void printHighChangeables(Map<String, List<String>> adjacentWords, int minWords){
        for(Map.Entry<String, List<String>> entry: adjacentWords.entrySet()){
            List<String> words = entry.getValue();
            if(words.size() >= minWords){
                System.out.print(entry.getKey() + " )" + words.size() + "):");
                for (String w: words){
                    System.out.println(" " + w);
                }
                System.out.println();
            }
        }
    }

    // After the shortest path calculation has run, computes the List that
    // contains the sequence of word changes to get from first to second.
    public static List<String> getChainFromPreviousMap(Map<String, String> prev, String first, String second){
        LinkedList<String> result = new LinkedList<>();
        if(prev.get(second) != null){
            for(String str = second; str != null; str = prev.get(str)){
                result.addFirst(str);
            }
        }
        return result;
    }

    // Runs the shortest path calculation from the adjacency map, returning a List
    // that contains the sequence of words changes to get from first to second.
    public static List<String> findChain(Map<String, List<String>> adjacentWords, String first, String second){
        Map<String, String> previousWord = new HashMap<>();
        Queue<String> q = new LinkedList<>();
        q.add(first);
        while (!q.isEmpty()){
            String current = q.element();
            q.remove();
            List<String> adj = adjacentWords.get(current);
            if(adj != null){
                for(String adjWord: adj){
                    if(previousWord.get(adjWord) == null){
                        previousWord.put(adjWord, current);
                        q.add(adjWord);
                    }
                }
            }
        }
        previousWord.put(first, null);
        return getChainFromPreviousMap(previousWord, first, second);
    }

    // Runs the shortest path calculation from the original list of words, returning
    // a List that contains the sequence of word changes to get from first to
    // second. Since this calls computeAdjacentWords, it is recommended that the
    // user instead call computeAdjacentWords once and then call other findChar for
    // each word pair.
    public static List<String> findChain(List<String> words, String first, String second){
        Map<String, List<String>> adjacentWords = computeAdjacentWords(words);
        return findChain(adjacentWords, first, second);
    }

    public static void main(String[] args) throws IOException {
        long start, end;
        FileReader fin = new FileReader("dict.txt");
        BufferedReader bin = new BufferedReader(fin);
        List<String> words = readWords(bin);
        System.out.println("Read the words..." + words.size());
        Map<String, List<String>> adjacentWords;

        start = System.currentTimeMillis();
        adjacentWords = computeAdjacentWords(words);
        end = System.currentTimeMillis();
        System.out.println("Elapsed time FAST:" + (end - start));

        start = System.currentTimeMillis();
        adjacentWords = computeAdjAcentWordsSlow(words);
        end = System.currentTimeMillis();
        System.out.println("Elapsed time SLOW:" + (end - start));

       // printHighChangeables(adjacentWords, 15);

        System.out.println("Adjacents computed...");
        List<String> mostChangeable = findMostChangeable(adjacentWords);
        System.out.println("Most changeable computed...");
        printMostChangeables(mostChangeable, adjacentWords);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        for( ; ; ){
            System.out.println("Enter two words: ");
            String w1 = in.readLine();
            String w2 = in.readLine();

            List<String> path = findChain(adjacentWords, w1, w2);
            System.out.print(path.size() + "...");
            for (String word: path) {
                System.out.print(" " + word);
            }
            System.out.println();
        }
    }
}