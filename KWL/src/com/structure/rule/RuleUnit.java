package com.structure.rule;

import java.util.Vector;

//��ŵ��������Ӧ������
public class RuleUnit {
	public String name;
	public Vector<StructUnitC> path;
	public RuleUnit() {
		path = new Vector<StructUnitC>();
	}
}