package se.johsjo.web.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import se.johsjo.web.model.User;
import se.johsjo.web.model.WorkItem.Status;
import se.johsjo.web.repository.UserRepository;
import se.johsjo.web.repository.WorkItemRepository;

@Component
public class UserService {

	private final UserRepository userRepository;
	private final WorkItemRepository workItemRepository;
	private final ServiceTransaction transaction;

	@Autowired
	public UserService(UserRepository userRepository, WorkItemRepository workItemRepository,
			ServiceTransaction transaction) {
		this.userRepository = userRepository;
		this.workItemRepository = workItemRepository;
		this.transaction = transaction;
	}
	
	
	public User addOrUpdateUser(User user) throws ServiceException {
		return transaction.executeAction(()->{
			if (user.getUsername().length() >= 10) {
				if (user.isActiveUser() == false) {
					return updateUserStatus(user, false);
				}
				return userRepository.save(user);
			} else {
				throw new ServiceException("Username is too short!");
			}			
		});
	}

	public User updateUsername(User user, String username) throws ServiceException {
		user.setUsername(username);
		return addOrUpdateUser(user);
	}

	public User updateFirstName(User user, String firstname) throws ServiceException {
		user.setFirstName(firstname);
		return addOrUpdateUser(user);
	}

	public User updateLastName(User user, String lastname) throws ServiceException {
		user.setLastname(lastname);
		return addOrUpdateUser(user);
	}

	private User updateUserStatus(User user, boolean status) throws ServiceException {
		user.setActiveUser(status);
		try {
			if (user.isActiveUser() == false) {
				return transaction.execute(() -> {
					workItemRepository.findByUserId(user.getId()).forEach(workItem -> {
						workItem.setStatus(Status.UNSTARTED);
						workItemRepository.save(workItem);
					});
					return userRepository.save(user);
				});
			}
			return userRepository.save(user);
		} catch (DataAccessException e) {
			throw new ServiceException("Could not update User status", e);
		}
	}

	public Collection<User> getUsersByTeamId(Long teamId) {
		return userRepository.findByTeamId(teamId);
	}

	public User getUserByUserId(String userId) {
		return userRepository.findUserByUserId(userId);
	}

	public Collection<User> getUsers(String firstname, String lastname, String username) {
		return userRepository.getUser(firstname, lastname, username);
	}

	public Collection<User> getUserByFirstname(String firstname) {
		return userRepository.getUser(firstname, "%", "%");
	}

	public Collection<User> getUserByLastname(String lastname) {
		return userRepository.getUser("%", lastname, "%");
	}

	public Collection<User> getUserByUsername(String username) {
		return userRepository.getUser("%", "%", username);
	}

	public Collection<User> getAllUsers() {
		List<User> listOfUsers = new ArrayList<>();
		userRepository.findAll().forEach(u -> listOfUsers.add(u));
		return listOfUsers;
	}

	public void userExists(User user) throws ServiceException {
		if (userRepository.findOne(user.getId()) == null) {
			throw new ServiceException("User not found");
		}
	}

}