package com.sxt.bus.vo;

import com.sxt.bus.domain.Inport;
import com.sxt.bus.domain.Sales;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
public class SalesVo extends Sales{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Integer page=1;
	private Integer limit=10;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
}
