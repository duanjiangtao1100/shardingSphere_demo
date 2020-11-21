package com.zt.djt_shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_udict")
public class Udict {


    private long dictid;
    private String ustatus;
    private String uvalue;


}
