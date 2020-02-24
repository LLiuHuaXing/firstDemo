package com.sxt.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.vo.DeptVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */
public interface DeptMapper extends BaseMapper<Dept> {

    Integer getDeptMax();

    void updateDeptAvailable(@Param("deptVo")DeptVo deptVo);

    List<Integer> showDeptIdfFromUname(Integer deptId);
}
