package org.sunnysolong.security;

import java.security.Permission;
import java.security.Provider;
import java.security.Security;
import java.security.SecurityPermission;

public class SecuritySample {

	@SuppressWarnings("serial")
	public static void main(String[] args){
		Provider[] providers = Security.getProviders();
		for(Provider p : providers){
			System.out.println(p.getInfo());
		}
		
		SecurityPermission sp  = new SecurityPermission("handle.*");
		sp.implies(new Permission("getPolicy"){

			@Override
			public boolean equals(Object arg0) {
				return false;
			}

			@Override
			public String getActions() {
				return this.getName();
			}

			@Override
			public int hashCode() {
				return 0;
			}

			@Override
			public boolean implies(Permission arg0) {
				return false;
			}
			
			
		});
	
		
	}
}
