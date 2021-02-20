package bebetter.demo.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * implements IEnum<Integer>
 * @author LiZuBin
 * @date 2021/2/20
 */
@AllArgsConstructor
@Getter
public enum SexEnum {
    /***/
    MAN("男",0),
    WOMAN("女",1),
    ;
    String label;
    @EnumValue
    Integer value;
}
