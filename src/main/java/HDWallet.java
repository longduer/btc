import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bouncycastle.util.encoders.Hex;
import party.loveit.bip44forjava.utils.Bip44Utils;

import java.math.BigInteger;
import java.util.List;

public class HDWallet {

    public static void main(String [] args) {
        //get 12 words
        List<String> words = null;
        try {
            words = Bip44Utils.generateMnemonicWords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("words: " + words.toString());

        // get bip39 seed
        byte[] seed = Bip44Utils.getSeed(words);
        System.out.println("seed: " + new BigInteger(1, seed).toString(16));

        //get PrivateKey by path
        BigInteger pri1 = Bip44Utils.getPathPrivateKey(words,"m/44'/0'/0'/0/0");
        System.out.println("pri1: " + pri1.toString(16));

        ECKey ecKey = ECKey.fromPrivate(pri1);

        NetworkParameters networkParameters = RegTestParams.get();
        String publicKey = ecKey.getPublicKeyAsHex();
        String privateKey = ecKey.getPrivateKeyEncoded(networkParameters).toString();
        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
        Address destination = LegacyAddress.fromBase58(MainNetParams.get(), Base58.encode(ecKey.getPubKey()));
        System.out.println(destination.getHash().toString());
    }
}
