package bebetter.basejpa.dao;

import bebetter.basejpa.enumsaver.IEnumSaverDao;
import bebetter.basejpa.model.db.Param;
import bebetter.mybatisplus.base.BaseDao;

public interface ParamDao extends BaseDao<Param>, IEnumSaverDao<Param> {
}
