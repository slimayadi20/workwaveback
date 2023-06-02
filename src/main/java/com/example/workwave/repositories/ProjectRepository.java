package com.example.workwave.repositories;

import com.example.workwave.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository  extends JpaRepository<Project,Long> {
    public Project findByProjectname(String projectname);
    @Query("SELECT COUNT(p) FROM Project p")
    Long countProjects();
    @Query("SELECT COUNT(p) FROM Project p WHERE p.etat = 'active'")
    Long countActiveProjects();
    @Query("SELECT COUNT(p) FROM Project p WHERE p.etat = 'inactive'")
    Long countInactiveProjects();
}