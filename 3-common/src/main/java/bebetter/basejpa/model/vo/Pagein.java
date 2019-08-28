package bebetter.basejpa.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Pagein implements Pageable {
    int pageNumber = 1;
    int pageSize = 10;
    Sort sort;//{sort:{orders:[{direction;'ASC/DESC', property:'createTime'}]}}

    //vue传入
    int curr = 1; //=page = pageNumber 当前第几页
    int size = 10;//=pagesize 一页几条
    String[] sortProps;
    String[] sortDesc;

    public Pagein(int pageNumber, int pageSize, Sort sort) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public void setCurr(Integer curr) {
        this.curr = curr;
        this.pageNumber = curr;
    }

    public void setSize(Integer size) {
        this.size = size;
        this.pageSize = size;
    }

    public Sort getSort() {
        List<Sort.Order> orders = new ArrayList<>(4);
        if (null != sortProps) {
            for (int i = 0; i < sortProps.length; i++) {
                orders.add(new Sort.Order("true".equals(sortDesc[i]) ? Sort.Direction.DESC : Sort.Direction.ASC, sortProps[i]));
            }
        }
        return new Sort(orders);
    }

    @Override
    public long getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    @Override
    public Pageable next() {
        return new Pagein(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new Pagein(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    public Pageable previous() {
        return getPageNumber() == 0 ? this : new Pagein(getPageNumber() - 1, getPageSize(), getSort());
    }
}
