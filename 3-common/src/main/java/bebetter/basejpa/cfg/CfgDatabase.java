package bebetter.basejpa.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * dao层/事务 等配置.
 */
@Configuration
@EnableTransactionManagement
public class CfgDatabase {

}
//public class CfgDatabase extends RepositoryAd {
//
//    @Autowired
//    StringToOfferTypeConverter converter;
//
//    @Override
//    public void configureConversionService(ConfigurableConversionService conversionService) {
//        conversionService.addConverter(converter);
//        super.configureConversionService(conversionService);
//    }
//}
