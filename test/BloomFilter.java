package test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class BloomFilter {
    private final int size;
    private final List<MessageDigest> digests;
    private final BitSet bits;

    public BloomFilter(int size, String... algorithms) {
        this.size = size;
        this.bits = new BitSet(size);
        this.digests = new ArrayList<>();

        for (String algorithm : algorithms) {
            try
             {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                digests.add(md);
            }
             catch (NoSuchAlgorithmException e) 
             {
                // Consider how you want to handle this error.
                // For now, we skip adding this digest.
                System.err.println("Unsupported algorithm provided: " + algorithm);
            }
        }
    }

    public void add(String data) {
        byte[] bytesOfData = data.getBytes(StandardCharsets.UTF_8);
        digests.forEach(digest -> {
            byte[] hashBytes = digest.digest(bytesOfData);
            BigInteger hashNumber = new BigInteger(1, hashBytes);
            int index = hashNumber.mod(BigInteger.valueOf(size)).intValue();
            bits.set(index);
        });
    }

    public boolean contains(String data) {
        byte[] bytesOfData = data.getBytes(StandardCharsets.UTF_8);
        return digests.stream().allMatch(digest -> {
            byte[] hashBytes = digest.digest(bytesOfData);
            BigInteger hashNumber = new BigInteger(1, hashBytes);
            int index = hashNumber.mod(BigInteger.valueOf(size)).intValue();
            return bits.get(index);
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(bits.get(i) ? '1' : '0');
        }
        return sb.toString();
    }
}



// package test;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.BitSet;
// import java.util.List;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.math.BigInteger;
// import java.nio.charset.StandardCharsets;
// import java.util.HexFormat;

// public class BloomFilter {
//     private final int size;
//     private final List<String> hashAlgs;
//     private final BitSet bits;

//     public BloomFilter(int len, String... algorithms) {
//         this.size = len;
//         this.hashAlgs = new ArrayList<>();
//         this.bits = new BitSet(size);
//         for (String alg : algorithms)
//         {
//             this.hashAlgs.add(alg);
//         }
//     }

//     public void add(String data) {
//         byte[] bytesOfData = data.getBytes(StandardCharsets.UTF_8);
//         System.out.println("Adding data: " + data);

//         for (String algorithm : hashAlgs) {
//             try {
//                 MessageDigest digest = MessageDigest.getInstance(algorithm);
//                 byte[] hashBytes = digest.digest(bytesOfData);

//                 // Use BigInteger to convert byte array into a non-negative integer
//                 BigInteger hashNumber = new BigInteger(1, hashBytes);

//                 // Calculate the index for the BitSet
//                 int index = hashNumber.mod(BigInteger.valueOf(size)).intValue();
             

//                 // Debug: print the algorithm and index
//                 System.out.println("Algorithm: " + algorithm + ", Index: " + index);

//                 // Set the bit at the computed index
               
//                 bits.set(index);
//             } 
//             catch (NoSuchAlgorithmException e) {
//                 throw new RuntimeException("Hash Algorithm " + algorithm + " not found", e);
//             }
//             System.out.println(bits);
//         }
//     }

//     public boolean contains(String data) {
//         byte[] bytesOfData = data.getBytes(StandardCharsets.UTF_8);

//         for (String algorithm : hashAlgs) {
//             try {
//                 MessageDigest digest = MessageDigest.getInstance(algorithm);
//                 byte[] hashBytes = digest.digest(bytesOfData);

//                 // Use BigInteger to convert byte array into a non-negative integer
//                 BigInteger hashNumber = new BigInteger(1, hashBytes);

//                 // Calculate the index for the BitSet
//                 int index = hashNumber.mod(BigInteger.valueOf(size)).intValue();

//                 // Check if the bit at the computed index is set
//                 if (!bits.get(index)) {
//                     return false; // If any bit is not set, the data is definitely not in the filter
//                 }
//             } catch (NoSuchAlgorithmException e) {
//                 throw new RuntimeException("Hash Algorithm " + algorithm + " not found", e);
//             }
//         }

//         // If all bits are set, the data might be in the filter
//         return true;
//     }
//     @Override
//     public String toString() {
//         StringBuilder sb = new StringBuilder();
//         for (int i = 0; i < size; i++) {
//             sb.append(bits.get(i) ? '1' : '0');
//         }
//         return sb.toString();
//     }
    
// }
