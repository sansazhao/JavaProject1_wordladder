package pers.wl;


import java.io.*;
import java.util.*;

public class Wordladder {
    /*several global variable*/
    //the queue containing stacks of words
    public static Queue<Stack<String>>ladder = new LinkedList<Stack<String>>();
    //all words in the dictionary
    public static Set<String>wordSet = new HashSet<String>();
    //the already-used neighbor
    public static Set<String>used_wordSet = new HashSet<String>();
    public static String alp = "abcdefghijklmnopqrstuvwxyz";
    private static boolean ladder_exist = false;
    public static String word1="";
    public static String word2="";
    public static void neighbor(String w,String w2,Stack<String>curs) {
        // add the neighbor words to the stack
        int len = w.length();
        for (int i = 0; i<len; ++i) {
            StringBuffer neigh = new StringBuffer(w);
            for (int j = 0; j < alp.length();j++) {
                neigh.setCharAt(i,alp.charAt(j));
                String neighStr = new String(neigh);
                if (wordSet.contains(neighStr) &&
                        !used_wordSet.contains(neighStr)) {
                    Stack<String>newStack = (Stack<String>)curs.clone();
                    newStack.add(neighStr);
                    used_wordSet.add(neighStr);
                    ladder.offer(newStack);
                }
            }
        }
    }

    private static void ladder_to_word(String word2, Stack<String>cur_stack) {
        //find the ladder ending with word2
        while (ladder.size() != 0) {
            cur_stack = ladder.peek();
            String cur_word = cur_stack.peek();
            if (cur_word.equals(word2)) {
                ladder_exist = true;
                printLadder(word1,word2,cur_stack);
                return;
            }
            else
                neighbor(cur_word,word2,cur_stack);
            ladder.poll();
        }
        printLadder(word1,word2,cur_stack);
    }

    private static void printLadder(String word1, String word2, Stack<String>cur_stack) {
        if (ladder_exist) {
            int size = cur_stack.size();
            System.out.print("A ladder from "+word2+" back to "+
                    word1 + " :");
            for (int i = 0; i < size; ++i) {
                String w = cur_stack.peek();
                System.out.print(cur_stack.peek()+" ");
                cur_stack.pop();
            }
        }
        else
            System.out.println("No word ladder found from "+word1+" back to "
                    +word2+".");
    }

    private static void searchLadder(String w1,String w2){
        //complete process of finding ladder
        Stack<String>cur_stack = new Stack<String>();
        Stack<String>wStack = new Stack<String>();
        used_wordSet.add(w1);
        wStack.push(w1);
        ladder.offer(wStack);
        ladder_to_word(w2,cur_stack);
    }

    public static void clear(){
        //clear the collections to get ready for the new loop
        ladder.clear();
        used_wordSet.clear();
        ladder_exist = false;
    }

    public static void main(String args[]) throws IOException
    {
        String fileName="";
        boolean open=false;
        while(!open) {
            System.out.print("Dictionary file name?");
            BufferedReader br = new BufferedReader(new
                    InputStreamReader(System.in));
            fileName = br.readLine();
            File in = new File(fileName);
            if (!in.exists()) {
                System.out.println("Unable to open that file.Try again.");
            }
            else
                open = true;
            FileReader file = new FileReader(fileName);
            BufferedReader read_file = new BufferedReader(file);
            boolean eof = false;
            String line;
            while(true) {
                line = read_file.readLine();
                if (line == null)
                    break;
                for (String word : line.split(" ")) {
                    wordSet.add(word);
                }
            }
            while (true) {
                System.out.print("\nWord1(or Enter to quit):");
                br = new BufferedReader(new
                        InputStreamReader(System.in));
                word1 = br.readLine().split(" ")[0];
                if(word1.equals("")){
                    System.out.println("Have a nice day.");
                    break;
                }
                System.out.print("Word2(or Enter to quit):");
                br = new BufferedReader(new
                        InputStreamReader(System.in));
                word2 = br.readLine().split(" ")[0];
                if(word2.equals("")){
                    System.out.println("Have a nice day.");
                    break;
                }
                word1 = word1.toLowerCase();
                word2 = word2.toLowerCase();
                //several valid tests
                if(word1.length() != word2.length())
                    System.out.println("The two words must be the same length.");
                else if (word1.equals(word2))
                    System.out.println("The two words must be different.");
                else {

                    if (!(wordSet.contains(word1) && wordSet.contains(word2)))
                        System.out.println("The two words must be found in the dictionary.");
                    else
                        searchLadder(word1, word2);
                }
                clear();
            }
        }
    }
}
