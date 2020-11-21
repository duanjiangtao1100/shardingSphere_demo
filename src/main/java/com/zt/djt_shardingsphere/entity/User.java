package com.zt.djt_shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_user")
public class User {

private Long userId;
private String username;
private  String ustatus;
}
