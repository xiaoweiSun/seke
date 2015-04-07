package com.structure.rule;

import java.util.Vector;

//存放单个规则对应的内容
public class RuleUnit {
	public String name;
	public Vector<StructUnitC> path;
	public RuleUnit() {
		path = new Vector<StructUnitC>();
	}
}