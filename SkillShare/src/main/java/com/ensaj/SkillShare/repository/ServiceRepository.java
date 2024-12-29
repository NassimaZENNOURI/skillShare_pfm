package com.ensaj.SkillShare.repository;

import com.ensaj.SkillShare.model.ServicePropose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServicePropose, Integer> {

    @Query("SELECT sp.nomService, sp.description, sp.prix, " +
            "u.nom, u.prenom, c.categorie, sp.idService, c.idCategorie " +
            "FROM ServicePropose sp " +
            "JOIN sp.utilisateur u " +
            "JOIN sp.categories c " +
            "WHERE c.idCategorie = :idCategorie AND u.idUser = :idUser")
    List<Object[]> findAllServicesByCategoryAndUser(@Param("idCategorie") int idCategorie,
                                                    @Param("idUser") int idUser);

    @Query("SELECT sp.idService ,sp.nomService, sp.description, sp.prix, " +
            "u.nom, u.prenom, c.categorie " +
            "FROM ServicePropose sp " +
            "JOIN sp.utilisateur u " +
            "JOIN sp.categories c " +
            "WHERE c.idCategorie = :idCategorie AND u.idUser = :idUser AND sp.idService =:idService")
    List<Object[]> findAllServicesByCategoryAndUserAndId(@Param("idCategorie") int idCategorie,
                                                         @Param("idUser") int idUser, @Param("idService") int idService);

    /*@Query("SELECT s FROM ServicePropose s JOIN s.utilisateur u WHERE " +
                                                                   "ST_Distance_Sphere(point(u.longitude, u.latitude), " +
                                                                   "point(?1, ?2)) <= ?3")
   List<ServicePropose> findNearbyServices(double longitude, double latitude, double radiusInMeters);*/

    
   /* @Query("SELECT sp.idService, sp.nomService, sp.description, sp.prix, " +
    	       "u.nom, u.prenom, c.categorie, r.statutR " +
    	       "FROM ServicePropose sp " +
    	       "JOIN sp.utilisateur u " +
    	       "JOIN sp.categories c " +
    	       "LEFT JOIN sp.reservations r")
    	List<Object[]> findAllServices();
*/

    @Query("SELECT sp.idService, sp.nomService, sp.description, sp.prix, " +
            "u.nom, u.prenom, u.localisation, u.image, c.categorie " +
            "FROM ServicePropose sp " +
            "JOIN sp.utilisateur u " +
            "JOIN sp.categories c " +
            "WHERE u.idUser != :idUser")
    List<Object[]> findAllServicesExcludingUser(@Param("idUser") int idUser);


    @Query("SELECT sp.idService, sp.nomService, sp.description, sp.prix,"+
            "u.nom, u.prenom, u.localisation, u.image, c.categorie "
            + " FROM ServicePropose sp " +
            "JOIN sp.utilisateur u " +
            "JOIN sp.categories c " +
            "WHERE u.idUser != :idUser " +
            "AND (:minPrix IS NULL OR sp.prix >= :minPrix) " +
            "AND (:maxPrix IS NULL OR sp.prix <= :maxPrix) " +
            "AND (:categorie IS NULL OR c.categorie = :categorie) " +
            "AND (:localisation IS NULL OR u.localisation = :localisation) " +
            "AND (:searchText IS NULL OR " +
            "LOWER(CONCAT(sp.nomService, ' ', sp.description, ' ', sp.prix, ' ', u.nom, ' ', u.prenom, ' ', u.localisation, ' ', c.categorie)) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    List<Object[]> searchServices(
            @Param("idUser") int idUser,
            @Param("minPrix") Float minPrix,
            @Param("maxPrix") Float maxPrix,
            @Param("categorie") String categorie,
            @Param("localisation") String localisation,
            @Param("searchText") String searchText
    );

    @Query("SELECT sp.idService, sp.nomService, sp.description, sp.prix,"+
            "u.nom, u.prenom, u.localisation, u.image, c.categorie "
            + " FROM ServicePropose sp " +
            "JOIN sp.utilisateur u " +
            "JOIN sp.categories c " +
            "WHERE u.idUser != :idUser " +
            "AND (:searchText IS NULL OR " +
            "LOWER(CONCAT(sp.nomService, ' ', sp.description, ' ', sp.prix, ' ', u.nom, ' ', u.prenom, ' ', u.localisation, ' ', c.categorie)) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    List<Object[]> searchServiceByText(
            @Param("idUser") int idUser,
            @Param("searchText") String searchText
    );


}