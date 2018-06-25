# ModelSearch
毕业设计，三维模型搜索

# 软件中的问题说明
## 两个服务端的问题
- 因为对接了两个服务端,所以网络接口有两个版本.[服务端1](http://www.3dhawkeye.com/),[服务端2](http://47.89.178.150/v3_2/index_2.model).
- SearchReqBean是服务端1的**请求**字段,Result是服务端1的**响应**字段.ResultBean是服务端2的**响应**字段
- ResultAdapter是用于服务端1的展示,DownloaderAdapter是用于服务端2的展示
- configure类中也是包含了两个服务器的字段
## ResultActivity功能太多,耦合严重
- 搜索历史记录功能和结果展示功能在同一个Activity中,仅仅通过view的gone,Visible,inVisible实现,是设计缺陷
- ResultActivity采用搜索框+一个fragment的设计.搜索历史记录功能用一个fragment,展示界面用一个fragment,之后就是两个fragment之间的切换
- 搜索框布局应该提取出来,可以重用.
## 运行时权限问题
- 在6.0版本手机上需要运行时权限,在代码中需要进行权限请求判断**自行百度运行时权限**

## 如果需要数据库
- 推荐objectbox,不要采用原生的sqlite数据库

#其他说明
## 网络httpUtil类
- okhttp实现
- 请学习Java中的**回调**,然后网络方法这个类中的网络方法就很简单了