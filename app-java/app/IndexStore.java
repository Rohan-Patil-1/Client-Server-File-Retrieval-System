package csc435.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class DocFreqPair {
    public long documentNumber;
    public long wordFrequency;

    public DocFreqPair(long documentNumber, long wordFrequency) {
        this.documentNumber = documentNumber;
        this.wordFrequency = wordFrequency;
    }
}

public class IndexStore {
    private ConcurrentHashMap<String, Long> documentMap;
    private ConcurrentHashMap<String, ArrayList<DocFreqPair>> termInvertedIndex;
    private final Object documentMapLock = new Object();
    private final Object termInvertedIndexLock = new Object();

    public IndexStore() {
        this.documentMap = new ConcurrentHashMap<>();
        this.termInvertedIndex = new ConcurrentHashMap<>();
    }

    public long putDocument(String documentPath) {
        // TO-DO implementing putting document in Map
    }

    public String getDocument(long documentNumber) {
        // TO-DO implementing getting document from Map
    }

    public void updateIndex(long documentNumber, HashMap<String, Long> wordFrequencies) {
        // TO-DO implementing updating index
    }

    public ArrayList<DocFreqPair> lookupIndex(String term) {
        return new ArrayList<>(termInvertedIndex.getOrDefault(term, new ArrayList<>()));
    }
}
