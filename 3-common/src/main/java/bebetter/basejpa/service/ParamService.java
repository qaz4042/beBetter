package bebetter.basejpa.service;

import bebetter.basejpa.dao.ParamDao;
import bebetter.basejpa.constant.CacheNamesBase;
import bebetter.basejpa.model.db.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class ParamService {
    @Autowired
    ParamDao paramDao;

    @Cacheable(CacheNamesBase.param)
    public Param getByCodeCache(String code) {//id 即 code
        return paramDao.getByCode(code);
    }
}
