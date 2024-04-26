package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class BloomFilter {
    private final int size;
    private final List<String> hashAlgs;
    private final BitSet bits;

    public BloomFilter(int len, String... algorithms) {
        this.size = len;
        this.hashAlgs = new ArrayList<>();
        this.bits = new BitSet(size);
        for (String alg : algorithms) {
            this.hashAlgs.add(alg);
        }
    }

    public void add(String data) {
        byte[] bytesOfData = data.getBytes(StandardCharsets.UTF_8);

        for (String algorithm : hashAlgs) {
            try {
                MessageDigest digest = MessageDigest.getInstance(algorithm);
                byte[] hashBytes = digest.digest(bytesOfData);

                // Use BigInteger to convert byte array into a non-negative integer
                BigInteger hashNumber = new BigInteger(1, hashBytes);

                // Calculate the index for the BitSet
                int index = hashNumber.mod(BigInteger.valueOf(size)).intValue();

                // Set the bit at the computed index
                bits.set(index);
            } 
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Hash Algorithm " + algorithm + " not found", e);
            }
        }
    }

    public boolean contains(String data) {
        byte[] bytesOfData = data.getBytes(StandardCharsets.UTF_8);

        for (String algorithm : hashAlgs) {
            try {
                MessageDigest digest = MessageDigest.getInstance(algorithm);
                byte[] hashBytes = digest.digest(bytesOfData);

                // Use BigInteger to convert byte array into a non-negative integer
                BigInteger hashNumber = new BigInteger(1, hashBytes);

                // Calculate the index for the BitSet
                int index = hashNumber.mod(BigInteger.valueOf(size)).intValue();

                // Check if the bit at the computed index is set
                if (!bits.get(index)) {
                    return false; // If any bit is not set, the data is definitely not in the filter
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Hash Algorithm " + algorithm + " not found", e);
            }
        }

        // If all bits are set, the data might be in the filter
        return true;
    }
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = bits.nextSetBit(0); i >= 0; i = bits.nextSetBit(i+1)) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            sb.append(i);
        }
        sb.append('}');
        return sb.toString();
    }

}
