# spike
# A spike project from rammelzzz.

## 项目完成的流程与工作

### 一、项目框架搭建
1.Spring Boot环境搭建

2.集成Thymeleaf，Result结果封装

3.集成Mybatis+Druid(alibaba的数据库连接池)

4.集成Jedis(Redis的Java客户端)+通用缓存Key封装

### 二、实现登录功能
1.数据库设计

2.明文密码两次MD5设计<pre>详见com.imooc.spike.util.MD5Util</pre>

3.JSR303参数校验、全局异常处理器<pre>参考com.imooc.exception和com.imooc.spike.validate包下的内容</pre>

4.分布式Session(将用户信息缓存到Redis服务器中)

### 三、实现秒杀功能
1.数据库设计

2.商品列表页

3.商品详情页

4.订单详情页

### 四、页面优化
1.页面缓存+URL缓存+对象缓存

2.页面静态化，前后端分离

3.静态资源优化

4.CDN优化

### 五、接口优化
1.Redis预减库存减少数据库访问

2.内存标记减少Redis访问

3.RabbitMQ集成Spring Boot

4.RabbitMQ队列缓冲，异步下单，增强用户体验
<pre>参考com.imooc.spike.rabbitmq</pre>

5.Nginx水平扩展

### 六、安全优化
1.秒杀地址隐藏

2.数学公式验证码(未完成)

3.接口限流防刷




