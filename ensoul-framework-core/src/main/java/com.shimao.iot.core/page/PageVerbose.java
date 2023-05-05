package com.shimao.iot.core.page;

import com.shimao.iot.core.sort.Order;
import com.shimao.iot.core.sort.SortOrderDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 分页冗余信息
 *
 * @author striver.cradle
 */
@Data
public class PageVerbose {

    @ApiModelProperty(value = "当前页码", position = 1)
    private Integer currentPage;

    @ApiModelProperty(value = "当前记录数", position = 2)
    private Integer currentSize;

    @ApiModelProperty(value = "是否为第一页", position = 3)
    private Boolean isFirst;

    @ApiModelProperty(value = "是否为最后一页", position = 4)
    private Boolean isLast;

    @ApiModelProperty(value = "是否有前一页", position = 5)
    private Boolean hasPrevious;

    @ApiModelProperty(value = "是否有下一页", position = 6)
    private Boolean hasNext;

    @ApiModelProperty(value = "总页数", position = 7)
    private Integer totalPage;

    @ApiModelProperty(value = "当前排序规则", position = 8)
    private List<SortOrderDTO> sort;

    @ApiModelProperty(value = "每页数量", position = 9)
    private Integer pageSize;

    private <E> PageVerbose(Page<E> page) {
        Pageable current = page.current();
        switch (current.verbose()) {
            case PAGE:
                setPage(page, current);
                break;
            case SORT:
                setSort(current);
                break;
            case ALL:
                setPage(page, current);
                setSort(current);
                break;
            default:
                throw new IllegalArgumentException("No verbose config");
        }
    }

    private <E> void setPage(Page<E> page, Pageable current) {
        this.currentPage = current.getPageNumber();
        this.currentSize = page.getContent().size();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.totalPage = page.getLastPageNumber();
        this.pageSize = current.getPageSize();
    }

    private void setSort(Pageable current) {
        if (current.getSort() != null) {
            Iterator<Order> iterator = current.getSort().iterator();
            List<SortOrderDTO> sortOrderDTOS = new ArrayList<>();
            while (iterator.hasNext()) {
                sortOrderDTOS.add(new SortOrderDTO(iterator.next()));
            }
            this.sort = sortOrderDTOS;
        }
    }

    public static <E> Optional<PageVerbose> of(Page<E> page) {
        if (page.current().verbose() == PageableVerbose.NONE) {
            return Optional.empty();
        }
        return Optional.of(new PageVerbose(page));
    }
}
