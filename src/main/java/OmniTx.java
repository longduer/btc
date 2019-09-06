public class OmniTx {

    private String txid;
    private double fee;
    private String sendingaddress;
    private String referenceaddress;
    private boolean ismine;
    private int version;
    private int type_int;

    private String type;
    private String propertyid;
    private boolean divisible;
    private String amount;
    private boolean valid;
    private String blockhash;
    private long blocktime;
    private int positioninblock;
    private int confirmations;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getSendingaddress() {
        return sendingaddress;
    }

    public void setSendingaddress(String sendingaddress) {
        this.sendingaddress = sendingaddress;
    }

    public String getReferenceaddress() {
        return referenceaddress;
    }

    public void setReferenceaddress(String referenceaddress) {
        this.referenceaddress = referenceaddress;
    }

    public boolean isIsmine() {
        return ismine;
    }

    public void setIsmine(boolean ismine) {
        this.ismine = ismine;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType_int() {
        return type_int;
    }

    public void setType_int(int type_int) {
        this.type_int = type_int;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(String propertyid) {
        this.propertyid = propertyid;
    }

    public boolean isDivisible() {
        return divisible;
    }

    public void setDivisible(boolean divisible) {
        this.divisible = divisible;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public long getBlocktime() {
        return blocktime;
    }

    public void setBlocktime(long blocktime) {
        this.blocktime = blocktime;
    }

    public int getPositioninblock() {
        return positioninblock;
    }

    public void setPositioninblock(int positioninblock) {
        this.positioninblock = positioninblock;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    @Override
    public String toString() {
        return "OmniTx{" +
                "txid='" + txid + '\'' +
                ", fee=" + fee +
                ", sendingaddress='" + sendingaddress + '\'' +
                ", referenceaddress='" + referenceaddress + '\'' +
                ", ismine=" + ismine +
                ", version=" + version +
                ", type_int=" + type_int +
                ", type='" + type + '\'' +
                ", propertyid='" + propertyid + '\'' +
                ", divisible=" + divisible +
                ", amount='" + amount + '\'' +
                ", valid=" + valid +
                ", blockhash='" + blockhash + '\'' +
                ", blocktime=" + blocktime +
                ", positioninblock=" + positioninblock +
                ", confirmations=" + confirmations +
                '}';
    }
}
