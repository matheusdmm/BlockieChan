import java.util.ArrayList;
import com.google.gson.GsonBuilder;

/*
    Inspired by the work of CryptoKass at https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
    Made mostly to learn how a proto blockchain works

    You gonna need to have Gson to make the code work properly.



    Matheus - 2021
*/

public class BlockChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String[] args) {

        blockchain.add(new Block("Hi im the first block", "0"));
        blockchain.add(new Block("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
        blockchain.add(new Block("Hey im the third block",blockchain.get(blockchain.size()-1).hash));

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);

    }

    public static Boolean isChainValid() {

        Block currentBlock;
        Block previousBlock;

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculatedHash())) {
                System.out.println("Current hash isnt equal");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.calculatedHash())) {
                System.out.println("Current hash ");
                return false;
            }
        }
        return true;
    }
}
