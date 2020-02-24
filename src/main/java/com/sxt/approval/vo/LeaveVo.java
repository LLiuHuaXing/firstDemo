package com.sxt.approval.vo;

import com.sxt.approval.damain.Leave;
import com.sxt.sys.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LeaveVo extends Leave {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;
	
}
