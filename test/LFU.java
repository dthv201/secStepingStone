package test;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFU implements CacheReplacementPolicy{
    
    private Map<String, String> values; // Maps keys to values.
    private Map<String, Integer> counts; // Maps keys to their access frequencies.
    private Map<Integer, LinkedHashSet<String>> lists; // Maps frequencies to list of keys.
    private int minFreq = 0;
    public LFU()
    {
       
        this.values = new HashMap<>();
        this.counts = new HashMap<>();
        this.lists = new HashMap<>();
        this.lists.put(1, new LinkedHashSet<>());
    }
    private String get4add(String key) 
    {
        if (!values.containsKey(key)) //dont have this value yet
        {
            return null;
        }
         // Increase the frequency count of the key
         int count = counts.get(key);
         counts.put(key,count+1);
         lists.get(count).remove(key);

         if(!lists.containsKey(count+1))
         {
            lists.put(count+1, new LinkedHashSet<>());
         }
         lists.get(count+1).add(key);
         //check if need to update minFreq
         if(count == minFreq && lists.get(count).isEmpty())
         {
            minFreq++;
         }
         return values.get(key);
    }
    @Override
    public void add(String word) {
       //if exsist the get will modiffy everything
       if(values.containsKey(word))
       {
         get4add(word);
         return;
       }
       //dosen't exsist yet 
       else
       {
        values.put(word, word);
        counts.put(word, 1);
        if (!lists.containsKey(1)) 
        { 
             // Ensure there is a set available for frequency 1.
            lists.put(1, new LinkedHashSet<>());
        }
        lists.get(1).add(word);
        minFreq = 1;

       }
    }

    @Override
    public String remove()
    {
        LinkedHashSet<String> set = lists.get(minFreq);
        String victimKey = set.iterator().next();
        return victimKey;
    }
    
}
