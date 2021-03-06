package com.alone.hotel.controller.superadmin;

import com.alibaba.fastjson.JSONObject;
import com.alone.hotel.dto.ImageExecution;
import com.alone.hotel.dto.RoomExecution;
import com.alone.hotel.entity.Room;
import com.alone.hotel.entity.RoomType;
import com.alone.hotel.enums.ResultEnum;
import com.alone.hotel.service.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.controller.superadmin
 * @Author: Alone
 * @CreateTime: 2020-03-09 21:22
 * @Description:
 */

@RestController
@RequestMapping("/superadmin")
public class RoomManagement {
    @Autowired
    private RoomService roomService;

    /**
     * 增加房间(formData提交)
     * @param roomStr json字符串
     * @param fileList 文件流
     * @return 成功或失败,失败返回失败信息
     */
    @PostMapping("/addroom")
    private RoomExecution addRoom(@RequestParam("roomStr")String roomStr,
                                  @RequestParam("fileList")MultipartFile[] fileList){
        //List<ImageExecution> imageList = new ArrayList<ImageExecution>();
        Room room = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            room = mapper.readValue(roomStr, Room.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(room != null && fileList != null && fileList.length > 0){
            try{
                RoomExecution roomExecution = roomService.addRoom(room, fileList);
                if(roomExecution.getState() == ResultEnum.SUCCESS.getState()){
                    return new RoomExecution(ResultEnum.SUCCESS);
                } else {
                    return new RoomExecution(ResultEnum.INNER_ERROR);
                }
            } catch (Exception e){
                return new RoomExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new RoomExecution(ResultEnum.EMPTY);
        }
    }

    /**
     * 根据房间号获取房间信息
     * @param roomId
     * @return
     */
    @GetMapping("/getroombyid")
    private RoomExecution getRoomById(@RequestParam("roomId")int roomId){
        RoomExecution roomExecution = null;
        if(roomId < -1){
            try{
                Room room = roomService.getRoomById(roomId);
                roomExecution = new RoomExecution(ResultEnum.SUCCESS, room);
            } catch (Exception e){
                roomExecution = new RoomExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            //房间号错误
            roomExecution = new RoomExecution(ResultEnum.ROOM_ID_ERROR);
        }
        return roomExecution;
    }

    /**
     * 获取房间列表(json传参)
     * @param pageIndex
     * @param pageSize
     * @param roomTypeId
     * @param roomState
     * @return
     */
    @GetMapping("/getroomlist")
    private RoomExecution getRoomList(int pageIndex, int pageSize, int roomTypeId, int roomState){
        if(pageIndex > -1 && pageSize > 0){
            Room roomCondition = new Room();
            if(roomTypeId > -1){
                RoomType roomType = new RoomType();
                roomType.setTypeId(roomTypeId);
                roomCondition.setRoomType(roomType);
            }
            if(roomState > -1){
                roomCondition.setRoomState(roomState);
            }
            RoomExecution re = roomService.getRoomList(roomCondition, pageIndex, pageSize);
            return re;
        } else {
            return new RoomExecution(ResultEnum.PAGE_ERROR);
        }
    }

    /**
     * 修改房间
     * @param roomStr
     * @param fileList
     * @return
     */
    @PostMapping("/updateroom")
    private RoomExecution updateRoom(@RequestParam("roomStr")String roomStr, @RequestParam("fileList")MultipartFile[] fileList){
        Room room = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            room = mapper.readValue(roomStr, Room.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(room != null){
            try{
                RoomExecution roomExecution = roomService.updateRoom(room, fileList);
                if(roomExecution.getState() == ResultEnum.SUCCESS.getState()){
                    return new RoomExecution(ResultEnum.SUCCESS);
                } else {
                    return new RoomExecution(ResultEnum.INNER_ERROR);
                }
            } catch (Exception e){
                return new RoomExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new RoomExecution(ResultEnum.EMPTY);
        }
    }

    @PostMapping("/deleteroom")
    private RoomExecution deleteRoom(@RequestParam("roomId")int roomId){
        if(roomId > 0){
            RoomExecution roomExecution = roomService.deleteRoom(roomId);
            return roomExecution;
        } else {
            return new RoomExecution(ResultEnum.ROOM_ID_ERROR);
        }
    }
}
