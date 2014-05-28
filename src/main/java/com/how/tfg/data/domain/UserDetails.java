package com.how.tfg.data.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.social.security.SocialUser;

import com.how.tfg.data.domain.enumerate.Role;
import com.how.tfg.data.domain.enumerate.SocialMediaService;

/**
 * Created by Sergio on 18/04/2014.
 */
public class UserDetails extends SocialUser {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4368203050242058439L;

	private Long id;

    private String firstName;

    private String lastName;

    private Role role;

    private SocialMediaService socialSignInProvider;
    
    private String imageProfile;

    public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String imageProfile) {
        super(username, password, authorities);
        this.imageProfile = imageProfile;
    }


    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public Role getRole() {
		return role;
	}



	public void setRole(Role role) {
		this.role = role;
	}



	public SocialMediaService getSocialSignInProvider() {
		return socialSignInProvider;
	}



	public void setSocialSignInProvider(SocialMediaService socialSignInProvider) {
		this.socialSignInProvider = socialSignInProvider;
	}
	
	public String getImageProfile() {
		return imageProfile;
	}


	public void setImageProfile(String imageProfile) {
		this.imageProfile = imageProfile;
	}


	public static Builder getBuilder() {
		return new Builder();
	}



	public static class Builder {

        private Long id;

        private String username;

        private String firstName;

        private String lastName;

        private String password="";
        
        private String imageProfile;

        private Role role;

        private SocialMediaService socialSignInProvider;

        private Set<GrantedAuthority> authorities;

        public Builder() {
            this.authorities = new HashSet<>();
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.toString());
            this.authorities.add(authority);

            return this;
        }
        
        public Builder imageProfile(String imageProfile) {
        	this.imageProfile = imageProfile;
        	return this;
        }

        public Builder socialSignInProvider(SocialMediaService socialSignInProvider) {
            this.socialSignInProvider = socialSignInProvider;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public UserDetails build() {
        	UserDetails user = new UserDetails(username, password, authorities, imageProfile);

            user.id = id;
            user.firstName = firstName;
            user.lastName = lastName;
            user.role = role;
            user.imageProfile = imageProfile;
            user.socialSignInProvider = socialSignInProvider;

            return user;
        }
    }
	
}