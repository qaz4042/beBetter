package bebetter.statics.model;

import java.io.Serializable;

public interface IUserId<ID extends Serializable, UID extends Serializable> extends Serializable{
    ID getId();

    void setId(ID id);

    UID getUserId();

    void setUserId(UID userId);
}
