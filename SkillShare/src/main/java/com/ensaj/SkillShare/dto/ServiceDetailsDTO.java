package com.ensaj.SkillShare.dto;

import lombok.Data;

@Data
public class ServiceDetailsDTO {
    private String categorieNom;
    private String userNom;
    private String userPrenom;

    public ServiceDetailsDTO(String categorieNom, String userNom, String userPrenom) {
        this.categorieNom = categorieNom;
        this.userNom = userNom;
        this.userPrenom = userPrenom;
    }
}
