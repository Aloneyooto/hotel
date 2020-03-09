package com.alone.hotel.entity;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.entity
 * @Author: Alone
 * @CreateTime: 2020-03-09 08:42
 * @Description: 房间实体类
 */
@Data
public class Room {
    private Integer roomId;
    private String roomFloor;
    private String roomDesc;
    private Integer roomState;
    private Integer roomType;
    private Double roomPrice;

    private List<RoomImg> roomImgList;
}
