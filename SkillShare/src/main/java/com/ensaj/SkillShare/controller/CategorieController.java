package com.ensaj.SkillShare.controller;

import com.ensaj.SkillShare.model.Categorie;
import com.ensaj.SkillShare.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {
    @Autowired
    private CategorieService categorieService;

    @GetMapping("/allCategories")
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = categorieService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/save")
    public ResponseEntity<String> ajouterCategorie(@RequestBody Categorie categorie) {
        categorieService.ajouterCategorie(categorie);
        return ResponseEntity.ok("categorie saved successfully");
    }

    @GetMapping("/categorie/{idCategorie}")
    public ResponseEntity<Optional<Categorie>> getCategorieById(@PathVariable int idCategorie) {
        Optional<Categorie> categorie = categorieService.getcategorieById(idCategorie);
        return ResponseEntity.ok(categorie);
    }

    @DeleteMapping("delete/{idCategorie}")
    public ResponseEntity<String> deleteCategorie(@PathVariable int idCategorie) {
        categorieService.deleteCategorie(idCategorie);
        return ResponseEntity.ok("categorie avec ID " + idCategorie + " supprimé avec succès.");
    }

    @PutMapping("/update/{idCategorie}")
    public ResponseEntity<String> updateCategorie(@PathVariable int idCategorie, @RequestBody Categorie updatedCategorie) {
        Optional<Categorie> optionalCategorie = categorieService.getcategorieById(idCategorie);
        if (optionalCategorie.isEmpty()) {
            return ResponseEntity.badRequest().body("categorie introuvable avec l'ID : " + idCategorie);
        }
        Categorie existingCategorie = optionalCategorie.get();
        existingCategorie.setCategorie(updatedCategorie.getCategorie());
        categorieService.updateCategorie(idCategorie,existingCategorie);
        return ResponseEntity.ok("Categorie mis à jour avec succès.");
    }
}
