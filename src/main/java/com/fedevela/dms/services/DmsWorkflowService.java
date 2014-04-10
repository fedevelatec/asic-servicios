package com.fedevela.dms.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.workflow.pojos.DmsWorkflowConfig;
import com.fedevela.core.workflow.pojos.DmsWorkflowSecurity;
import com.fedevela.core.workflow.pojos.DmsWorkflows;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import org.hibernate.criterion.DetachedCriteria;

@Deprecated
public interface DmsWorkflowService {

    /**
     *
     * @return
     */
    public List<DmsWorkflows> getWorkflows();

    /**
     *
     * @param criteria
     * @return
     */
    public List<DmsWorkflows> getWorkflows(DetachedCriteria criteria);

    /**
     *
     * @return
     */
    public List<DmsWorkflowConfig> getWorkflowConfigs();

    /**
     *
     * @param criteria
     * @return
     */
    public List<DmsWorkflowConfig> getWorkflowConfigs(DetachedCriteria criteria);

    /**
     *
     * @return
     */
    public List<DmsWorkflowSecurity> getWorkflowSecurity();

    /**
     *
     * @param criteria
     * @return
     */
    public List<DmsWorkflowSecurity> getWorkflowSecurity(DetachedCriteria criteria);

    /**
     *
     * @param idWorkflow
     * @param rols
     * @return
     */
    public List<String> getWFViewStatesByUserRols(Long idWorkflow, Set<BigInteger> rols);
}
