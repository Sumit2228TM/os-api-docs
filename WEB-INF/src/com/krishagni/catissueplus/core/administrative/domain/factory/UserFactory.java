
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.UserDetail;

public interface UserFactory {
	User createUser(UserDetail detail);
	
	User createUser(User existing, UserDetail detail);
}
