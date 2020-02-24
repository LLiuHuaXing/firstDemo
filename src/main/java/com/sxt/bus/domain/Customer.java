package com.sxt.bus.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("bus_customer")
@ToString
public class Customer implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "客户名称不能为空")
    private String customername;

    @NotEmpty(message = "邮编不能为空")
    @Pattern(regexp = "^[0-9]{6}$",message = "邮编位数为6位")
    private String zip;

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotEmpty(message = "座机号码不能为空")
    @Pattern(regexp = "0\\d{2,3}-\\d{7,8}",message = "座机号格式错误")
    private String telephone;

    @NotEmpty(message = "联系人不能为空")
    private String connectionperson;

    @NotEmpty(message = "联系人手机号码不能为空")
    @Pattern(regexp = "^(13|14|15|18|17)[0-9]{9}",message = "手机号码格式错误")
    private String phone;

    @NotEmpty(message = "开户银行不能为空")
    private String bank;

    @NotEmpty(message = "帐号不能为空")
    @Pattern(regexp = "^(\\d{15}|\\d{16}|\\d{18})$",message = "银行卡号位数错误")
    private String account;

    @NotEmpty(message = "邮箱不能为空")
    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}",message = "邮箱格式错误")
    private String email;

    @NotEmpty(message = "传真不能为空")
    private String fax;

    @NotNull(message = "客户类型不能为空")
    private Integer available;

}
