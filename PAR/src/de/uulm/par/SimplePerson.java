package de.uulm.par;

import java.io.Serializable;

/**
 * @author Fabian Schwab
 *
 */
public class SimplePerson implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String mac;
	public SimplePerson(String name, String mac) {
		super();
		this.name = name;
		this.mac = mac;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
