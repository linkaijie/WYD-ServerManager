package com.zhwyd.server.common.util;

import java.util.List;

public class JqueryZTreeNode {

	private String id;
	private String pId;
	private String name;
	private boolean checked;
	private boolean open;
	private String isParent;
	private String icon;
	private String type;
	private List<JqueryZTreeNode> childs;
	private String temp1;
	private String temp2;
	private String temp3;
	private String iconOpen;
	private String iconClose;
	public static String iconOpenText="../scripts/zTree/zTreeStyle/img/minus.gif"; 
	public static String iconCloseText="../scripts/zTree/zTreeStyle/img/plus.gif"; 
	public static String iconText="../scripts/zTree/zTreeStyle/img/join.gif"; 

	public List<JqueryZTreeNode> getChilds() {
		return childs;
	}

	public void setChilds(List<JqueryZTreeNode> childs) {
		this.childs = childs;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getTemp1() {
		return temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	public String getTemp2() {
		return temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	public String getTemp3() {
		return temp3;
	}

	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}
	

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}
	
}