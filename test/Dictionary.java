package test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Dictionary {
    private final CacheManager wordsInDictionary;
    private final CacheManager wordsNotInDictionary;
    private final BloomFilter bloomFilter;
    private List<String> fileNList;
    public Dictionary(String...fileNames)
    {
        wordsInDictionary = new CacheManager(400, new LRU());
        wordsNotInDictionary = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5","SHA1");
        this.fileNList = new ArrayList<>();  // Initialize the list

        for (String fileName : fileNames) {
            this.fileNList.add(fileName);  // Store filenames
        }
        // Load words from files
        loadWords(fileNames);


    }

    private void loadWords(String...fileNames)
    {
        for(String fileName: fileNames)
        {
          try (Stream<String> stream = Files.lines(Paths.get(fileName))) 
          {
                stream.forEach(word -> 
                {
                    bloomFilter.add(word);  // Add each word to the BloomFilter
                    wordsInDictionary.add(word);  // You might choose some logic to decide between frequent and infrequent
                });
            }
            catch (IOException e)
             {
                e.printStackTrace();
            }
        }   
    }
    public boolean query(String word)
    {
        boolean ans = true;
        // now we do the checks :
        
         //look in bloom filter and update cache manager 
         //find chache of existing in the word what are in the dictishinary
        if(wordsInDictionary.query(word))
        {
          return true;
        }
        else if(wordsNotInDictionary.query(word))
        {
          return false;
        }
        // if i got here means that it isn't in 2 of the cashes 
        else
        {
            if (!bloomFilter.contains(word)) 
            {
                // System.out.println(word + " is definitely not in the dataset.");
                ans = false; 
                wordsNotInDictionary.add(word);
            }
            else
            {
                ans = true;
                wordsInDictionary.add(word);
            }
            return ans;
        }
        
     
    }

    public boolean challenge(String word)
    {   
          if( IOSearcher.search(word, fileNList.toArray(new String [0])) )
          {
            wordsInDictionary.add(word);
            return true;
          }
          else
          {
            wordsNotInDictionary.add(word);
            return false;
          }
        
    }
 
}

