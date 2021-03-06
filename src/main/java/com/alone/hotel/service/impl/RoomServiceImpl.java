package com.alone.hotel.service.impl;

import com.alone.hotel.dao.RoomDao;
import com.alone.hotel.dao.RoomImgDao;
import com.alone.hotel.dto.ImageExecution;
import com.alone.hotel.dto.RoomExecution;
import com.alone.hotel.entity.Room;
import com.alone.hotel.entity.RoomImg;
import com.alone.hotel.enums.ResultEnum;
import com.alone.hotel.enums.ResultEnum;
import com.alone.hotel.enums.RoomStateEnum;
import com.alone.hotel.exceptions.RoomException;
import com.alone.hotel.service.RoomService;
import com.alone.hotel.utils.ImageUtil;
import com.alone.hotel.utils.PageUtil;
import com.alone.hotel.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.service.impl
 * @Author: Alone
 * @CreateTime: 2020-03-09 19:31
 * @Description:
 */
@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomImgDao roomImgDao;

    /**
     * 添加房间
     * @param room
     * @param files
     * @return
     */
    @Override
    @Transactional
    public RoomExecution addRoom(Room room, MultipartFile[] files) {
        //向tb_room写入房间信息,获取roomId
        //结合roomId批量处理房间详情图
        //将房间详情图列表批量插入tb_room_img中

        //空值判断
        if(room != null){
            //设置房间默认状态为空房间
            room.setRoomState(RoomStateEnum.EMPTY.getState());
            try{
                //插入房间信息
                int effectNum = roomDao.insertRoom(room);
                if(effectNum <= 0){
                    throw new RoomException(ResultEnum.ROOM_INSERT_ERROR);
                }
            } catch (Exception e){
                throw new RoomException(ResultEnum.ROOM_INSERT_ERROR);
            }
            //若房间详情图不为空则添加
            if(files != null && files.length > 0){
                addRoomImgList(room, files);
                return new RoomExecution(ResultEnum.SUCCESS, room);
            } else {
                return new RoomExecution(ResultEnum.EMPTY);
            }
        } else {
            return new RoomExecution(ResultEnum.EMPTY);
        }
    }

    /**
     * 根据房间号查询房间信息
     * @param roomId
     * @return
     */
    public Room getRoomById(int roomId){
        return roomDao.queryRoomById(roomId);
    }

    @Override
    public RoomExecution getRoomList(Room roomCondition, int pageIndex, int pageSize) {
        //页数转换成数据库的行数
        int rowIndex = PageUtil.calculateRowIndex(pageIndex, pageSize);
        //查询所需记录
        List<Room> roomList = roomDao.queryRoomList(roomCondition, rowIndex, pageSize);
        //记录的总行数
        int count = roomDao.queryRoomCount(roomCondition);
        RoomExecution re = null;
        if(roomList != null){
            re = new RoomExecution();
            re.setRoomList(roomList);
            re.setCount(count);
        }
        return re;
    }

    /**
     * 修改房间
     * @param room
     * @param files
     * @return
     */
    @Override
    @Transactional
    public RoomExecution updateRoom(Room room, MultipartFile[] files) {
        if(room != null){
            //更新图片
            if(files != null){
                deleteRoomImgList(room.getRoomId());
                addRoomImgList(room, files);
            }
            try{
                int effectedNum = roomDao.updateRoom(room);
                if(effectedNum <= 0){
                    throw new RoomException(ResultEnum.ROOM_UPDATE_ERROR);
                }
                return new RoomExecution(ResultEnum.SUCCESS, room);
            } catch (Exception e){
                throw new RoomException(ResultEnum.ROOM_UPDATE_ERROR);
            }
        } else {
            return new RoomExecution(ResultEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public RoomExecution deleteRoom(int roomId) {
        try {
            if(roomId > 0){
                int effectedNum = roomImgDao.deleteRoomImgByRoomId(roomId);
                if(effectedNum < 0){
                    throw new RoomException(ResultEnum.ROOM_IMG_DELETE_ERROR);
                }
                int result = roomDao.deleteRoom(roomId);
                if(result <= 0){
                    throw new RoomException(ResultEnum.ROOM_DELETE_ERROR);
                } else {
                    return new RoomExecution(ResultEnum.SUCCESS);
                }
            }
        } catch (Exception e){
            throw new RoomException(ResultEnum.ROOM_DELETE_ERROR);
        }
        return new RoomExecution(ResultEnum.INNER_ERROR);
    }

    private void addRoomImgList(Room room, MultipartFile[] files){
        List<RoomImg> roomImgList = new ArrayList<RoomImg>();
        //获取图片存储路径
        String dest = PathUtil.getRoomImagePath(room.getRoomId());
        //遍历图片
        for (MultipartFile file : files) {
            try {
                String imgAddr = ImageUtil.uploadImage(file, dest);
                RoomImg roomImg = new RoomImg();
                roomImg.setRoomImgAddr(imgAddr);
                roomImg.setRoomId(room.getRoomId());
                roomImgList.add(roomImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(roomImgList.size() > 0){
            try {
                int effectedNum = roomImgDao.batchInsertRoomImg(roomImgList);
                room.setRoomImgList(roomImgList);
                if(effectedNum <= 0){
                    throw new RoomException(ResultEnum.ROOM_INSERT_ERROR);
                }
            } catch (Exception e){
                throw new RoomException(ResultEnum.ROOM_INSERT_ERROR);
            }
        }
    }

    private void deleteRoomImgList(int roomId){
        //根据roomId获取原来的图片
        List<RoomImg> roomImgList = roomImgDao.queryRoomImgList(roomId);
        //干掉原来的照片
        for (RoomImg roomImg : roomImgList) {
            ImageUtil.deleteFileOrPath(roomImg.getRoomImgAddr());
        }
        roomImgDao.deleteRoomImgByRoomId(roomId);
    }
}
