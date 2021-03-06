package com.alone.hotel.service.impl;

import com.alone.hotel.dao.CustomerDao;
import com.alone.hotel.dto.CustomerExecution;
import com.alone.hotel.entity.Customer;
import com.alone.hotel.enums.ResultEnum;
import com.alone.hotel.exceptions.CustomerException;
import com.alone.hotel.service.CustomerService;
import com.alone.hotel.utils.ImageUtil;
import com.alone.hotel.utils.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @BelongsProject: hotel
 * @BelongsPackage: com.alone.hotel.service.impl
 * @Author: Alone
 * @CreateTime: 2020-03-23 20:19
 * @Description:
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    
    @Override
    @Transactional
    public CustomerExecution addCustomer(Customer customer, MultipartFile cardImg, MultipartFile faceImg) {
        //非空判断
        if(customer != null && customer.getCustomerCardNumber() != null && customer.getCustomerName() != null && customer.getCustomerCardNumber().length() == 18){
            try {
                //文件流
                if(cardImg != null){
                    String cardUrl = addImages(customer, 1, cardImg);
                    customer.setCustomerCardImg(cardUrl);
                }
                if(faceImg != null){
                    String faceUrl = addImages(customer, 0, faceImg);
                    customer.setCustomerFaceImg(faceUrl);
                }
            } catch (Exception e){
                throw new CustomerException(ResultEnum.FILE_INSERT_ERROR.getStateInfo());
            }
            //插入信息
            try {
                int effectNum = customerDao.addCustomer(customer);
                if(effectNum <= 0){
                    throw new CustomerException(ResultEnum.INNER_ERROR.getStateInfo());
                } else {
                    return new CustomerExecution(ResultEnum.SUCCESS, customer);
                }
            } catch (Exception e){
                return new CustomerExecution(ResultEnum.INNER_ERROR);
            }
        } else {
            return new CustomerExecution(ResultEnum.EMPTY);
        }
    }

    @Override
    public Customer queryCustomerById(String customerId) {
        return customerDao.queryCustomerById(customerId);
    }

    @Override
    public List<Customer> queryCustomerFaceImages() {
        return customerDao.queryCustomerFaceImages();
    }


    @Override
    @Transactional
    public CustomerExecution updateCustomer(Customer customer, MultipartFile cardImg, MultipartFile faceImg) {
        //空值判断
        if(customer != null && customer.getCustomerCardNumber() != null){
            try{
                //身份证图片是否为空
                if(cardImg != null){
                    String cardUrl = addImages(customer, 1, cardImg);
                    customer.setCustomerCardImg(cardUrl);
                }
                if(faceImg != null){
                    String faceUrl = addImages(customer, 0, faceImg);
                    customer.setCustomerFaceImg(faceUrl);
                }
            } catch (Exception e){
                throw new CustomerException(ResultEnum.FILE_INSERT_ERROR.getStateInfo());
            }
            try{
                int effectedNum = customerDao.updateCustomer(customer);
                if(effectedNum <= 0){
                    throw new CustomerException(ResultEnum.INNER_ERROR.getStateInfo());
                }
                return new CustomerExecution(ResultEnum.SUCCESS, customer);
            } catch (Exception e){
                throw new CustomerException(ResultEnum.INNER_ERROR.getStateInfo());
            }
        } else {
            return new CustomerExecution(ResultEnum.EMPTY);
        }
    }

    @Override
    @Transactional
    public CustomerExecution deleteCustomer(String customerCardNumber) {
        if(customerCardNumber != null){
            int effectNum = customerDao.deleteCustomer(customerCardNumber);
            if(effectNum <= 0){
                throw new CustomerException(ResultEnum.INNER_ERROR.getStateInfo());
            }
            return new CustomerExecution(ResultEnum.SUCCESS);
        } else {
            return new CustomerExecution(ResultEnum.EMPTY);
        }
    }


    /**
     * 上传图片
     * @param customer
     * @param imageType
     * @param file
     * @return
     */
    private String addImages(Customer customer, int imageType, MultipartFile file) throws IOException {
        //获取存储路径
        String dest = PathUtil.getPersonImagePath(customer.getCustomerCardNumber(), imageType, 0);
        String relativeAddr = ImageUtil.uploadImage(file, dest);
        return relativeAddr;
    }
}
