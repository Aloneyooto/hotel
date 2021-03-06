package com.alone.hotel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.entity
 * @Author: Alone
 * @CreateTime: 2020-03-09 09:03
 * @Description: 员工
 */
@Data
public class Employee {
    private String employeeId;
    private String employeeName;
    private Integer employeeAge;
    private Integer employeeGender;
    private String employeeCardNumber;
    private String employeeCardImg;
    private String employeeFaceImg;
    private String employeePhone;
    private Position position;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date employeeCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date employeeLeaveTime;
}
