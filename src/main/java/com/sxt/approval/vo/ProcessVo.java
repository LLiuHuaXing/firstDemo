package com.sxt.approval.vo;

import com.sxt.approval.damain.Leave;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProcessVo {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer page=1;
	private Integer limit=10;
	
}
