package com.sxt.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxt.sys.common.DataGridView;
import com.sxt.sys.common.ResultObj;
import com.sxt.sys.common.TreeNode;
import com.sxt.sys.service.DeptService;
import com.sxt.sys.vo.DeptVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_dept")
@ToString
public class Dept implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "父级id不能为空")
    private Integer pid;

    @NotEmpty(message = "部门名称不能为空")
    private String title;

    @NotNull(message = "是否展开不能为空")
    private Integer open;

    private String remark;

    @NotEmpty(message = "地址不能为空")
    private String address;

    /**
     * 状态【0不可用1可用】
     */
    @NotNull(message = "部门状态不能为空")
    private Integer available;

    /**
     * 排序码【为了调事显示顺序】
     */
    @NotNull(message = "排序码不能为空")
    private Integer ordernum;

    private Date createtime;


}
