## brop-demo使用说明

### 写入存证数据
修改`WriteData.class`里面的下列参数
```
// 存证流水号，必填，每次写入都需要更新，存证流水号必须唯一，重复提交会失败
public static final String dataUnitId = "test-500000";
// 前序存证流水号，可为空
public static final String preDataUnitId = "";
// 存证hash    
public static final String existenceHash = "1ca6574c4dd8749538798af8ac9cb08412d5036f45b338062a1db4ef8fb5008a";
// 自定义字符串
public static final String customString = "";
```
执行main方法

### 读取存证数据
修改`ReadData.class`里面的下列参数
```
// 存证流水号，确保已经使用 WriteData 写入了这个存证数据
private static final String dataUnitId = "test-500000";
```
执行main方法