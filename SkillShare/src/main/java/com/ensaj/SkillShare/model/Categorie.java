package com.ensaj.SkillShare.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Categorie {

    public int getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

//	public List<ServicePropose> getServices() {
//		return services;
//	}
//
//	public void setServices(List<ServicePropose> services) {
//		this.services = services;
//	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategorie;

    private String categorie;

//    @OneToMany(mappedBy = "idService", cascade = CascadeType.ALL)
//    private List<ServicePropose> services;

}
