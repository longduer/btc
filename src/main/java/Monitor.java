import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;
import org.bitcoinj.store.MemoryBlockStore;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class Monitor {

    public static void main(String args[]) {

        NetworkParameters params = RegTestParams.get();

        // Now we initialize a new WalletAppKit. The kit handles all the boilerplate for us and is the easiest way to get everything up and running.
        // Have a look at the WalletAppKit documentation and its source to understand what's happening behind the scenes: https://github.com/bitcoinj/bitcoinj/blob/master/core/src/main/java/org/bitcoinj/kits/WalletAppKit.java
        WalletAppKit kit = new WalletAppKit(params, new File("."), "walletappkit-example");

        // In case you want to connect with your local bitcoind tell the kit to connect to localhost.
        // You must do that in reg test mode.
        kit.connectToLocalHost();

        // Now we start the kit and sync the blockchain.
        // bitcoinj is working a lot with the Google Guava libraries. The WalletAppKit extends the AbstractIdleService. Have a look at the introduction to Guava services: https://github.com/google/guava/wiki/ServiceExplained
        kit.startAsync();
        System.out.println("kit.startAsync();");
        kit.awaitRunning();
        System.out.println("kit.awaitRunning();");







    }
}
