package com.alone.hotel.controller.customer;

import com.alibaba.fastjson.JSONArray;
import com.alone.hotel.annotation.UserLoginToken;
import com.alone.hotel.dto.CustomerAccountExecution;
import com.alone.hotel.dto.CustomerExecution;
import com.alone.hotel.dto.CustomerRelationExecution;
import com.alone.hotel.dto.RoomExecution;
import com.alone.hotel.entity.CheckIn;
import com.alone.hotel.entity.Customer;
import com.alone.hotel.entity.CustomerAccount;
import com.alone.hotel.entity.CustomerRelation;
import com.alone.hotel.enums.*;
import com.alone.hotel.service.CheckInService;
import com.alone.hotel.service.CustomerAccountService;
import com.alone.hotel.service.CustomerRelationService;
import com.alone.hotel.service.CustomerService;
import com.alone.hotel.utils.FaceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.controller.customer
 * @Author: Alone
 * @CreateTime: 2020-03-24 09:08
 * @Description:
 */
@RestController
@RequestMapping("/customer")
public class CustomerManagement {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private CustomerRelationService customerRelationService;

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增加顾客信息
     * @Param request
     * @return
     */
    @UserLoginToken
    @PostMapping("/addcustomermessage")
    private CustomerExecution addCustomerMessage(HttpServletRequest request){
        //Customer customer, MultipartFile cardImg, MultipartFile faceImg, CustomerAccount customerAccount, int flag
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        String customerCardNumber = params.getParameter("customerCardNumber");
        //保证身份证号不为空
        if(customerCardNumber != null && customerCardNumber.length() == 18){
            Customer customer = new Customer();
            int customerAge = 0;
            int customerGender = 0;
            String customerPhone = null;
            MultipartFile cardImg = params.getFile("cardImg");
            MultipartFile faceImg = params.getFile("faceImg");
            String customerName = params.getParameter("customerName");
            if(params.getParameter("customerAge") != null){
                customerAge = Integer.parseInt(params.getParameter("customerAge"));
            }
            if(params.getParameter("customerGender") != null){
                customerGender = Integer.parseInt(params.getParameter("customerGender"));
            }
            if(params.getParameter("customerPhone") != null){
                customerPhone = params.getParameter("customerPhone");
            }

            //获取token
            String token = params.getHeader("token");
            CustomerAccount customerAccount = null;
            if(token != null){
                //获取customerAccount对象
                String jsonStr = (String)redisTemplate.opsForValue().get(token);
                customerAccount = JSONArray.parseObject(jsonStr, CustomerAccount.class);
            }

            //添加顾客信息
            try{
                customer.setCustomerCardNumber(customerCardNumber);
                customer.setCustomerName(customerName);
                customer.setCustomerAge(customerAge);
                customer.setCustomerGender(customerGender);
                customer.setCustomerPhone(customerPhone);

                CustomerExecution customerExecution = customerService.addCustomer(customer, cardImg, faceImg);
                if(customerExecution.getState() != ResultEnum.SUCCESS.getState()){
                    return customerExecution;
                }
            } catch (Exception e){
                return new CustomerExecution(ResultEnum.INNER_ERROR);
            }
            //向customer_relation表添加关系
            CustomerRelation customerRelation = new CustomerRelation();
            customerRelation.setAccount(customerAccount);
            customerRelation.setCustomer(customer);
            CustomerRelationExecution customerRelationExecution = customerRelationService.addCustomerRelation(customerRelation);
            if(customerRelationExecution.getState() == ResultEnum.SUCCESS.getState()){
                return new CustomerExecution(ResultEnum.SUCCESS, customer);
            } else {
                return new CustomerExecution(ResultEnum.RELATION_INSERT_ERROR);
            }
        } else {
            return new CustomerExecution(ResultEnum.EMPTY);
        }
    }

