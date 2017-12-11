package utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import req.BuildDeployInfoReq;
import req.ContractReq;
import req.SendTxReq;
import res.CommonResult;
import res.TransactionResult;

import java.util.Map;

public class BROPUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取当前块号
     *
     * @return
     */
    public static Integer blockNumber(String middleware) {
        CommonResult commonResult = new CommonResult();
        String blockNumber = null;
        Integer currentBlockNumber = -1;
        try {
            blockNumber = HttpClientSevice.get(middleware + "/common/blockNumber.json");
            commonResult = JSON.parseObject(blockNumber, new com.alibaba.fastjson.TypeReference<CommonResult>() {
            });
            currentBlockNumber = (Integer) commonResult.getDetail();
        } catch (Exception e) {
            System.out.println(e);
        }
        return currentBlockNumber;
    }

    /**
     * 获取待签原文
     *
     * @param blockLimit
     * @param bizId
     * @param method
     * @param params
     * @return
     */
    public static String buildDeployInfo(String middleware,
                                         String blockLimit,
                                         String bizId,
                                         String method,
                                         Object[] params,
                                         String dataUnitAddress) {
        BuildDeployInfoReq buildDeployInfoReq = getBuildDeployInfoReq(blockLimit, bizId, method, params, dataUnitAddress);
        CommonResult commonResult = new CommonResult();
        String deployInfo = null;
        String result = null;
        try {
            result = HttpClientSevice.postJsonObject(middleware + "/common/buildSendInfo.json", buildDeployInfoReq);
            commonResult = mapper.readValue(result, new TypeReference<CommonResult<String>>() {
            });
            deployInfo = (String) commonResult.getDetail();
        } catch (Exception e) {
            System.out.println(e);
        }
        return deployInfo;
    }

    /**
     * 签名
     *
     * @param signReq
     * @return
     */
    public static String sign(String hsmServer, String hsmUserId, String signReq) {
        String result = null;
        String signInfo = null;
        CommonResult commonResult = new CommonResult();
        try {
            result = HttpClientSevice.get(hsmServer + "/crypt/sign.json?userId=" + hsmUserId + "&messageHash=" + signReq);
            commonResult = mapper.readValue(result, new TypeReference<CommonResult<String>>() {
            });
            signInfo = (String) commonResult.getDetail();
        } catch (Exception e) {
            System.out.println(e);
        }
        return signInfo;
    }

    /**
     * 发送交易
     *
     * @param blockLimit
     * @param bizId
     * @param method
     * @param params
     * @param signatureInBase64
     * @return
     */
    public static String sendTx(String middleware,
                                String blockLimit,
                                String bizId,
                                String method,
                                Object[] params,
                                String signatureInBase64,
                                String dataUnitAddress) {
        SendTxReq sendTxReq = getSendTxReq(blockLimit, bizId, method, params, signatureInBase64, dataUnitAddress);
        String result = null;
        String txHash = null;
        CommonResult commonResult = new CommonResult();
        try {
            result = HttpClientSevice.postJsonObject(middleware + "/common/sendTx.json", sendTxReq);
            commonResult = mapper.readValue(result, new TypeReference<CommonResult<String>>() {
            });
            txHash = (String) commonResult.getDetail();
        } catch (Exception e) {
            System.out.println(e);
        }
        return txHash;
    }

    /**
     * 根据交易hash获取交易信息
     *
     * @param txHash
     * @return
     */
    public static CommonResult getTxMessage(String middleware, String txHash) {
        String result = null;
        CommonResult commonResult = new CommonResult();
        try {
            result = HttpClientSevice.get(middleware + "/transaction/" + txHash + ".json");
            commonResult = mapper.readValue(result, new TypeReference<CommonResult<Map<String, TransactionResult>>>() {
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        return commonResult;
    }

    /**
     * 只读接口获取信息
     * @param contractAddress
     * @param functionName
     * @param params
     * @return
     */
    public static CommonResult contract(String middleware,
                                        String contractAddress,
                                        String functionName,
                                        Object[] params) {
        ContractReq contractReq = getContractReq(contractAddress, functionName, params);
        String result = null;
        CommonResult commonResult = new CommonResult();
        try {
            result = HttpClientSevice.postJsonObject(middleware + "/common/contract.json", contractReq);
            commonResult = mapper.readValue(result, new TypeReference<CommonResult<Map<String, Object>>>() {
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        return commonResult;
    }

    private static ContractReq getContractReq(String contractAddress, String functionName, Object[] params) {
        ContractReq contractReq = new ContractReq();
        contractReq.setContractAddress(contractAddress);
        contractReq.setFunctionName(functionName);
        contractReq.setParams(params);
        return contractReq;
    }

    private static SendTxReq getSendTxReq(String blockLimit, String bizId, String method, Object[] params, String signatureInBase64, String dataUnitAddress) {
        SendTxReq sendTxReq = new SendTxReq();
        sendTxReq.setBlockLimit(blockLimit);
        sendTxReq.setBizId(bizId);
        sendTxReq.setContractAddress(dataUnitAddress);
        sendTxReq.setMethod(method);
        sendTxReq.setParams(params);
        sendTxReq.setSignatureInBase64(signatureInBase64);
        return sendTxReq;
    }

    private static BuildDeployInfoReq getBuildDeployInfoReq(String blockLimit, String bizId, String method, Object[] params, String dataUnitAddress) {
        BuildDeployInfoReq buildDeployInfoReq = new BuildDeployInfoReq();
        buildDeployInfoReq.setBlockLimit(blockLimit);
        buildDeployInfoReq.setBizId(bizId);
        buildDeployInfoReq.setContractAddress(dataUnitAddress);
        buildDeployInfoReq.setMethod(method);
        buildDeployInfoReq.setParams(params);
        return buildDeployInfoReq;
    }
}
