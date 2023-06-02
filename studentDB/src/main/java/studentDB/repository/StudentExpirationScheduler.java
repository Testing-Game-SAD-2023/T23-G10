package studentDB.repository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Component
public class StudentExpirationScheduler {

    @PersistenceContext
    private EntityManager entityManager;

    @Scheduled(cron = "0 0 * * * *") // Esegui ogni ora
    @Transactional
    public void removeExpiredStudents() {
    	String time = String.valueOf(LocalDateTime.now());
    	time = time.replace("T"," ").substring(0, 19);
    	System.out.println(time + " --- Rimozione utenti non verificati.");
    	
    	// Elimina gli utenti non verificati da più di 8 ore
        LocalDateTime currentDateTime = LocalDateTime.now().minusHours(8);
        Query deleteQuery = entityManager.createQuery("DELETE FROM Student s WHERE s.creationTimestamp < :timestamp");
        deleteQuery.setParameter("timestamp", currentDateTime);
        deleteQuery.executeUpdate();
    }
}