package com.ctosb.core.model;

public class User {

	private Integer id;
	private String userName;
	private String password;
	private Integer age;

	public Integer getId() {
		return id;
	}

	public User setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public User setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public User setAge(Integer age) {
		this.age = age;
		return this;
	}

}
