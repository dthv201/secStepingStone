package test;

import java.util.HashSet;

public class CacheManager {
	
    private final int maxCacheSize;
    private final CacheReplacementPolicy crp;
    private final HashSet<String> wordsInCache;
	public CacheManager(int size, CacheReplacementPolicy crp)
    {
        this.maxCacheSize = size;
        this.crp = crp;
        wordsInCache = new HashSet<>();
    }
    public boolean query(String word)
    {
        
       return this.wordsInCache.contains(word);  
       
    }
    public void add(String sentence) {
        String[] words = sentence.split("\\s+"); // Split sentence into words by spaces
        for (String word : words) {
            // Check if the word is already in the cache
            if (!wordsInCache.contains(word)) {
                // Check if there is free space in the cache
                if (wordsInCache.size() >= maxCacheSize) {
                    // Need to remove the least/most appropriate item according to the policy
                    String toRemove = crp.remove();
                    wordsInCache.remove(toRemove);
                }
                // Now, add the new word to the cache
                wordsInCache.add(word);
                crp.add(word); // This should handle the internal logic of the Cache Replacement Policy
            }
            // If it exists, it will increase the count if needed; no need to do anything in the cache
            else {
                crp.add(word); // Ensure the policy updates its state, e.g., for LRU or LFU
            }
        }
    }
    
    // public void add(String word)
    // {
    //     //check if already in cash
    //     if(!wordsInCache.contains(word))
    //     {
    //         //i need to see if there is a free space
    //         if(maxCacheSize <= wordsInCache.size())
    //         {
    //             //need to remove
    //             String toRemove = crp.remove();
    //             wordsInCache.remove(toRemove);
    //         }
    //         //now i can add
    //         wordsInCache.add(word);
    //         crp.add(word);
    //     }
    //     //it exsist it will increace the count if needed no need to do anthing in cash
    //     else
    //     {
    //         crp.add(word);
    //     }
    // }
   
    


}
