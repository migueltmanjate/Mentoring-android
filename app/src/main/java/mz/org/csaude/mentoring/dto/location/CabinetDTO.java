package mz.org.csaude.mentoring.dto.location;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.csaude.mentoring.base.dto.BaseEntityDTO;
import mz.org.csaude.mentoring.model.location.Cabinet;

/**
 * @author Jose Julai Ritsure
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CabinetDTO extends BaseEntityDTO {
    private String name;

    public CabinetDTO(String name) {
        this.setName(name);
    }

    public CabinetDTO(String uuid, String name) {
        this.setUuid(uuid);
        this.setName(name);
    }

    public CabinetDTO(Cabinet cabinet) {
        super(cabinet);
        this.setName(cabinet.getName());
    }

    public Cabinet getCabinet(){
        Cabinet cabinet = new Cabinet();
        cabinet.setUuid(this.getUuid());
        cabinet.setCreatedAt(this.getCreatedAt());
        cabinet.setUpdatedAt(this.getUpdatedAt());
        cabinet.setLifeCycleStatus(this.getLifeCycleStatus());
        cabinet.setName(this.getName());
        return cabinet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
