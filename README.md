## Dynamic for LabsCare
### 主要用于公司遇到的需求, 不知道用不用的上, 写出来也算对自己以前的学习有一部分总结.

使用方式:

先获取项目到本地, 然后 maven install.

需要引用如下依赖:
```bash
<dependency>
    <groupId>org.dynamic.datasource</groupId>
    <artifactId>dynamic-datasource-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <scope>provided</scope>
    <optional>true</optional>
    <version>2.7.9</version>
</dependency>
```

然后在自己工程启动类中添加 @EnableDynamic 注解

配置文件参考:
```bash
custom:
  dynamic:
    dataSourceConfig:
      url: jdbc:mysql://localhost:13306/labscare_base_huaxi?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&useSSL=false  #查询配置租户得数据库
      username: root  #数据库用户名
      password: mysql  #数据库密码
      driverClassName: com.mysql.jdbc.Driver  #数据库驱动
    defaultDataSourceName: coreDataSource  #需要重写得数据源(MySQL)
    defaultMongoSourceName: mongoTemplate  #需要重写得数据源(MongoDB)
    aspect:
      expression: execution (* com.labscare.statistics.controller..*.*(..))  #需要拦截得接口, 更新数据源
    tableInfo:
      sql: SELECT * FROM labscare_base_huaxi.t_company  #MySQL租户查询语句
      id: id  #对应租户id
      driverClassNameColumn: driver  #驱动
      urlColumn: url  #路径
      usernameColumn: dataUserName  #用户名
      passwordColumn: dataPassword  #密码
    mongoInfo:
      sql: SELECT * FROM labscare_base_huaxi.t_company  #Mongo租户查询语句
      id: id  #对应租户id
      databaseColumn: mongoProDatabase  #对应数据库
      hostColumn: mongoHost  #地址
      portColumn: mongoPort  #端口
      usernameColumn: mongoUserName  #验证用户名
      passwordColumn: mongoPassword  #验证密码
      authDataBaseColumn: mongoAuthDatabase  #验证数据库
    idInfo:
      queryIdClass: com.labscare.statistics.util.ServletUtil  #用于获取租户id得类
      method: getCompanyId  #用于获取租户id得方法
```

动态数据源, 目前适配MySQL、MongoDB, 主要用于公司得复制实验室功能, 所以很多地方做了公司项目得适配.

1、考虑MongoDB和MySQL租户分开

    问题: 定时任务、怎么控制两个的开关
    目前方案: 过滤器链

2、初始化MongoDB、数据库多租户能不能改成代理方式

    问题: 怎么代理, 还要区分他原来的数据源. 代理完还要重新注入到IOC中, 怎么销毁和重新注入
    目前方案: 代理

3、现在获取Spring Context有点问题, 里面的bean不多.

    目前方案: 多尝试几种初始化Context方法

4、脚本, 毕竟是做给自己公司用于复制实验室的. 数据库复制的脚本, 替换companyId脚本还要处理

5、这个工程配置量感觉有点太多了, 得想办法优化

6、新复制实验室后怎么动态加入到已经存在得多数据源, 不然每次都要重启?

7、考虑数据库、MongoDB配置优化, 且支持的连接属性多样化.

    目前方案: 多数据库连接, 读取统一属性配置文件
