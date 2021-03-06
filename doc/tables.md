## 数据库

### 总体设计


#### 账号表 tb_employee_account

|      字段名      | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :--------------: | :------: | :------: | :------: | :------: |
|   account_name   | varchar  |  用户名  |    是    |          |
| account_password | varchar  |   密码   |          |          |
|  account_power   |   int    |   权限   |          |          |


#### 员工信息表 tb_employee

|        字段名        | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :------------------: | :------: | :------: | :------: | :------: |
|     employee_id      | varchar  |  用户id  |    是    |          |
|    employee_name     | varchar  | 用户姓名 |          |          |
|     employee_age     |   int    |   年龄   |          |          |
|   employee_gender    |   int    |   性别   |          |          |
| employee_card_number | varchar  | 证件号码 |          |          |
|  employee_card_img   | varchar  | 证件图片 |          |          |
|  employee_face_img   | varchar  | 脸部图片 |          |          |
|    employee_phone    | varchar  |  手机号  |          |          |
|    position_type     |   int    |   职位   |          |    是    |
| employee_create_time | datetime | 入职时间 |          |          |
| employee_leave_time  | datetime | 离职时间 |          |          |

#### 顾客信息表 tb_customer

|      字段名       | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :---------------: | :------: | :------: | :------: | :------: |
|   customer_name   | varchar  | 用户姓名 |          |          |
|   customer_age    |   int    |   年龄   |          |          |
|  customer_gender  |   int    |   性别   |          |          |
| customer_card_img | varchar  | 证件图片 |          |          |
| customer_card_id  | char(18) | 证件号码 |    是    |          |
| customer_face_img | varchar  | 脸部图片 |          |          |
|  customer_phone   | char(11) |  手机号  |          |          |

#### 房间表 tb_room

|   字段名   | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :--------: | :------: | :------: | :------: | :------: |
|  room_id   |   int    |  房间号  |    是    |          |
| room_floor | varchar  | 房间楼栋 |          |          |
| room_desc  | varchar  | 房间描述 |          |          |
| room_state |   int    | 房间状态 |          |          |
| room_type  |   int    | 房间类型 |          |          |
| room_price | decimal  | 房间单价 |          |          |

#### 房间订单表 tb_room_order

|    字段名    | 字段类型 |  字段描述  | 是否主键 | 是否外键 |
| :----------: | :------: | :--------: | :------: | :------: |
|   order_id   | varchar  | 房间订单号 |    是    |          |
|  account_id  | varchar  |  消费者id  |          |    是    |
|   room_id    |   int    |   房间id   |          |    是    |
| order_price  | decimal  |    总价    |          |          |
|  start_time  | datetime |  开始时间  |          |          |
|   end_time   | datetime |  结束时间  |          |          |
| order_status |   int    |  订单状态  |          |          |
| hand_in_time | datetime |  提交时间  |          |          |

#### 其他消费表 tb_recreate_order

|    字段名     | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :-----------: | :------: | :------: | :------: | :------: |
|   order_id    | varchar  |  订单id  |    是    |          |
|  customer_id  | varchar  | 消费者id |          |    是    |
| recreation_id |   int    | 消费类型 |          |    是    |
|  order_price  | decimal  |   价格   |          |          |
|  start_time   | datetime | 开始时间 |          |          |
|   end_time    | datetime | 结束时间 |          |          |
| order_status  |   int    | 订单状态 |          |          |

#### 娱乐消费目录 tb_recreation

|      字段名      | 字段类型 |  字段描述  | 是否主键 | 是否外键 |
| :--------------: | :------: | :--------: | :------: | :------: |
|  recreation_id   |   int    | 娱乐项目id |    是    |          |
| recreation_name  | varchar  |    名称    |          |          |
| recreation_price | decimal  |    单价    |          |          |

#### 入住信息表 tb_check_in

|   字段名    | 字段类型 |  字段描述  | 是否主键 | 是否外键 |
| :---------: | :------: | :--------: | :------: | :------: |
|   room_id   |   int    |   房间号   |    是    |          |
| customer_id | varchar  | 入住人编号 |    是    |          |

#### 排班表 tb_work

|   字段名    | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :---------: | :------: | :------: | :------: | :------: |
| employee_id | varchar  |  员工id  |    是    |    是    |
|  work_time  | datetime |   日期   |    是    |          |
| start_time  | datetime | 开始时间 |          |          |
|  end_time   | datetime | 结束时间 |          |          |
|   status    | integer  |   状态   |          |          |

#### 工资表 tb_position

|        字段名         | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :-------------------: | :------: | :------: | :------: | :------: |
|      position_id      |   int    |  职位号  |    是    |          |
|     position_name     | varchar  |  职位名  |          |          |
| position_basic_salary | decimal  |   底薪   |          |          |
|     position_note     | varchar  |   备注   |          |          |

#### 房间详情图 tb_room_img

|    字段名     | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :-----------: | :------: | :------: | :------: | :------: |
|  room_img_id  |   int    |  图片ID  |    是    |          |
| room_img_addr | varchar  | 图片地址 |          |          |
|   priority    |   int    |   权重   |          |          |
|    room_id    |   int    |  房间ID  |          |    是    |

#### 账号关联信息表 tb_customer_account

|   字段名    | 字段类型 |  字段描述  | 是否主键 | 是否外键 |
| :---------: | :------: | :--------: | :------: | :------: |
| account_id  | varchar  |   账号id   |    是    |          |
| customer_id | varchar  |   用户id   |          |          |
|    flag     |   int    | 实名制标记 |          |          |

#### 顾客添加信息表 tb_customer_relation
|    字段名    | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :----------: | :------: | :------: | :------: | :------: |
| customer_id  | varchar  |  顾客id  |    是    |          |
| account_name | varchar  |  账户名  |    是    |          |

#### 清洁工楼层 tb_cleaner

|   字段名    | 字段类型 | 字段描述 | 是否主键 | 是否外键 |
| :---------: | :------: | :------: | :------: | :------: |
| employee_id | varchar  | 清洁工id |    是    |    是    |
| room_floor  |   int    | 酒店楼层 |    是    |          |