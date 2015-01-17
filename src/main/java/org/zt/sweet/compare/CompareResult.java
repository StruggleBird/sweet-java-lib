package org.zt.sweet.compare;

import java.util.ArrayList;
import java.util.List;

/**
 * 比较后的返回结果
 * 
 * @author Ternence
 * @date 2014年3月19日
 */
public class CompareResult<T> {

	/**
	 * 更新列表
	 */
	private List<T> updateList = new ArrayList<T>();

	/**
	 * 删除列表
	 */
	private List<T> deleteList = new ArrayList<T>();

	/**
	 * 新增列表
	 */
	private List<T> newList = new ArrayList<T>();

	/**
	 * 获取变更的总个数，包含增、删、改的总数
	 *
	 *
	 * @author Zhoutao
	 * @date 2014-8-11
	 * @return
	 */
	public int getChangeCount() {
		return updateList.size() + deleteList.size() + newList.size();
	}

	public List<T> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<T> updateList) {
		this.updateList = updateList;
	}

	public List<T> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<T> deleteList) {
		this.deleteList = deleteList;
	}

	public List<T> getNewList() {
		return newList;
	}

	public void setNewList(List<T> newList) {
		this.newList = newList;
	}

}
