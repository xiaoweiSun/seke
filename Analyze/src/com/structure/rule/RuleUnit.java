package com.structure.rule;

import java.util.Vector;

//��ŵ��������Ӧ������
public class RuleUnit {
	public String name;
	public String value;
	public Vector<StructUnitC> path;
	public RuleUnit() {
		path = new Vector<StructUnitC>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setPath(Vector<StructUnitC> path) {
		this.path = path;
	}
}