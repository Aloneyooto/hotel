package com.alone.hotel.dto;

import com.alone.hotel.entity.Customer;
import com.alone.hotel.enums.ResultEnum;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.dto
 * @Author: Alone
 * @CreateTime: 2020-03-23 20:15
 * @Description:
 */
@Data
public class CustomerExecution {
    //结果状态
    private int state;
    //状态标识
    private String stateInfo;
    //商品数量
    private int count;
    //操作的顾客
    private Customer customer;
    //操作的顾客列表
    private List<Customer> customerList;

    public CustomerExecution() {
    }

    public CustomerExecution(ResultEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public CustomerExecution(ResultEnum stateEnum, Customer customer){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.customer = customer;
    }

    public CustomerExecution(ResultEnum stateEnum, List<Customer> customerList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.customerList = customerList;
    }
}
