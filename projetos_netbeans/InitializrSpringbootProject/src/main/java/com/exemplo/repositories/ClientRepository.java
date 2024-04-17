
package com.exemplo.repositories;

import com.exemplo.models.Client;
import java.util.List;
import javax.persistence.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByName(final String name);
    
    @Query(
            value = "select * from Client where credits=(select max(credits) from Client)",
            nativeQuery = true
    )
    List<Client> findByMaxCredits();

    
}
