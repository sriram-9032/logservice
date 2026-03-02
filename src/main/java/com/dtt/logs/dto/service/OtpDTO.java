package com.dtt.logs.dto.service;

public class OtpDTO {
    private String identifier;
    
    private String suid;
    
    private String email;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

	public String getSuid() {
		return suid;
	}

	public void setSuid(String suid) {
		this.suid = suid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "OtpDTO [identifier=" + identifier + ", suid=" + suid + ", email=" + email + "]";
	}     
}
