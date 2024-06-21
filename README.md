工程实践2-企业文件管理系统-后端部分
下载代码之后记得在yml文件中修改自己的数据库信息
祝大家写的开心

5.28 搭建项目框架，分层架构，一次性导入了大量依赖

5.29 调试项目，调整各个依赖的版本，弃用springboot3.x版本，采用springboot2.7.7，基本解决了版本冲突问题，springboot项目能正常运行  
    设计数据表，对各个字段名进行了标准化，导出sql语句并push到仓库

5.30 开启mybatisplus驼峰映射，修改entity包中各个bean对象的字段名，将其修改为驼峰模式，解决了数据库和bean类之间的字段映射问题  
     在登录模块中使用jwt令牌作为token返回到前端，其中又出现了版本问题，使用最新的jwtwebtoken依赖会出现不可预知的错误，因此最终决定使用0.9.1的jwt版本，因为jwt在此项目中仅作为token使用，因此使用老版本来保证jwt令牌生成和解析稳定可用即可  
     完成了图形验证码，后端返回图形的base64和验证码的ID，将图片上的验证码按照ID保存在redis中，设置有效期为十分钟(但是好像还没有考虑验证码的刷新，交给前端吧）

5.31 完成了获取图片验证码，发送邮箱验证码，注册等逻辑，删减了几个冗余类  
     优化了数据表，抽象出了一个bucket，每个用户在注册账号时系统为其分配一个私有的bucket，之后用户的所有文件操作都在其中进行，bucket的实质还是一个文件夹  
     管理员删除用户即先删除用户的bucket再删除用户信息  
     最后测试了下管理员新建共享bucket的接口  

6.1 编写了Interceptor结构，具体实现还没写  
    测试了管理员创建共享桶的接口确认无误  
    最后把目前的进度打包了个jar包在target文件里，大家有兴趣可以测试下  

6.2 添加 Dockerfile 与 Docker Compose 文件,数据库名改为entbase（之前叫enbase），添加了SSH密钥用于持续部署  
    添加了CorsConfig用于允许跨域请求，修改了data的存储路径为相对路径  

6.3 写了管理员修改和删除共享桶操作，没来得及测试  

6.4 测试了昨天写的接口，发现在修改bucket名称后bucket_path没修改，解决了该bug  
    突然想起来没有配置redis的连接工厂和序列化方式，加上了 

6.5  
6.6  
6.7 复习计体考试，没有代码  

6.8 摆烂  
6.9 写了共享文件的接口，注意桶ID指的是路径而不是数据库中的bucketId  

6.10-6.17 复习概率论/计组/数值分析

6.18 优化了共享文件的接口，在redis中的hash结构为 共享ID-密码-文件路径，只有当共享ID和密码都正确时才能获取文件路径，得到共享文件的二进制  
     没有实现的是对于整个目录的共享，因为目录中有多个文件（夹），如果全部以二进制返回暂时没想好怎么区分  

6.19 决定优化了共享文件的存储方式，在数据库中存储而不是在redis中，因此不用考虑redis的数据持久化了     

6.20 设计了share表来存储共享文件的信息，因此重构了共享文件的接口，新增了查看用户所有共享文件的接口。直接跑会报错，因为拦截器还没开启，UserHolder里面没有数据，要测试的话得自己伪造user信息

6.21 新增查看用户所有存储桶的接口，新增登录拦截器，但由于接口还没有全部push，所有没有配置拦截路径

    
