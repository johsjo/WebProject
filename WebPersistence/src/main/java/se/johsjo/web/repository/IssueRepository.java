package se.johsjo.web.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import se.johsjo.web.model.*;

public interface IssueRepository extends CrudRepository<Issue, Long> {

	Collection<Issue> findByWorkItemId(Long id);
	
	Issue findOne(Long id);
}
