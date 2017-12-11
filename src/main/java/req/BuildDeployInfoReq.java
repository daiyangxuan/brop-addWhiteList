package req;

public class BuildDeployInfoReq {

    String blockLimit;
    String bizId;
    String contractAddress;
    String method;
    Object[] params;

    public String getBlockLimit() {
        return blockLimit;
    }

    public void setBlockLimit(String blockLimit) {
        this.blockLimit = blockLimit;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
