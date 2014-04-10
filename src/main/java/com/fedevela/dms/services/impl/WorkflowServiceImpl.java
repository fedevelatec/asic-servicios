package com.fedevela.dms.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.workflow.pojos.Workflow;
import com.fedevela.core.workflow.pojos.WorkflowConfig;
import com.fedevela.core.workflow.pojos.WorkflowState;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.dms.services.WorkflowService;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Resource
    private DmsDao dao;

    @Override
    public WorkflowConfig getWorkflowConfig(
            final Long workflow,
            final String currentState,
            final String event) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WorkflowConfig.class);
        criteria.add(Restrictions.eq("workflow.idWorkflow", workflow));
        criteria.add(Restrictions.eq("currentState.idWorkflowState", currentState));
        criteria.add(Restrictions.eq("event.idWorkflowEvent", event));
        List<WorkflowConfig> rs = dao.find(criteria);
        if (rs == null || rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public WorkflowConfig getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String idWorkflowEvent,
            final String[] authorities,
            final boolean doNothingExclude) {
        StringBuilder strAuthorities = null;
        for (String authority : authorities) {
            if (strAuthorities == null) {
                strAuthorities = new StringBuilder();
                strAuthorities.append("'").append(authority).append("'");
            } else {
                strAuthorities.append(",'").append(authority).append("'");
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT WC.* ");
        sql.append(" FROM PROD.WORKFLOW_CONFIG WC,");
        sql.append("      PROD.WORKFLOW_SECURITY WS");
        sql.append(" WHERE   WC.ID_WORKFLOW=").append(idWorkflow);
        sql.append("     AND WC.ID_CURRENT_STATE='").append(idWorkflowState).append("'");
        sql.append("     AND WC.ID_WORKFLOW_EVENT='").append(idWorkflowEvent).append("'");
        if (doNothingExclude) {
            sql.append("     AND WC.ID_WORKFLOW_EVENT<>'doNothing' ");
        }
        sql.append("     AND WS.ID_WORKFLOW_CONFIG=WC.ID_WORKFLOW_CONFIG");
        sql.append("     AND WS.ID_AUTHORITY IN(").append(strAuthorities).append(")");
        List<WorkflowConfig> config = dao.find(WorkflowConfig.class, sql);
        return config.get(0);
    }

    @Override
    public WorkflowConfig getWorkflowConfig(
            final Long idWorkflow,
            String idWorkflowState,
            String idWorkflowEvent,
            String[] authorities,
            boolean doNothingExclude,
            boolean allowState,
            boolean allowEvent) {
        StringBuilder strAuthorities = null;
        for (String authority : authorities) {
            if (strAuthorities == null) {
                strAuthorities = new StringBuilder();
                strAuthorities.append("'").append(authority).append("'");
            } else {
                strAuthorities.append(",'").append(authority).append("'");
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT WC.* ");
        sql.append(" FROM PROD.WORKFLOW_CONFIG WC,");
        sql.append("      PROD.WORKFLOW_SECURITY WS");
        sql.append(" WHERE   WC.ID_WORKFLOW=").append(idWorkflow);
        sql.append("     AND WC.ID_CURRENT_STATE='").append(idWorkflowState).append("'");
        sql.append("     AND WC.ID_WORKFLOW_EVENT='").append(idWorkflowEvent).append("'");
        if (doNothingExclude) {
            sql.append("     AND WC.ID_WORKFLOW_EVENT<>'doNothing' ");
        }
        sql.append("     AND WS.ID_WORKFLOW_CONFIG=WC.ID_WORKFLOW_CONFIG");
        sql.append("     AND WS.ID_AUTHORITY IN(").append(strAuthorities).append(")");
        sql.append("     AND WS.ALLOW_STATE='").append((allowState) ? 't' : 'f').append("'");
        sql.append("     AND WS.ALLOW_EVENT='").append((allowEvent) ? 't' : 'f').append("'");
        List<WorkflowConfig> config = dao.find(WorkflowConfig.class, sql);
        if ((config == null) || (config.isEmpty())) {
            return null;
        } else {
            return config.get(0);
        }
    }

    @Override
    public List<WorkflowConfig> getWorkflowConfig(
            final Long workflow,
            final String currentState,
            final boolean doNothingExclude) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WorkflowConfig.class);
        criteria.add(Restrictions.eq("workflow.idWorkflow", workflow));
        criteria.add(Restrictions.eq("currentState.idWorkflowState", currentState));
        if (doNothingExclude) {
            criteria.add(Restrictions.ne("workflowEvent.idWorkflowEvent", "doNothing"));
        }
        return dao.find(criteria);
    }

    @Override
    public List<WorkflowConfig> getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String[] authorities,
            final boolean doNothingExclude) {
        StringBuilder strAuthorities = null;
        for (String authority : authorities) {
            if (strAuthorities == null) {
                strAuthorities = new StringBuilder();
                strAuthorities.append("'").append(authority).append("'");
            } else {
                strAuthorities.append(",'").append(authority).append("'");
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT WC.* ");
        sql.append(" FROM PROD.WORKFLOW_CONFIG WC,");
        sql.append("      PROD.WORKFLOW_SECURITY WS");
        sql.append(" WHERE   WC.ID_WORKFLOW=").append(idWorkflow);
        sql.append("     AND WC.ID_CURRENT_STATE='").append(idWorkflowState).append("'");
        if (doNothingExclude) {
            sql.append("     AND WC.ID_WORKFLOW_EVENT<>'doNothing' ");
        }
        sql.append("     AND WS.ID_WORKFLOW_CONFIG=WC.ID_WORKFLOW_CONFIG");
        sql.append("     AND WS.ID_AUTHORITY IN(").append(strAuthorities).append(")");

        return dao.find(WorkflowConfig.class, sql);
    }

    @Override
    public List<WorkflowConfig> getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String[] authorities,
            final boolean doNothingExclude,
            final boolean allowState,
            final boolean allowEvent) {
        StringBuilder strAuthorities = null;
        for (String authority : authorities) {
            if (strAuthorities == null) {
                strAuthorities = new StringBuilder();
                strAuthorities.append("'").append(authority).append("'");
            } else {
                strAuthorities.append(",'").append(authority).append("'");
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT WC.* ");
        sql.append(" FROM PROD.WORKFLOW_CONFIG WC,");
        sql.append("      PROD.WORKFLOW_SECURITY WS");
        sql.append(" WHERE   WC.ID_WORKFLOW=").append(idWorkflow);
        sql.append("     AND WC.ID_CURRENT_STATE='").append(idWorkflowState).append("'");
        if (doNothingExclude) {
            sql.append("     AND WC.ID_WORKFLOW_EVENT<>'doNothing' ");
        }
        sql.append("     AND WS.ID_WORKFLOW_CONFIG=WC.ID_WORKFLOW_CONFIG");
        sql.append("     AND WS.ID_AUTHORITY IN(").append(strAuthorities).append(")");
        sql.append("     AND WS.ALLOW_STATE='").append((allowState) ? 't' : 'f').append("'");
        sql.append("     AND WS.ALLOW_EVENT='").append((allowEvent) ? 't' : 'f').append("'");
        return dao.find(WorkflowConfig.class, sql);
    }

    @Override
    public List<WorkflowState> getWorkflowState(final Long idWorkflow, final String[] authorities, final boolean allowState) {
        // String authoritiesString =
        StringBuilder strAuthorities = null;
        for (String authority : authorities) {
            if (strAuthorities == null) {
                strAuthorities = new StringBuilder();
                strAuthorities.append("'").append(authority).append("'");
            } else {
                strAuthorities.append(",'").append(authority).append("'");
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT WST.*");
        sql.append(" FROM PROD.WORKFLOW_STATE WST,");
        sql.append("      PROD.WORKFLOW_CONFIG WC,");
        sql.append("      PROD.WORKFLOW_SECURITY WS");
        sql.append(" WHERE    WC.ID_CURRENT_STATE=WST.ID_WORKFLOW_STATE");
        sql.append("      AND WC.ID_WORKFLOW=").append(idWorkflow);
        sql.append("      AND WS.ID_WORKFLOW_CONFIG=WC.ID_WORKFLOW_CONFIG");
        sql.append("      AND WS.ALLOW_STATE='").append((allowState) ? "t" : "f").append("'");
        sql.append("      AND WS.ID_AUTHORITY IN(").append(strAuthorities).append(")");
        return dao.find(WorkflowState.class, sql);
    }

    @Override
    public WorkflowState getWorkflowState(String idWorkflowState) {
        return dao.get(WorkflowState.class, idWorkflowState);
    }

    @Override
    public void deleteWorkflow(final Long idWorkflow) {
        dao.delete(dao.get(Workflow.class, idWorkflow));
    }

    @Override
    public Workflow getWorkflow(final Long idWorkflow) {
        return dao.get(Workflow.class, idWorkflow);
    }

    @Override
    public Workflow saveWorkflow(Workflow workflow) {
        return dao.persist(workflow);
    }

    @Override
    public void deleteWorkflowConfig(final Long idWorkflowConfig) {
        dao.delete(dao.get(WorkflowConfig.class, idWorkflowConfig));
    }

    @Override
    public WorkflowConfig saveWorkflowConfig(WorkflowConfig workflowConfig) {
        return dao.persist(workflowConfig);
    }

    @Override
    public WorkflowState saveWorkflowState(WorkflowState workflowState) {
        return dao.persist(workflowState);
    }

    @Override
    public void deleteWorkflowState(String idWorkflowState) {
        dao.delete(dao.get(WorkflowState.class, idWorkflowState));
    }

    @Override
    public List<WorkflowState> getState() {
        return dao.find(WorkflowState.class);
    }

    @Override
    public List<WorkflowState> getState(final Long... idWorkflow) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT WST.*");
        sql.append(" FROM PROD.WORKFLOW_STATE WST,");
        sql.append("      PROD.WORKFLOW_CONFIG WC");
        sql.append(" WHERE    (WC.ID_CURRENT_STATE=WST.ID_WORKFLOW_STATE OR WC.ID_NEXT_STATE=WST.ID_WORKFLOW_STATE)");
        sql.append("      AND WC.ID_WORKFLOW IN(?");
        if (idWorkflow.length > 1) {
            for (int idx = 1; idx < idWorkflow.length; idx++) {
                sql.append(", ?");
            }
        }
        sql.append(')');
        return dao.find(WorkflowState.class, sql, (Object[]) idWorkflow);
    }

    @Override
    public List<WorkflowConfig> getWorkflowConfig(final Long... idWorkflow) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT WC.*");
        sql.append(" FROM PROD.WORKFLOW_CONFIG WC");
        sql.append(" WHERE WC.ID_WORKFLOW IN(?");
        if (idWorkflow.length > 1) {
            for (int idx = 1; idx < idWorkflow.length; idx++) {
                sql.append(", ?");
            }
        }
        sql.append(')');
        return dao.find(WorkflowConfig.class, sql, (Object[]) idWorkflow);
    }
}
