package in.pulkitjain.expensetrackerapi.service;

import in.pulkitjain.expensetrackerapi.entity.User;
import in.pulkitjain.expensetrackerapi.entity.UserModel;

public interface UserService {
	User createUser(UserModel user);
	User readUser();
	User updateUser(UserModel user);
	void deleteUser();
	User getLoggedInUser();
}
