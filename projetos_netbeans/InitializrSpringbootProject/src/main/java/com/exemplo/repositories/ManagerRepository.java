package com.exemplo.repositories;

import com.exemplo.models.Manager;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    @Query(
            value = "select Manager.* from Manager "
            + "INNER JOIN Client ON Manager.id = Client.manager_id "
            + "where payments=(select max(payments) from Client);",
            nativeQuery = true
    )
    List<Manager> findByMaxPayments();
}
