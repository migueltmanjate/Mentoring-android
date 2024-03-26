package mz.org.csaude.mentoring.service.location;

import java.sql.SQLException;
import java.util.List;

import mz.org.csaude.mentoring.base.service.BaseService;
import mz.org.csaude.mentoring.dto.location.LocationDTO;
import mz.org.csaude.mentoring.model.location.Location;

public interface LocationService extends BaseService<Location> {

    void saveOrUpdates(List<LocationDTO> locationDTOS) throws SQLException;
    Location saveOrUpdate(Location location) throws SQLException;

}
