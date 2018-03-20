package seedu.address.logic.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Trie data structure for word auto-complete
 */
public class Trie {

    private Node root;

    private class Node {
        private HashMap<Character, Node> children = new HashMap<>();
        private boolean isCompleteWord = false;
    }

    /**
     * Creates a Trie
     */
    public Trie() {
        root = new Node();
    }

    /**
     * Insert a word into Trie
     */
    public void insertWord(String word) {
        insert(root, word);
    }

    /**
     *  Recursive insert to insert part of key into Trie
     */
    private void insert(Node currNode, String key) {
        if (!key.isEmpty()) {
            if (!currNode.children.containsKey(key.charAt(0))) {
                currNode.children.put(key.charAt(0), new Node());
            }
            insert(currNode.children.get(key.charAt(0)), key.substring(1));
        } else {
            currNode.isCompleteWord = true;
        }
    }

    /**
     * Auto-complete strings
     *
     * Returns an {@code ArrayList<String>} of auto-completed words
     */
    public ArrayList<String> autoComplete(String prefix) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : getAllPostFix(search(root, prefix))) {
            result.add(prefix + s);
        }
        return result;
    }

    /**
     * Recursive search for end node
     */
    private Node search(Node currNode, String key) {
        if (!key.isEmpty()) {
            return search(currNode.children.get(key.charAt(0)), key.substring(1));
        } else {
            return currNode;
        }
    }


    /**
     *  Returns arraylist of all postfix from node
     */
    private ArrayList<String> getAllPostFix(Node node) {
        ArrayList<String> listOfPostFix = new ArrayList<>();
        return getAllPostFix(node, "", null, listOfPostFix);
    }

    /**
     * Recursive method to get all postfix string
     */
    private ArrayList<String> getAllPostFix(Node node, String s, Character next, ArrayList<String> listOfPostFix) {
        if (next != null) {
            s += next;
        }
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            listOfPostFix = getAllPostFix(entry.getValue(), s, entry.getKey(), listOfPostFix);
        }
        if (node.isCompleteWord) {
            listOfPostFix.add(s);
        }
        return listOfPostFix;
    }

}

