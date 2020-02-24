package com.sxt.sys.common;

import java.util.ArrayList;
import java.util.List;

public class TreeNodeBuilder {
	
	
	
	/**
	*@Description 把没有层级关系的集合变成有层级关系的
	*@Param
	*@Return
	*@Author Mr.Ren
	*@Date 2019/12/16
	*@Time 23:43
	*/
	public static List<TreeNode> build(List<TreeNode> treeNodes,Integer topid){
		List<TreeNode> nodes=new ArrayList<>();
		for (TreeNode n1 : treeNodes) {
			if(n1.getPid()==topid) {
				nodes.add(n1);
			}
			for (TreeNode n2 : treeNodes) {
				if(n1.getId()==n2.getPid()) {
					n1.getChildren().add(n2);
				}
			}
		}
		return nodes;
	}
}
