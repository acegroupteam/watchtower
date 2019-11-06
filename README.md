## spring-boot-easyprofiler-starter

###### 一个方便，轻量级，无侵入的springboot项目web接口访问监控插件,默认情况下无须任何配置，只须添加maven依赖


### 第一步: 由于还没上传到中央仓库，需要克隆项目到本地
```bash
 git clone https://github.com/hexiangtao/spring-boot-easyprofiler-starter.git
```

### 第二步: 构建项目，部署到本地仓库
```bash
mvn  install
```  


### 第三步，在你的项目添加如下依赖
```xml
<dependency>
    <groupId>io.github.easy-profiler</groupId>
    <artifactId>spring-boot-easy-profiler-starter</artifactId>
    <version>1.0.1.RELEASE</version>
</dependency>

```

### 第四步： 启动你web项目  访问统计页路径: /easy-profiler.例如
```bash
http://localhost:8080/easy-profiler
```
>  tip:第一次访问会要求输入用户名密码，默认用户密码是profiler,profiler,该值可以在配置文件里覆盖

会看到接口每次调用的统计数据,例如:
![img](https://github.com/hexiangtao/configuration/blob/master/20191106133718.png)


### 统计项：

```bash
路径	失败数	成功数	总数	平均ms	最大ms	最小ms	最大ms时间点	累计成功数	累计失败数	累计调用数	历史平均ms	历史最小ms	历史最大ms	历史最大ms时间点
```


### 自定义配置说明:
  
在spring-boot项目的配置文件(例如:application-dev.yml)里键入eas,会有如下提示:
  
![img](https://github.com/hexiangtao/configuration/blob/master/20191106135024.png)

#### 完整的配置项如下:
```yaml
easy-profiler:
  enabled: false
  password: test
  username: test
  enable-basic: true
  base-package: com.example
  exclude-class: BizException

```

#### 配置项说明:
  1.  enabled:  是否启用，默认为true,如果不想启用，设置为false
  2.  username: 统计页登陆名  
  3.  password: 统计页密码
  4.  enable-basic: 访问统计页是否需要授权，默认true,如果希望任何人都能访问，设置为false
  5.  base-package:  controller所在包路径，可选
  6.  exclude-class: 忽略异常类.例如某些业务异常不想被统计到失败数里面，则可以在这里配置
 
 #### 注:以上参数都是可选配置
 

#### 欢迎star,使用过程中如有任何疑问，欢迎联系本人
 ##### email:xiangtaohe@gmail.com
 ##### qq:798395170




