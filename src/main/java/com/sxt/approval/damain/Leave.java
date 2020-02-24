package com.sxt.approval.damain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_leavebill")
public class Leave implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotEmpty(message = "请假标题不能为空")
    private String title;

    @NotEmpty(message = "请假原因不能为空")
    private String content;

    @NotNull(message = "请假天数不能为空")
    private Integer days;

    @NotEmpty(message = "请假开始日期不能为空")
    private String leavetime;

    @NotEmpty(message = "请假类型不能为空")
    private String leavetype;

    private String createtime;

    private Integer userid;

    private Integer state;

    private String endtime;

    /*
    * 请假人的名字
    *
    */
    @TableField(exist = false)
    private String leaveName;

    @TableField(exist = false)
    private String startTime;




}
