package se.johsjo.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

//import se.johsjo.webpersistence.model.Team;
//import se.johsjo.webpersistence.model.User;
//import se.johsjo.webpersistence.model.WorkItem;
//import se.johsjo.webpersistence.service.TeamService;
//import se.johsjo.webpersistence.service.UserService;
//import se.johsjo.webpersistence.service.WorkItemService;

@SpringBootApplication
public class WebPersistenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebPersistenceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner run(ApplicationContext context) {
		return args -> {
//  	//TEAM-SCOPE
//			 TeamService teamService = context.getBean(TeamService.class);
//			
//			 teamService.addOrUpdateTeam(new Team("teamName5"));
//			 teamService.addOrUpdateTeam(new Team("teamName6"));
//			 Team team = teamService.addOrUpdateTeam(new Team("teamName7"));
//			
//			 User user = new User("userddnamhytdadsddd1", "firstname", "lastnam",
//			 "2007");
//			 User user2 = new User("userfgnamhytdasdhfddd2", "firstname",
//			 "lastnam", "2008");
//			
//			 UserService userService = context.getBean(UserService.class);
//
//			 userService.addOrUpdateUser(user);
//			 userService.addOrUpdateUser(user2);
//			
//			 teamService.addUserToTeam(user, team);
//			 teamService.addUserToTeam(user2, team);
			
			
			
//		//WORKITEM-SCOPE
//			//WORKITEMS
//			 WorkItem workItem = new WorkItem("title12", "description12");
//			 WorkItem workItem1 = new WorkItem("title122", "description12");
//			 WorkItem workItem2 = new WorkItem("title123", "description12");
//			 WorkItem workItem3 = new WorkItem("title124", "description12");
//			 WorkItem workItem4 = new WorkItem("title125", "description12");
//			 WorkItem workItem5 = new WorkItem("title126", "description12");
////			
//			//SERVICE
//			 WorkItemService workItemService =
//			 context.getBean(WorkItemService.class);
//			
//			
//			 workItemService.addOrUpdateWorkItem(workItem);
//			 workItemService.addOrUpdateWorkItem(workItem1);
//			 workItemService.addOrUpdateWorkItem(workItem2);
//			 workItemService.addOrUpdateWorkItem(workItem3);
//			 workItemService.addOrUpdateWorkItem(workItem4);
//			 workItemService.addOrUpdateWorkItem(workItem5);

			
			
			
//			 workItemService.addUserToWorkItem(workItem, user);
			
//			 userService.updateUserStatus(user, false);
			
			
//			 WorkItem workItem = workItemService.getWorkItemById(8L);
//			 
//			 IssueService issueService = context.getBean(IssueService.class);
			 
//			 Issue issue = new Issue(workItem, "description");
			
//			 issueService.addIssue(workItem, "testes");
//			 addOrUpdate(issue);

		};
	}
}
