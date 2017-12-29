# AutographHttp

该工具包基于[微信签名规范](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3)开发，用于Android客户端网络请求签名认证。 <br>

### 签名算法
（[签名校验工具）](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=20_1)<br>


- 第一步，请求参数增加signTime、signStr，其中signTime为当前时间戳、signStr为16位随机字符串。<br>
- 第二步，将所有请求参数按照参数名ASCII码从小到大排序（字典序），再使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。<br>
- 第三步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。<br>

### 使用方法

## 初始化
	   AutographHttp.init(Context context, // 上下文环境，建议放在Application中初始化
						 String Api.KEY, // 私有密钥，
						 Url url, // 访问服务器的基地址，例如"http:www.baidu.com/"
						 boolean debug); // 默认false，如果传true，则打印日志（包括私有密钥），建议线上环境使用false

## 调用
	new AutographHttp.Builder()
				.changeHttp(String http) // 默认使用初始化时的基地址，如果传，则使用新的，仅在本次有效
                .with(String url) // 需要访问的地址，必传，例如"http:www.baidu.com/404/" "404/"这部分。如果传完整地址，则取完整地址
                .addParameter(Map map) // 参数集合，建议使用线程安全的ConcurrentHashMap 
				.add(String key,Object obj) // 单个参数键值对，与addParameter(map)使用有先后顺序，如果先使用add在使用appParamter则先使用的add无效
                .requestMethod(RequestMethod) // 请求方式，默认get，RequestMethod枚举类型有两个，GET和POST
				//.get() // get请求
				//.post() // post请求
				//.callbackOnUiThread(Activity activity) // 如果传则结果回调在ui线程
				//.alterKey(String key) // 修改私有密钥，修改只对本次有效，默认取初始化密钥
                .onResponse(new ResponseCallback() { // 访问成功回调 ，回调结果默认在子线程
                    @Override
                    public void onResponse(String s) {
                    }
                }).onFailure(new FailureCallback() {
            		@Override
            		public void onFailure(Call call, String s) { // 错误回调，回调结果默认在子线程
                		runOnUiThread(new Runnable() {
                   			@Override
                    		public void run() {
                       			new Dialog.Builder(Activity activity)
                                	.setMessage("网络错误，请检查后重试")
                                	.setPositive("知道了")
                                	.show();
                   			}
                		});
            		}
        		})
				.builder(); // 开始


## 导包
使用时复制jar包到libs文件夹即可<br>
网络请求工具包为okhttp3，请按okhttp3导包规范导包

## 备注
源码为modules工程，修改源码需要添加到工程的model列表中