package se.johsjo.web.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import se.johsjo.web.model.*;

public interface TeamRepository extends CrudRepository<Team, Long> {

	Collection<Team> findAll();
}
