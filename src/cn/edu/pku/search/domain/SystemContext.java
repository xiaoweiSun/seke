package cn.edu.pku.search.domain;

public class SystemContext {

	private static ThreadLocal<Integer> size = new ThreadLocal<>();
	
	static {
		size.set(10);
	}
	
	public static Integer getSize() {
		return size.get();
	}
	
	public static void setSize(Integer _size) {
		size.set(_size);
	}
	
	public static void removeSize() {
		size.remove();
	}
}
