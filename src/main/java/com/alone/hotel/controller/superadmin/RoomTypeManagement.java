package com.alone.hotel.controller.superadmin;

import com.alone.hotel.dto.RoomTypeExecution;
import com.alone.hotel.entity.RoomType;
import com.alone.hotel.enums.RoomTypeStateEnum;
import com.alone.hotel.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.controller.superadmin
 * @Author: Alone
 * @CreateTime: 2020-03-30 10:19
 * @Description:
 */
@CrossOrigin
@RestController
@RequestMapping("/superadmin")
public class RoomTypeManagement {
    @Autowired
    private RoomTypeService roomTypeService;

    @PostMapping("/addroomtype")
    private RoomTypeExecution addRoomType(@RequestParam("roomType") RoomType roomType){
        if(roomType != null){
            return roomTypeService.addRoomType(roomType);
        } else {
            return new RoomTypeExecution(RoomTypeStateEnum.EMPTY);
        }
    }

    @GetMapping("/queryroomtypebyid")
    private RoomTypeExecution queryRoomTypeById(@RequestParam("roomTypeId") int roomTypeId){
        if(roomTypeId > 0){
            try {
                RoomType roomType = roomTypeService.queryRoomTypeById(roomTypeId);
                return new RoomTypeExecution(RoomTypeStateEnum.SUCCESS, roomType);
            } catch (Exception e){
                return new RoomTypeExecution(RoomTypeStateEnum.INNER_ERROR);
            }
        } else {
            return new RoomTypeExecution(RoomTypeStateEnum.EMPTY);
        }
    }

    @GetMapping("/queryroomtype")
    private RoomTypeExecution queryRoomType(){
        try {
            List<RoomType> roomTypeList = roomTypeService.queryRoomType();
            return new RoomTypeExecution(RoomTypeStateEnum.SUCCESS, roomTypeList);
        } catch (Exception e){
            return new RoomTypeExecution(RoomTypeStateEnum.INNER_ERROR);
        }
    }

    @PostMapping("/updateroomtype")
    private RoomTypeExecution updateRoomType(@RequestParam("roomType") RoomType roomType){
        if(roomType != null){
            return roomTypeService.updateRoomType(roomType);
        } else {
            return new RoomTypeExecution(RoomTypeStateEnum.EMPTY);
        }
    }

    @PostMapping("/deleteroomtype")
    private RoomTypeExecution deleteRoomType(@RequestParam("roomTypeId") int roomTypeId){
        if(roomTypeId > 0){
            return roomTypeService.deleteRoomType(roomTypeId);
        } else {
            return new RoomTypeExecution(RoomTypeStateEnum.EMPTY);
        }
    }
}
