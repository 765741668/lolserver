# LOLServer java服务器netty版
个人喜欢竞技类游戏，能锻炼人的反应能力以及意识，更重要的是啪啪啪的敲代码速度也更快了。EQ：LOL PVP游戏，或者MMORPG 中的PVP也差不多。

# 技术点
1. jdk8、spi、aop、tcp/socket/websocket/http
2. netty4-final
3. protobuf3
4. memcahed
5. hibernate5、spring4
6. git、maven等，后续增加mybits、zookeeper、disconf、elk、redis、rpc等

# 已实现功能
- 账号、玩家的注册与登陆
- 匹配搜寻、选人、战斗数据流(移动、攻击、伤害、技能升级、英雄升级等)处理
- 心跳检测、断线重连等大体架构设计
- 匹配房间、选人房间管理器、战斗房间管理器的设计
- 使用protobuf序列化进行上下游数据交互
- 利用AOP织入memcached缓存进行热数据缓存(后续调整为分布式redis)
- hibernate+mysql或mybits+mysql进行数据本地存储...还有其他功能不一一赘述了,源码里有详细注释。

# 待续
还有很多功能及算法未严格的实现，比如
1. 段位系统未考虑
1. 伤害算法
1. NPC
1. 装备库 and so on...

最近一直没什么时间写这个了，有想研究netty的朋友可以forks and pull，后续有空再持续更新！

# 开源地址
- 中国开源社区地址(支持一把国人)：https://gitee.com/thekingoffighter765741668/lolserver/tree/yzh_netty4/
- 全球github开源地址(有空再放上去)：https://github.com/