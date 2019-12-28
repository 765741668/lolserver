# LOLServer java服务器netty版
个人喜欢竞技类游戏，能锻炼人的反应能力以及意识，业余时间写的一个单机版PVP服务器，练手netty4与netty5。

# 技术点
1. jdk8、spi、tcp/socket/websocket/http
2. netty4-final
3. protobuf3
4. memcahed/redis
5. hibernate5/mybatis、spring4
6. git、maven等，后续增加mybits、zookeeper、disconf、elk、redis、rpc等

# 已实现功能
- 账号、玩家的注册与登陆
- 匹配搜寻、选人、战斗数据流(移动、攻击、伤害、技能升级、英雄升级等)处理
- 心跳检测、断线重连等大体架构设计
- 匹配房间、选人房间管理器、战斗房间管理器的设计
- 使用protobuf序列化进行上下游数据交互
- AOP织入memcached缓存进行热数据缓存(后续调整为分布式redis)
- hibernate+mysql或mybits+mysql进行数据本地存储...还有其他功能不一一赘述了,源码里有详细注释。

# 待续
还有很多功能及算法未严格的实现，比如
1. 段位系统 未实现
2. 伤害算法
3. NPC
4. 装备库 and so on...
5. 有时间准备改vert.x架构..

# 性能测试
本工程包含有java写的一个jemeter插件，有空再去完善了。

最近一直没什么时间写这个了，有想研究netty的朋友可以forks and pull，后续有空再持续更新！

# 开源地址
- https://gitee.com/thekingoffighter765741668/lolserver/tree/yzh_netty4/
- https://github.com/765741668/lolserver/tree/yzh_netty4
