package entities;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class SportRegistrationCustomizer implements DescriptorCustomizer {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void customize(ClassDescriptor descriptor) throws Exception {
        //Query query =
        descriptor.getQueryManager().setDeleteSQLString("UPDATE " + descriptor.getTableName() + " s SET s.deleted_On = CURRENT_TIMESTAMP WHERE s.id = #ID");
        descriptor.getQueryManager().setAdditionalCriteria(":disableDeletedFeature = 1 or this.deletedOn IS NULL");
    }
}
