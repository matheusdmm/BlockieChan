import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

/*
    Matheus - 2021



    Inspired by the work of CryptoKass at https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
    Made mostly to learn how a proto blockchain works

    You gonna need to have Gson and BouncyCastle to make the code work and compile properly.
*/

public class BlockChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String,TransactionOutput> UTXOs;

    static {
        UTXOs = new HashMap<>();
    }

    public static int difficulty = 5;
    public static float minimumTransaction = 0.1f;
    public static Wallet Movements;
    public static Wallet LaDispute;
    public static Transaction genesisTransaction;

    public static void main(String[] args) {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Movements = new Wallet();
        LaDispute = new Wallet();
        Wallet coinbase = new Wallet();

        genesisTransaction = new Transaction(coinbase.publicKey, Movements.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        Block block1 = new Block(genesis.hash);
        System.out.println("\nMovements balance is: " + Movements.getBalance());
        System.out.println("\nMovements is Attempting to send funds (65) to WalletB.");
        block1.addTransaction(Movements.sendFunds(LaDispute.publicKey, 65f));
        addBlock(block1);
        System.out.println("\nMovements balance is: " + Movements.getBalance());
        System.out.println("LaDispute balance is: " + LaDispute.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nMovements Attempting to send more funds (980) than it has.");
        block2.addTransaction(Movements.sendFunds(LaDispute.publicKey, 980f));
        addBlock(block2);
        System.out.println("\nMovements balance is: " + Movements.getBalance());
        System.out.println("LaDispute balance is: " + LaDispute.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nLaDispute is Attempting to send funds (20) to Movements.");
        block3.addTransaction(LaDispute.sendFunds( Movements.publicKey, 20));
        System.out.println("\nMovements balance is: " + Movements.getBalance());
        System.out.println("LaDispute balance is: " + LaDispute.getBalance());

        isChainValid();


    }

    public static void isChainValid() {

        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));


        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current hash isn't equal");
                return;
            }

            if (!previousBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current hash inst equal");
                return;
            }

            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return;
            }

            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (currentTransaction.verifySignature()) {
                    System.out.println("#Signature on transaction(" + t +") is invalid");
                    return;
                }

                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Inputs aren't equal to outputs on transaction(" + t + ")");
                    return;
                }

                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if (tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return;
                    }

                    tempUTXOs.remove(input.transactionOutputId);

                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return;
                }
                if( currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return;
                }

            }

        }
        System.out.println("Blockchain is valid");
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
