package com.sxt.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_goods")
@ToString
public class Goods implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "商品名称不能为空")
    private String goodsname;

    @NotEmpty(message = "产地不能为空")
    private String produceplace;

    @NotEmpty(message = "规格大小不能为空")
    private String size;


    private String goodspackage;

    @NotEmpty(message = "生产批号不能为空")
    private String productcode;

    @NotEmpty(message = "批准文号不能为空")
    private String promitcode;

    @NotEmpty(message = "批准文号不能为空")
    private String description;

    @NotNull(message = "销售价格不能为空")
    //@Pattern(regexp = "([0-9]*(\\.?)[0-9]*)|[0-9]+",message = "库存量必须为整数或者浮点数")
    private Double price;

    @NotNull(message = "库存量不能为空")
   // @Pattern(regexp = "^\\d+$}",message = "库存量必须为正整数")
    private Integer number;

    @NotNull(message = "预警值不能为空")
    //@Pattern(regexp = "^\\d+$",message = "预警值必须为正整数")
    private Integer dangernum;

    @NotEmpty(message = "商品照片不能为空")
    private String goodsimg;

    @NotNull(message = "商品类型不能为空")
    private Integer available;

    @NotNull(message = "供应商id不能为空")
    private Integer providerid;
    
    @TableField(exist=false)
    private String providername;
    


}