    /**
     * 列出该账号存放的顾客信息
     * @param request
     * @return
     */
    @UserLoginToken
    @GetMapping("/querycustomerbyaccount")
    private CustomerExecution queryCustomerByAccount(HttpServletRequest request){
        try {
            //获取token
            String token = request.getHeader("token");
            //获取customerAccount对象
            String jsonStr = (String)redisTemplate.opsForValue().get(token);
            CustomerAccount customerAccount = JSONArray.parseObject(jsonStr, CustomerAccount.class);
            if(customerAccount != null){
                CustomerRelation customerRelation = new CustomerRelation();
                customerRelation.setAccount(customerAccount);
                CustomerAccount account = customerRelationService.queryCustomerByAccount(customerRelation);
                return new CustomerExecution(ResultEnum.SUCCESS, account.getCustomerList());
            } else {
                return new CustomerExecution(ResultEnum.ACCOUNT_EMPTY);
            }
        } catch (Exception e){
            return new CustomerExecution(ResultEnum.INNER_ERROR);
        }
    }

    /**
     * 修改顾客信息
     * @param request
     * @return
     */
    @UserLoginToken
    @PostMapping("/updatecustomermessage")
    private CustomerExecution updateCustomerMessage(HttpServletRequest request){
//        @RequestParam("customer") Customer customer, @RequestParam("cardImg") MultipartFile cardImg, @RequestParam("faceImg") MultipartFile faceImg
//        判断顾客信息是否为空
        MultipartHttpServletRequest params = (MultipartHttpServletRequest) request;
        String customerCardNumber = params.getParameter("customerCardNumber");
        Customer customer = new Customer();
        MultipartFile cardImg = params.getFile("cardImg");
        MultipartFile faceImg = params.getFile("faceImg");
        if(customerCardNumber != null && customerCardNumber.length() == 18){
            customer.setCustomerCardNumber(customerCardNumber);
            int customerAge = 0;
            int customerGender = 0;
            String customerPhone = null;
            String customerName = null;
            if(params.getParameter("customerName") != null){
                customerName = params.getParameter("customerName");
                customer.setCustomerName(customerName);
            }
            if(params.getParameter("customerAge") != null){
                customerAge = Integer.parseInt(params.getParameter("customerAge"));
                customer.setCustomerAge(customerAge);
            }
            if(params.getParameter("customerGender") != null){
                customerGender = Integer.parseInt(params.getParameter("customerGender"));
                customer.setCustomerGender(customerGender);
            }
            if(params.getParameter("customerPhone") != null){
                customerPhone = params.getParameter("customerPhone");
                customer.setCustomerPhone(customerPhone);
            }
        }
        if(customer != null){
            try{
                CustomerExecution customerExecution = customerService.updateCustomer(customer, cardImg, faceImg);
                if(customerExecution.getState() == ResultEnum.SUCCESS.getState()){
                    return customerExecution;
                } else {
                    return new CustomerExecution(ResultEnum.INNER_ERROR);
                }
            } catch (Exception e){
                  return new CustomerExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new CustomerExecution(ResultEnum.EMPTY);
        }
    }

    /**
     * 删除顾客信息
     * @param request
     * @return
     */
    @UserLoginToken
    @PostMapping("/deletecustomermessage")
    private CustomerExecution deleteCustomerMessage(HttpServletRequest request){
        //String accountName, String customerCardNumber

        //获取token
        String token = request.getHeader("token");
        CustomerAccount customerAccount = null;
        if(token != null){
            //获取customerAccount对象
            String jsonStr = (String)redisTemplate.opsForValue().get(token);
            customerAccount = JSONArray.parseObject(jsonStr, CustomerAccount.class);
        }

        String customerCardNumber = request.getParameter("customerCardNumber");

        if(customerCardNumber != null){
            //删除customerrelation里的对应内容
            CustomerRelation customerRelation = new CustomerRelation();
            Customer customer = new Customer();
            customer.setCustomerCardNumber(customerCardNumber);
            customerRelation.setAccount(customerAccount);
            customerRelation.setCustomer(customer);
            try {
                CustomerRelationExecution customerRelationExecution = customerRelationService.deleteCustomerRelation(customerRelation);
                if(customerRelationExecution.getState() != ResultEnum.SUCCESS.getState()){
                    return new CustomerExecution(ResultEnum.RELATION_DELETE_ERROR);
                }
            } catch (Exception e){
                return new CustomerExecution(ResultEnum.RELATION_DELETE_ERROR);
            }
            //删除customer的记录
            CustomerExecution customerExecution = customerService.deleteCustomer(customer.getCustomerCardNumber());
            if(customerExecution.getState() == ResultEnum.SUCCESS.getState()){
                return customerExecution;
            } else {
                return new CustomerExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new CustomerExecution(ResultEnum.EMPTY);
        }
    }

    /**
     * 人脸识别
     * @param faceFile
     * @param roomId
     */
    @UserLoginToken
    @GetMapping("/compareFaces")
    private RoomExecution compareFaces(@RequestParam MultipartFile faceFile, @RequestParam Integer roomId){
        //初始化引擎
        FaceUtil.initEngine();
        //查询数据库内已有的人脸
        List<Customer> customerList = customerService.queryCustomerFaceImages();
        //生成人脸特征信息
        FaceUtil.getCustomerFeature(customerList);
        try {
            File newFile = FaceUtil.multipartFileToFile(faceFile);
            String customerCardNumber = FaceUtil.compareFaces(newFile);
            CheckIn checkIn = checkInService.queryCheckInByCustomer(customerCardNumber);
            if(roomId == checkIn.getRoom().getRoomId()){
                return new RoomExecution(ResultEnum.SUCCESS, checkIn.getRoom());
            } else {
                return new RoomExecution(ResultEnum.ROOM_ID_ERROR);
            }
        } catch (IOException e) {
            return new RoomExecution(ResultEnum.INNER_ERROR);
        }
    }

    /**
     * 实名认证
     * @param customer
     */
    @UserLoginToken
    @PostMapping("/customerverified")
    private CustomerAccountExecution customerVerified(HttpServletRequest request, @RequestBody Customer customer){
        //TODO 更新url
        //获取token
        String token = request.getHeader("token");
        CustomerAccount customerAccount = null;
        if(token != null){
            //获取customerAccount对象
            String jsonStr = (String)redisTemplate.opsForValue().get(token);
            customerAccount = JSONArray.parseObject(jsonStr, CustomerAccount.class);
        } else {
            return new CustomerAccountExecution(ResultEnum.TOKEN_EMPTY);
        }

        if(customer != null && customer.getCustomerCardNumber() != null){
            Customer oldCustomer = customerService.queryCustomerById(customer.getCustomerCardNumber());
            if(oldCustomer == null){
                CustomerExecution customerExecution = customerService.addCustomer(customer, null, null);
                //插入成功
                if(customerExecution.getState() != ResultEnum.SUCCESS.getState()){
                    return new CustomerAccountExecution(ResultEnum.INNER_ERROR);
                }
            }
            customerAccount.setFlag(1);
            customerAccount.setCustomer(customer);
            //向customer_relation表添加关系
            CustomerRelation customerRelation = new CustomerRelation();
            customerRelation.setAccount(customerAccount);
            customerRelation.setCustomer(customer);
            CustomerRelationExecution customerRelationExecution = customerRelationService.addCustomerRelation(customerRelation);
            if(customerRelationExecution.getState() != ResultEnum.SUCCESS.getState()){
                return new CustomerAccountExecution(ResultEnum.RELATION_INSERT_ERROR);
            }
            CustomerAccountExecution customerAccountExecution = customerAccountService.updateCustomerAccount(customerAccount, null);
            if(customerAccountExecution.getState() == ResultEnum.SUCCESS.getState()){
                return customerAccountExecution;
            } else {
                return new CustomerAccountExecution(ResultEnum.CERTIFICATION_ERROR);
            }
        } else {
            return new CustomerAccountExecution(ResultEnum.EMPTY);
        }
    }
}
