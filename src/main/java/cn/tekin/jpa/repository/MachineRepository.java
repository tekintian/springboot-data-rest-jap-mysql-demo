package cn.tekin.jpa.repository;

import cn.tekin.jpa.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "machine")
public interface MachineRepository extends JpaRepository<Machine,Integer> {

}
