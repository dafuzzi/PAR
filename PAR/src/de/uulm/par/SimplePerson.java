package de.uulm.par;

import java.io.Serializable;

/**
 * @author Fabian Schwab
 *
 * A simple object which contains a MAC and a custom Name for the person.
 */
public class SimplePerson implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String mac;
	
	/**
	 * @param name
	 * @param mac
	 */
	public SimplePerson(String name, String mac) {
		super();
		this.name = name;
		this.mac = mac;
	}
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
}
