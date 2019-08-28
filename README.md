# beBetter

## 框架定位
```
前端+后台,两套都为前后端分离代码
```

### 框架详情
```
1.前后端分离  ( vue&vueitfy + java/springboot系列)
2.已有 前端常用组件,后端极常用接口 可直接使用.例如 <zselect v-model="user.sex"/>;CommonController
3.代码生成,由实体(model)类生成,前端和后台2套代码,分别都有dao + sevice + controller + vue
4.各种框架组成
      ui框架: vue-cli3(vue/npm/nodejs项目) + vuetify
    java框架: springboot + spring-data-jpa(hibernate) + j2cache(redis+caffeine缓存框架)
5.框架的使用 详看 beBetter-demo
```


### 特点
```
1.对jpa进行简化封装,既保留扩展性,也添加便捷方法,例如简化sql拼接.
2.实体类的属性可以是对象,入库存为json (属性内容更不宜过多)
3.可以分布式应用下,user端隐藏部分字段,admin端全展示

