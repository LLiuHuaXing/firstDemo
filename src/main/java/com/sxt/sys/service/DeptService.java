package com.sxt.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxt.sys.domain.Dept;
import com.sxt.sys.vo.DeptVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface DeptService extends IService<Dept> {

    Integer getDeptMax();

    void updateDeptAvailable(DeptVo deptVo);

    /*根据用户名到用户表查部门id
      根据部门id到部门表查该部门的下属部门id
        */
    List<Integer> showDeptIdfFromUname(Integer deptId);
}
