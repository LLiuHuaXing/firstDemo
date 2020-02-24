package com.sxt.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

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
@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "父级菜单不能为空")
    private Integer pid;

    /**
     * 权限类型[menu/permission]
     */
    private String type;

    @NotEmpty(message = "菜单名称不能为空")
    private String title;

    /**
     * 权限编码[只有type= permission才有  user:view]
     */
    private String percode;

    private String icon;

    private String href;

    private String target;

    @NotNull(message = "是否展开不能为空")
    private Integer open;

    @NotNull(message = "排列号不能为空")
    //https://www.cnblogs.com/webskill/p/7422876.html
   // @Pattern(regexp = "^([1-9][0-9]*)$",message = "必须为正整数")
    private Integer ordernum;

    /**
     * 状态【0不可用1可用】
     */
    @NotNull(message = "菜单状态不能为空")
    private Integer available;


}
