### logback-miner

[![Build Status](https://travis-ci.org/CharLemAznable/logback-miner.svg?branch=master)](https://travis-ci.org/CharLemAznable/logback-miner)
[![codecov](https://codecov.io/gh/CharLemAznable/logback-miner/branch/master/graph/badge.svg)](https://codecov.io/gh/CharLemAznable/logback-miner)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.charlemaznable/logback-miner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.charlemaznable/logback-miner/)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
![GitHub code size](https://img.shields.io/github/languages/code-size/CharLemAznable/logback-miner)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=alert_status)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=bugs)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=security_rating)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=sqale_index)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=code_smells)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=ncloc)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=coverage)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=CharLemAznable_logback-miner&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=CharLemAznable_logback-miner)

自定义扩展Logback-Classic.

##### Maven Dependency

```xml
<dependency>
  <groupId>com.github.charlemaznable</groupId>
  <artifactId>logback-miner</artifactId>
  <version>0.1.0</version>
</dependency>
```

##### Maven Dependency SNAPSHOT

```xml
<dependency>
  <groupId>com.github.charlemaznable</groupId>
  <artifactId>logback-miner</artifactId>
  <version>0.1.1-SNAPSHOT</version>
</dependency>
```

#### 目标功能

1. 热更新日志配置
2. 简化日志配置, 将xml格式替换为properties格式, 并省略常用默认配置
3. 日志默认配置, 使用异步日志队列
4. 特定类型的日志参数Bean映射插入数据库日志表
5. 通过配置Vert.x集群, 将日志事件转化为EventBus消息, 便于将来统一日志服务

#### 快速开始

1. 本地类路径添加配置文件```logback-miner.properties```:

```
root[level]=info    # 配置根级别logger日志级别为info, 默认为debug, "level"关键字和日志级别不区分大小写
```

2. 配置日志additive属性

```
{logger-name}[additivity]={true/yes/on/y为真值, 其他为假值}  # "additivity"关键字和配置值不区分大小写
```

3. 配置控制台输出

```
{logger-name}[console.level]=debug
{logger-name}[console.charset]=utf-8
{logger-name}[console.pattern]=%date [%20.20thread] %5level %50.50logger{50}\\(%4.4line\\): %message%n
{logger-name}[console.target]=System.out
{logger-name}[console.immediateFlush]={true/yes/on/y为真值, 其他为假值}
```

以上配置中:

  * "console.XXX"关键字不区分大小写
  * 日志级别不区分大小写, 覆盖当前级别日志的```[level]```
  * 控制台日志级别未设置时, 优先使用当前级别日志的```[level]```, 若未设置```[level]```, 则使用父级日志的控制台日志级别
  * 字符编码可选值参见```java.nio.charset.Charset```
  * 日志输出格式参见```ch.qos.logback.core.pattern.PatternLayoutEncoderBase```
  * 日志输出目标可选值为: ```System.out```或```System.err```

配置以上任一项, 即启动对应级别logger日志控制台输出

仅需启动日志控制台输出, 且仅使用默认配置, 则需添加配置: 

```
{logger-name}[console]  # 启动对应级别logger日志控制台输出, 使用默认配置, "console"关键字不区分大小写
```

4. 配置数据库日志

```
{logger-name}[dql.level]=debug
{logger-name}[dql.connection]=
{logger-name}[dql.sql]=
```

以上配置中:

  * "dql.XXX"关键字不区分大小写
  * 日志级别不区分大小写, 覆盖当前级别日志的```[level]```
  * 数据库日志级别未设置时, 优先使用当前级别日志的```[level]```, 若未设置```[level]```, 则使用父级日志的数据库日志级别
  * dql.connection配置默认使用的Dql连接配置名, 即```new Dql("XXX")```中的```"XXX"```
  * dql.sql配置默认使用的日志插入SQL语句, 可选参数参见```com.github.charlemaznable.logback.miner.appender.LoggingEventElf```, 如: ```event.message```, ```mdc.XXX```, ```property.XXX```, 等

配置以上任一项, 即启动对应级别logger数据库日志

仅需启动数据库日志, 且仅使用默认配置, 则需添加配置: 

```
{logger-name}[dql]  # 启动对应级别logger数据库日志, 使用默认配置, "dql"关键字不区分大小写
```

数据库插入参数规则:

  * 如参数中不包含标注```@LogbackBean```类型的对象, 则使用默认连接默认插入SQL, 可选参数为```event.message```, ```mdc.XXX```, ```property.XXX```, 等
  * 如参数中包含标注```@LogbackBean```类型的对象, 则遍历所有此类参数进行数据库插入, 除上述可选参数外, 另可使用```arg.字段名```作为参数
  * 可填写```@LogbackBean```注解的```value```, 覆盖日志配置的默认的dql连接配置
  * 日志参数Bean默认插入的日志表名为类名的下划线格式, e.g. ```class TestLog```插入表```table TEST_LOG```, 可使用```@LogbackTable```注解另行指定
  * 日志参数Bean默认插入的日志字段为类型声明的非静态字段, 列名为字段名的下划线格式, 可使用```@LogbackColumn```注解另行指定, 或使用```@LogbackSkip```注解指定排除
  * 可使用```@LogbackSql```注解另行指定插入日志的```sqlFile```和```sqlId```, 默认为当前类型对应的```sqlFile```中的名为```[log{类名}]```的SQL语句

5. 配置Vert.x日志

```
{logger-name}[vertx.level]=debug
{logger-name}[vertx.name]=
{logger-name}[vertx.address]={logger-name}
```

以上配置中:

  * "vertx.XXX"关键字不区分大小写
  * 日志级别不区分大小写, 覆盖当前级别日志的```[level]```
  * Vert.x日志级别未设置时, 优先使用当前级别日志的```[level]```, 若未设置```[level]```, 则使用父级日志的Vert.x日志级别
  * vertx.name配置Vert.x实例标识, 如果存在```diamond group:VertxOptions dataId:[vertx.name]```配置, 则自动加载并初始化Vert.x实例用于发送日志事件消息
  * 可使用```VertxManager#putExternalVertx```方法配置自定义的Vert.x实例, 需自行控制自定义Vert.x实例的生命周期, 使用自定义的名称作为vertx.name配置
  * vertx.address配置日志事件消息发送的地址topic, 默认为logger-name
  * 日志事件消息的接收端处理器类型为```io.vertx.core.Handler<io.vertx.core.eventbus.Message<io.vertx.core.json.JsonObject>>```, 其中JsonObject包含```event```, ```mdc```和```property```三个子JsonObject

配置以上任一项, 即启动对应级别logger Vert.x日志

仅需启动Vert.x日志, 且仅使用默认配置, 则需添加配置: 

```
{logger-name}[vertx]  # 启动对应级别logger数据库日志, 使用默认配置, "vertx"关键字不区分大小写
```

6. 配置日志Context

```
context.packagingDataEnabled=false              # 配置logback LoggerContext
context.maxCallerDataDepth=8
context.frameworkPackages=

context.property[property-name]=property-value  # 配置上下文属性参数, 可用于日志输出参数
```

7. 配置热更新

```
logback.miner.group=Logback
logback.miner.dataId=default
```

启动时, 将自动读取```diamond group:Logback dataId:default```配置内容为Properties格式, 覆盖本地配置文件```logback-miner.properties```中的同名配置
在```diamond group:Logback dataId:default```内容更新后, 将自动更新日志配置, 但仍以本地配置文件```logback-miner.properties```中的配置作为默认配置
