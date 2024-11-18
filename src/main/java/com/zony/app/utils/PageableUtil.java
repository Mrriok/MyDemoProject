/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.utils;

import com.qiniu.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/9/27 -17:23 
 */
public class PageableUtil {
    public static final String ASC_ORDER= "ASC";
    public static final String DESC_ORDER = "DESC";
    /**
     * 根据传入参数，创建排序分页器
     * @param pageNum
     * @param pageSize
     * @param sortKey 按此字段排序.eg:id/createTime等。为null时，默认按创建时间（createTime）排序
     * @param order 升序：PageableUtil.ASC_ORDER；降序：PageableUtil.DESC_ORDER。为null时，默认降序
     * @return
     */
    public static Pageable getPageable(int pageNum, int pageSize, String sortKey, String order){
        String realSortKey = "id";   //默认排序关键字
        if(!(StringUtils.isNullOrEmpty(sortKey))){
            realSortKey = sortKey;
        }

        Sort sort = null;
        if("DESC".equalsIgnoreCase(order)||StringUtils.isNullOrEmpty(order)){
            sort = new Sort(Sort.Direction.DESC, realSortKey);  //降序
        }else if("ASC".equalsIgnoreCase(order)){
            sort = new Sort(Sort.Direction.ASC, realSortKey);   //升序
        }else {
            sort = new Sort(Sort.Direction.DESC, realSortKey);
        }

        return PageRequest.of(pageNum,pageSize,sort);
    }

}
