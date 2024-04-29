package test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class IOSearcher {

    private static String preprocessText(String text) {
        // Normalize newlines and multiple spaces to single space, and trim the text
        text = text.replaceAll("[\\n\\t]+", " ");  // Replace newlines and tabs with a single space
        text = text.replaceAll("\\s+", " ");  // Replace multiple spaces with a single space
        return text.trim();  // Trim leading and trailing spaces
    }

    public static boolean search(String word, String...fileNames)
    {
        Stream <String> s;
        for (String fileName : fileNames)
         {
            try
            {
               String content = new String(Files.readAllBytes(Paths.get(fileName)),StandardCharsets.UTF_8);
               content = preprocessText(content);
               if (Arrays.asList(content.split("\\s+")).contains(word.toLowerCase()))
                {
                    return true;  // Return true if the word is found in the current file
                }
            }
             catch (IOException e) {
                e.printStackTrace();  // Handle exceptions, possibly log them or inform the user
            }
        }
      

        return  false;
    }
    
}
