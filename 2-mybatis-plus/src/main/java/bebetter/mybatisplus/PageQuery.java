package bebetter.mybatisplus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @date 2020-03-04
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 2706705106831384468L;
    /**
     * 分页参数 当前页
     */
    private Integer currentPage;

    /**
     * 分页参数 数量
     */
    private Integer pageSize;

    PageQuery() {
        this.currentPage = 1;
        this.pageSize = 10;
    }
}
