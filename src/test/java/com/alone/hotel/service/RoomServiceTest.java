package com.alone.hotel.service;

import com.alone.hotel.dto.ImageExecution;
import com.alone.hotel.dto.RoomExecution;
import com.alone.hotel.entity.Room;
import com.alone.hotel.enums.RoomStateEnum;
import com.alone.hotel.utils.ImageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.service
 * @Author: Alone
 * @CreateTime: 2020-03-09 20:39
 * @Description:
 */
@SpringBootTest
class RoomServiceTest {
    @Autowired
    private RoomService roomService;

    @Test
    public void testInsertRoom() throws IOException {
        Room room = new Room();
        room.setRoomId(102);
        room.setRoomDesc("test");
        room.setRoomType(1);
        room.setRoomFloor("1层");
        room.setRoomPrice(105.2);
        File file = new File("E:\\proresources\\images\\latestbg.jpg");
        InputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        ImageExecution image = new ImageExecution(multipartFile, file.getName());
        List<ImageExecution> list = new ArrayList<ImageExecution>();
        list.add(image);
//        RoomExecution roomExecution = roomService.addRoom(room, list);
//        assertEquals(RoomStateEnum.SUCCESS.getState(), roomExecution.getState());
    }
}