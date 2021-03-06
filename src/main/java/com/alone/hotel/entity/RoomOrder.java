package com.alone.hotel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.entity
 * @Author: Alone
 * @CreateTime: 2020-03-09 09:15
 * @Description: 房间订单
 */
@Data
public class RoomOrder {
    private String orderId;
    private CustomerAccount account;
    private RoomType roomType;
    private Integer roomAmount;
    private Double orderPrice;
    private Date startTime;
    private Date endTime;
    private Integer orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date handInTime;

    private List<Room> roomList;
    private List<Customer> customerList;
}
