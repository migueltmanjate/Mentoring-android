package mz.org.csaude.mentoring.dto.professionalcategory;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.professionalCategory.ProfessionalCategory;

@Data
@NoArgsConstructor
public class ProfessionalCategoryDTO extends BaseEntityDTO {

    private String code;
    private String description;

    public ProfessionalCategoryDTO(ProfessionalCategory professionalCategory){
        this.setUuid(professionalCategory.getUuid());
        this.setCode(professionalCategory.getCode());
        this.setDescription(professionalCategory.getDescription());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ProfessionalCategory getProfessionalCategory() {
        ProfessionalCategory professionalCategory = new ProfessionalCategory();
        professionalCategory.setUuid(this.getUuid());
        professionalCategory.setCode(this.getCode());
        professionalCategory.setDescription(this.getDescription());
        professionalCategory.setCreatedAt(this.getCreatedAt());
        professionalCategory.setUpdatedAt(this.getUpdatedAt());
        return professionalCategory;
    }

}
