import res.CommonResult;
import res.TransactionResult;
import utils.BROPUtils;

import java.util.Calendar;
import java.util.Map;

import static java.lang.Thread.sleep;

public class AddDataPerson {

    public static final String middleware = System.getProperty("middleware");
    public static final String dataUnitAddress = System.getProperty("dataUnitAddress");
    public static final String hsm = System.getProperty("hsm");
    public static final String hsmUserId = System.getProperty("hsmUserId");
    public static final String accountId = System.getProperty("accountId");

    public static void main(String[] args) throws InterruptedException {
        if (System.getProperty("middleware") != null
                && System.getProperty("dataUnitAddress") != null
                && System.getProperty("hsm") != null
                && System.getProperty("hsmUserId") != null
                && System.getProperty("accountId") != null) {
            System.out.println("middleware: " + System.getProperty("middleware") + ","
                             + "dataUnitAddress: " + System.getProperty("dataUnitAddress") + ","
                             + "hsm: " + System.getProperty("hsm") + ","
                             + "hsmUserId: " + System.getProperty("hsmUserId") + ","
                             + "accountId: " + System.getProperty("accountId"));
            final Integer blockLimit = BROPUtils.blockNumber(middleware) + 50;
            final String bizId = Calendar.getInstance().getTimeInMillis() + "";
            final String method = "addUserToWhiteList";
            final Object[] params = {accountId};
            //获取待签原文
            String deployInfo = BROPUtils.buildDeployInfo(middleware, blockLimit.toString(), bizId, method, params, dataUnitAddress);
            //签名
            String signInfo = BROPUtils.sign(hsm, hsmUserId, deployInfo);
            //发送交易
            String txHash = BROPUtils.sendTx(middleware, blockLimit.toString(), bizId, method, params, signInfo, dataUnitAddress);
            System.out.println(txHash);
            int i = 0;
            while (i < 10) {
                CommonResult<Map<String, TransactionResult>> commonResult = BROPUtils.getTxMessage(middleware, txHash);
                if (commonResult.getDetail().get("transaction") != null) {
                    System.out.println(commonResult.getDetail());
                    break;
                }
                sleep(3000);
                i++;
            }
            if (i >= 10) {
                System.out.println("白名单人员添加失败");
            } else {
                System.out.println("白名单人员添加成功");
            }
        } else {
            String expression = "输入参数错误，使用方法 \n" +
                    "加入白名单用户: java -Dmiddleware=[middleware] -DdataUnitAddress=[dataUnitAddress] -Dhsm=[hsm] -DhsmUserId=[hsmUserId] -DaccountId=[accountId] -jar brop-addWhiteList-1.0.0.jar\n" +
                    "  middleware                BROP服务地址, 如: http://127.0.0.1:9999\n" +
                    "  hsm                       加密机地址, 如: http://127.0.0.1:8090/hsmServer\n" +
                    "  dataUnitAddress           存证合约地址\n" +
                    "  hsmUserId                 加入白名单用户在加密机的id\n" +
                    "  accountId                 加入白名单人员id";
            System.out.println(expression);
        }
    }
}
