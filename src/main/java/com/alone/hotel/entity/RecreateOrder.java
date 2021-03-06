package com.alone.hotel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.entity
 * @Author: Alone
 * @CreateTime: 2020-03-09 09:08
 * @Description:
 */
@Data
public class RecreateOrder {
    private String orderId;
    private Customer customer;
    private Recreation recreation;
    private Double orderPrice;
    private Date startTime;
    private Date endTime;
    private Integer orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date handInTime;
}
