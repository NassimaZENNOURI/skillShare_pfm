package com.ensaj.SkillShare.service;

import com.ensaj.SkillShare.model.Categorie;
import com.ensaj.SkillShare.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Optional<Categorie> getcategorieById(int idCategorie) {
        return categorieRepository.findById(idCategorie);
    }

    public void ajouterCategorie(Categorie categorie) {
        categorieRepository.save(categorie);
    }

    public void updateCategorie(int idCategorie, Categorie categorie) {
        Optional<Categorie> existingCategorie = categorieRepository.findById(idCategorie);
        if (existingCategorie.isPresent()) {
            Categorie c = existingCategorie.get();
            c.setCategorie(categorie.getCategorie());
            categorieRepository.save(c);
        }
    }

    public void deleteCategorie(int idCategorie) {
        if (categorieRepository.existsById(idCategorie)) {
            categorieRepository.deleteById(idCategorie);
        } else {
            throw new RuntimeException("Categorie avec l'ID " + idCategorie + " n'existe pas.");
        }
    }

}
