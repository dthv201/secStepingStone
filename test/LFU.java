package test;


public class LFU implements CacheReplacementPolicy{
    private final int capacity;
    private final LinkedHashSet<String> cache;
    @Override
    public void add(String word) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public String remove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
    
}
