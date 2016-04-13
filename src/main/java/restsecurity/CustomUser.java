package restsecurity;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class CustomUser {
	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	
	public CustomUser() {}
	
	public CustomUser(String username, String password) {
		this.username = username;
		this.password  = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}	
}