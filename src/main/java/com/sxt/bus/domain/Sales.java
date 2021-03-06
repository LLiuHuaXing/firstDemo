package com.sxt.bus.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_sales")
public class Sales implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "客户id不能为空")
    private Integer customerid;

    @NotEmpty(message = "支付类型不能为空")
    private String paytype;

    private Date salestime;

    private String operateperson;

    @NotNull(message = "商品数量不能为空")
    private Integer number;

    private String remark;

    @NotNull(message = "销售价格不能为空")
    private Double salesprice;

    @NotNull(message = "商品id不能为空")
    private Integer goodsid;

    @NotEmpty(message = "工号不能为空")
    private String jobnumber;
    

    @TableField(exist=false)
    private String goodsname;
    @TableField(exist=false)
    private String customername;
}
