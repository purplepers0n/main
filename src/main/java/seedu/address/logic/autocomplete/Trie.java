package seedu.address.logic.autocomplete;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@@author jonathanwj
/**
 * Trie data structure for word auto-complete
 */
public class Trie {

    private Node root;
    private int size = 0;

    /**
     * Represents node a Trie
     */
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
        requireNonNull(word);
        insert(root, word);
    }

    /**
     * Recursive insert to insert part of key into Trie
     */
    private void insert(Node currNode, String key) {
        if (!key.isEmpty()) {
            if (!currNode.children.containsKey(key.charAt(0))) {
                currNode.children.put(key.charAt(0), new Node());
            }
            insert(currNode.children.get(key.charAt(0)), key.substring(1));
        } else {
            if (currNode.isCompleteWord == false) {
                size++;
            }
            currNode.isCompleteWord = true;
        }
    }

    /**
     * Auto-complete strings
     * <p>
     * Returns an {@code ArrayList<String>} of auto-completed words
     */
    public List<String> autoComplete(String prefix) {
        List<String> result = new ArrayList<>();
        if (search(root, prefix) == null) {
            return result;
        }
        for (String s : getAllPostFix(search(root, prefix))) {
            result.add(prefix + s);
        }
        return result;
    }

    /**
     * Recursive search for end node
     */
    private Node search(Node currNode, String key) {
        if (!key.isEmpty() && currNode != null) {
            return search(currNode.children.get(key.charAt(0)), key.substring(1));
        } else {
            return currNode;
        }
    }


    /**
     * Returns arraylist of all postfix from node
     */
    private List<String> getAllPostFix(Node node) {
        ArrayList<String> listOfPostFix = new ArrayList<>();
        return getAllPostFix(node, "", null, listOfPostFix);
    }

    /**
     * Recursive method to get all postfix string
     */
    private List<String> getAllPostFix(Node node, String s, Character next, List<String> listOfPostFix) {
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

    /**
     * @return size of Trie
     */
    public int size() {
        return size;
    }

}

