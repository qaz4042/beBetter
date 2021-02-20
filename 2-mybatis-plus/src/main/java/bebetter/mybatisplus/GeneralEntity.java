package bebetter.mybatisplus;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2020-01-01
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GeneralEntity implements Serializable {

    private static final long serialVersionUID = -198921906612591837L;
    
    @TableId
    private Long id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
