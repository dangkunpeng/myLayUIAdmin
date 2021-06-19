package dang.kp.manager.common.utils;

import dang.kp.manager.common.response.PageDataResult;
import org.springframework.data.domain.Page;

import java.util.Objects;

public class PageUtils {

    public static void defaultPage(Integer pageNum, Integer pageSize) {

        if (Objects.isNull(pageNum)) {
            pageNum = 1;
        }
        pageNum -= 1;
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
    }

    public static PageDataResult getPage(Page<?> pageResult) {
        return PageDataResult.builder()
                .list(pageResult.getContent())
                .totals(Math.toIntExact(pageResult.getTotalElements()))
                .code(200)
                .build();
    }
}
