package com.alone.hotel.dao;

import com.alone.hotel.entity.Recreation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.dao
 * @Author: Alone
 * @CreateTime: 2020-03-17 19:48
 * @Description:
 */
public interface RecreationDao {
    /**
     * 添加其他娱乐项目
     * @param recreation
     * @return
     */
    int addRecreation(Recreation recreation);

    /**
     * 根据娱乐项目号查询对应娱乐项目信息
     * @param recreationId
     * @return
     */
    Recreation queryRecreationById(int recreationId);

    /**
     * 根据查询条件查询对应娱乐项目信息
     * @param recreationCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<Recreation> queryRecreationList(@Param("recreationCondition") Recreation recreationCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 娱乐项目信息总数
     * @return
     */
    int queryRecreationCount(@Param("recreationCondition") Recreation recreationCondition);

    /**
     * 修改娱乐项目信息
     * @param recreation
     * @return
     */
    int updateRecreation(Recreation recreation);

    /**
     * 删除娱乐项目信息
     * @param recreationId
     * @return
     */
    int deleteRecreation(int recreationId);
}
