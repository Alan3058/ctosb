# ctosb-core
ctosb-core 集成了Mybatis，包含自定义分页Page插件、Limit插件，支持排序Sort组件，支持mysql、mariadb、sqlserver、oracle等数据库。
## 拦截器
LimitInterceptor  
只支持拦截Limit，返回一个范围的数据。Deprecated

PageInterceptor  
支持Limit、Page、Sort，自动返回某个范围的数据或者分页数据。

## 组件
Limit  
查询范围数据组件

Page  
查询分页数据组件

Sort  
定义排序规则组件

PageList
分页集合  

PageInfo  
分页信息集合，与PageList不一样，其中的分页信息支持json序列化
