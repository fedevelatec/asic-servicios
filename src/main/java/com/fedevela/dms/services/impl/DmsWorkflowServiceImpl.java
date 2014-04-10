package com.fedevela.dms.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.workflow.pojos.DmsWorkflowConfig;
import com.fedevela.core.workflow.pojos.DmsWorkflowSecurity;
import com.fedevela.core.workflow.pojos.DmsWorkflows;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.dms.services.DmsWorkflowService;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

@Service
public class DmsWorkflowServiceImpl implements DmsWorkflowService {

    @Resource
    private DmsDao dao;

    @Override
    public List<DmsWorkflows> getWorkflows() {
        return dao.find(DmsWorkflows.class);
    }

    @Override
    public List<DmsWorkflows> getWorkflows(DetachedCriteria criteria) {
        return dao.find(criteria);
    }

    @Override
    public List<DmsWorkflowConfig> getWorkflowConfigs() {
        return dao.find(DmsWorkflowConfig.class);
    }

    @Override
    public List<DmsWorkflowConfig> getWorkflowConfigs(DetachedCriteria criteria) {
        return dao.find(criteria);
    }

    @Override
    public List<DmsWorkflowSecurity> getWorkflowSecurity() {
        return dao.find(DmsWorkflowSecurity.class);
    }

    @Override
    public List<DmsWorkflowSecurity> getWorkflowSecurity(DetachedCriteria criteria) {
        return dao.find(criteria);
    }

    @Override
    public List<String> getWFViewStatesByUserRols(Long idWorkflow, Set<BigInteger> rols) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DmsWorkflowSecurity.class);
        criteria.add(Restrictions.eq("dmsWorkflowSecurityPK.idWorkflow", idWorkflow));
        criteria.add(Restrictions.eq("viewState", 't'));
        if ((rols != null) && (!rols.isEmpty())) {
            criteria.add(Restrictions.in("dmsWorkflowSecurityPK.idRol", rols));
        } else {
            criteria.add(Restrictions.isNull("dmsWorkflowSecurityPK.idRol"));
        }
        ProjectionList projections = Projections.projectionList();
        projections.add(Projections.groupProperty("dmsWorkflowSecurityPK.idWorkflowState"));
        criteria.setProjection(projections);
        return dao.find(criteria);
    }
}
