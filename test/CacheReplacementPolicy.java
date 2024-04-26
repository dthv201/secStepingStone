package test;

public interface CacheReplacementPolicy{
	Object counts = null;
    void add(String word);
	String remove(); 
}
