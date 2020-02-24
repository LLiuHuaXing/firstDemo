package com.sxt.bus.vo;

import com.sxt.bus.domain.Goods;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GoodsVo extends Goods {

	private static final long serialVersionUID = 1L;

	private Integer page = 1;
	private Integer limit = 10;

	private Integer[] ids;
	private String[] goodsImgS;

}
