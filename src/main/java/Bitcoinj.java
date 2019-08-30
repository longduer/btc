import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;

public class Bitcoinj {
    public static void main(String[] args) {
        String network = "regtest";
        NetworkParameters params;
        String filePrefix;
        if (network.equals("testnet")) {
            params = TestNet3Params.get();
            filePrefix = "forwarding-service-testnet";
        } else if (network.equals("regtest")) {
            params = RegTestParams.get();
            filePrefix = "forwarding-service-regtest";
        } else {
            params = MainNetParams.get();
            filePrefix = "forwarding-service";
        }
        System.out.println("filePrefix: " + filePrefix);
        System.out.println("params: " + params.getId());

        // Parse the address given as the first parameter.
    }
}
