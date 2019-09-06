public class UnSpentUtxo2 {

    private String txid;//交易hash
    private int vout; //
    private String address;
    private String label;
    private String account;
    private String redeemScript;
    private String scriptPubKey;
    private double amount;
    private int confirmations;
    private boolean spendable;
    private boolean solvable;
    private boolean safe;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public int getVout() {
        return vout;
    }

    public void setVout(int vout) {
        this.vout = vout;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRedeemScript() {
        return redeemScript;
    }

    public void setRedeemScript(String redeemScript) {
        this.redeemScript = redeemScript;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public boolean isSpendable() {
        return spendable;
    }

    public void setSpendable(boolean spendable) {
        this.spendable = spendable;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public void setSolvable(boolean solvable) {
        this.solvable = solvable;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    @Override
    public String toString() {
        return "UnSpentUtxo2{" +
                "txid='" + txid + '\'' +
                ", vout=" + vout +
                ", address='" + address + '\'' +
                ", label='" + label + '\'' +
                ", account='" + account + '\'' +
                ", redeemScript='" + redeemScript + '\'' +
                ", scriptPubKey='" + scriptPubKey + '\'' +
                ", amount=" + amount +
                ", confirmations=" + confirmations +
                ", spendable=" + spendable +
                ", solvable=" + solvable +
                ", safe=" + safe +
                '}';
    }
}
