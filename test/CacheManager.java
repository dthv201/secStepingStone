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
    public void add(String word)
    {
        //check if already in cash
        if(!wordsInCache.contains(word))
        {
            //i need to see if there is a free space
            if(maxCacheSize <= wordsInCache.size())
            {
                //need to remove
                String toRemove = crp.remove();
                wordsInCache.remove(toRemove);
            }
            //now i can add
            wordsInCache.add(word);
            crp.add(word);
        }
        //it exsist it will increace the count if needed no need to do anthing in cash
        else
        {
            crp.add(word);
        }
    }
   
    


}
