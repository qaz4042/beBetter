package bebetter.basejpa.model.base;

import bebetter.basejpa.model.base.ext.UserType_Json;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@MappedSuperclass
@DynamicInsert//dao.save()时,如果属性为null,则不做修改
@TypeDef(name = "json", typeClass = UserType_Json.class)
//@TypeDef
public abstract class Id_CreateTime implements Serializable {

    public static final String field_id = "id";
    public static final String field_createTime = "createTime";

    /**
     * id
     */
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "custom-id")
//    @GenericGenerator(name = "custom-id", strategy = "com.muyuer.springdemo.core.CustomIDGenerator")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    protected LocalDateTime createTime;
}
