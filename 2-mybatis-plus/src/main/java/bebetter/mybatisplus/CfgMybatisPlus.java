package bebetter.mybatisplus;

import bebetter.statics.constant.C;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

/**
 * @date 2020-04-05
 */
@MapperScan(C.SYS_PACKAGE + ".**.mapper")
public class CfgMybatisPlus {

    /**
     * mybatis-plus 分页插件，自动识别数据库类型
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        final MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //todo 攻击 SQL 阻断解析器,防止全表更新与删除
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // UpdateWrapper  LambdaUpdateWrapper .set() 自动填充 updateTime字段简单处理
        mybatisPlusInterceptor.addInnerInterceptor(new UpdateTimeInnerInterceptor());
        /*final StandardShardingHandler standardShardingHandler = new StandardShardingHandler() {
            @Override
            public List<StandardShardingMetaSource> getShardingMetaSources() {
                return Collections.singletonList(StandardShardingMetaSource.builder()
                        .tableName("adb_user_track_record")
                        .columns("id").build());
            }

            @Override
            public String sharding(String tableName, StandardShardingValue shardingValue) {
                System.out.printf("%s,%s%n",tableName, JSONUtil.toJsonStr(shardingValue));
                return null;
            }
        };
        mybatisPlusInterceptor.addInnerInterceptor(new StandardShardingInnerInterceptor(standardShardingHandler));*/
        return mybatisPlusInterceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    /**
     * mybatis-plus 公共字段自动填充
     */
    @Bean
    public GlobalConfig globalConfiguration(MetaObjectHandler metaObjectHandler) {
        GlobalConfig gc = new GlobalConfig();
        gc.setMetaObjectHandler(metaObjectHandler);
        return gc;
    }
}
