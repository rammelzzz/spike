页面优化策略：
1.页面缓存+URL缓存+对象缓存
2.页面静态化，前后端分离
3.静态资源优化
4.CDN优化

静态资源优化：
1.JS/CSS压缩，减少流量
2.多个JS/CSS组合，减少连接数 //The Tengine <a>tengine.taobao.org</a>
//打包前端资源 webpack.github.io
3.cdn 内容分发网络 Content Delivery Network

接口优化：
1.Redis预减库存减少数据库访问
2.内存标记减少Redis访问
3.请求先入队缓冲，异步下单，增强用户体验
4.RabbitMQ安装与Spring Boot集成
5.Nginx水平扩展
6.压测
//7.数据库分库分表 mycat alibaba开发的数据库分库分表中间件

秒杀接口优化思路：减少数据库访问
1.系统初始化，把商品库存数量加载到Redis
2.收到请求，Redis预减库存，库存不足，直接返回，否则进入3
3.请求入队，立即返回排队中
4.请求出队，生成订单，减少库存
5.客户端轮询，是否秒杀成功

rabbitmq的四種交換機模式
1.Direct模式 only one queue
2.Topic模式 主題訂閱模式
3.Fanout模式 廣播模式
4.Header模式 消息入隊必須帶上一些Headers

安全優化
1.秒殺接口地址隱藏
2.數學公式驗證碼
3.接口限流防刷

秒殺開始之前，先去請求接口獲取秒殺地址
1.接口改造，帶上PathVariable參數
2.添加生成地址的接口
3.秒殺收到請求，先驗證PathVariable