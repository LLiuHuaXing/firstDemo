package com.sxt.stat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class CustomerAreaStat implements Serializable {

    private static final long serialVersionUID=1L;

    private String name;

    private Object value;

    private String yearRange;

    private String yearMonthRange;

    private String endYearTime;

    private String endYearMonthTime;

    private Integer deptId;

    //数据查询条件
    private String startCondition;
    private String endCondition;
    //用年月格式还是年格式的标志
    private String NotSign;
    private List<Object> list;

}
