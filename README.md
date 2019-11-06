## spring-boot-easyprofiler-starter
一个简单的统计springmvc web口的工具,默认情况下无须任何配置，只须添加maven依赖

### 第一步: 克隆项目到本地
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

### 第四步： 启动你web项目  访问统计页路径: /profiler,例如
```bash
http://localhost:8080/profiler
```

 会显示如下内容
 ![img](https://github.com/hexiangtao/configuration/blob/master/20191106133718.png)