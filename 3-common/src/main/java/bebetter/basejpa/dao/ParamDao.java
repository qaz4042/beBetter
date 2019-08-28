package bebetter.basejpa.dao;

import bebetter.basejpa.enumsaver.IEnumSaverDao;
import bebetter.basejpa.model.db.Param;
import bebetter.jpa.base.BaseDao;

public interface ParamDao extends BaseDao<Param>, IEnumSaverDao<Param> {
}
