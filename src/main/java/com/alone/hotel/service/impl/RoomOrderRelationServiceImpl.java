package com.alone.hotel.service.impl;

import com.alone.hotel.dao.RoomOrderRelationDao;
import com.alone.hotel.entity.RoomOrder;
import com.alone.hotel.entity.RoomOrderRelation;
import com.alone.hotel.enums.OrderStateEnum;
import com.alone.hotel.exceptions.OrderException;
import com.alone.hotel.service.RoomOrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.service.impl
 * @Author: Alone
 * @CreateTime: 2020-03-30 16:19
 * @Description:
 */
@Service
public class RoomOrderRelationServiceImpl implements RoomOrderRelationService {
    @Autowired
    private RoomOrderRelationDao roomOrderRelationDao;

    @Override
    @Transactional
    public Boolean addRoomOrderRelation(RoomOrderRelation roomOrderRelation) {
        if(roomOrderRelation != null && roomOrderRelation.getCustomer() != null && roomOrderRelation.getRoomOrder() != null){
            try {
                int effectNum = roomOrderRelationDao.addRoomOrderRelation(roomOrderRelation);
                if(effectNum <= 0){
                    return false;
                }
                return true;
            } catch (Exception e){
                throw new OrderException(OrderStateEnum.RELATION_INSERT_ERROR.getStateInfo());
            }
        } else {
            return false;
        }
    }

    @Override
    public RoomOrder queryCustomerByOrderId(String orderId) {
        return roomOrderRelationDao.queryCustomerByOrderId(orderId);
    }

    @Override
    @Transactional
    public Boolean deleteRoomOrderRelation(String roomOrderId) {
        if(roomOrderId != null){
            try {
                int effectNum = roomOrderRelationDao.deleteRoomOrderRelation(roomOrderId);
                if(effectNum <= 0){
                    return false;
                }
                return true;
            } catch (Exception e){
                throw new OrderException(OrderStateEnum.RELATION_DELETE_ERROR.getStateInfo());
            }
        } else {
            return false;
        }
    }
}