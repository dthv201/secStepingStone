package test;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class LRU implements CacheReplacementPolicy{

    private final int capacity;
    private final LinkedHashSet<String> cache;

    public LRU(int cap)
    {
        this.capacity = cap;
        this.cache = new LinkedHashSet<String>(capacity);
    
    }

    @Override
    public void add(String word) {
        // TODO Add new one if not exsist if i got space
        // if it allready exist
        if(this.cache.contains(word))
        {
            this.cache.remove(word);
        }
        //now handelling adding back
        if(this.cache.size() == capacity)
        {
            Iterator<String> it = cache.iterator();  // Get the iterator to access the first item
            if (it.hasNext()) {
                it.next();  // Move to the first item
                it.remove();  // Remove the first item
            }
        }
        //now all good need to add to the end
        cache.add(word);
        
    }

    @Override
    
    public String remove() {
        // TODO returns the least frecuntly used to remove dosend remove anything
        if (!cache.isEmpty()) {
            return cache.iterator().next();
        }
        return null; 
        
    }
    
}
