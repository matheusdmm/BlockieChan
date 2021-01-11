import java.util.Date;
import java.util.ArrayList;

public class Block {

    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public long timeStamp;
    public int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        merkleRoot
        );
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    public void addTransaction (Transaction transaction) {
        if (transaction == null) return;
        if ((!previousHash.equals("0"))) {
            if ((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Discarted.");
                return;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction successfully added to block.");
    }


}
