package com.sxt.bus.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inport")
public class Inport implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "支付类型不能为空")
    private String paytype;

    private Date inporttime;

    private String operateperson;

    @NotNull(message = "商品数量不能为空")
    private Integer number;

    private String remark;

    @NotNull(message = "商品价格不能为空")
    private Double inportprice;

    @NotNull(message = "供应商id不能为空")
    private Integer providerid;

    @NotNull(message = "商品id不能为空")
    private Integer goodsid;
    
    @TableField(exist=false)
    private String providername;
    @TableField(exist=false)
    private String goodsname;
    @TableField(exist=false)
    private String size;//规格 
}
