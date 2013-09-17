package com.leansoftwarelabs.mypis.service;

import com.leansoftwarelabs.mypis.domain.TrackingCategory;

import javax.annotation.Resource;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "TrackingCategoryFacade", mappedName = "MyPIS-MyPISService-TrackingCategoryFacade")
@Local
public class TrackingCategoryFacadeBean extends AbstractMultiTenantFacade<TrackingCategory> {
    @Resource
    SessionContext sessionContext;
    @PersistenceContext(unitName = "MyPISService")
    private EntityManager em;

    public TrackingCategoryFacadeBean() {
        super(TrackingCategory.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    @Override
    public TrackingCategory mergeEntity(TrackingCategory category) throws ServiceException {
        if (category.getIndex() == null) {
            int freeColumnIndex = -1;
            OUTER_LOOP:
            for (int i = 1; i <= 5; i++) {
                for (TrackingCategory cat : findAll()) {
                    if (cat.getIndex() == i)
                        continue OUTER_LOOP;
                }
                freeColumnIndex = i;
                break OUTER_LOOP;
            }
            if (freeColumnIndex == -1) {
                throw new MaxTrackingCategoryException("5 tracking category is the maximum supported at this point in time.");
            }
            category.setIndex(freeColumnIndex);
        }
        return super.mergeEntity(category);
    }

    @Override
    public void remove(TrackingCategory entity) {
        String trackingField = entity.getTrackingField();
        super.remove(entity);
        StringBuffer buffer = new StringBuffer("UPDATE GLEntryLine o SET o.");
        buffer.append(trackingField);
        buffer.append(" = :value where o.entry.tenantId = :tenantId");
        Query query = em.createQuery(buffer.toString());
        query.setParameter("value", null);
        query.setParameter("tenantId", getCurrentUser().getTenant().getTenantId());
        query.executeUpdate();
    }
    
    public static final class MaxTrackingCategoryException extends ServiceException {
        MaxTrackingCategoryException(String string) {
            super(string);
        }
    }


}

