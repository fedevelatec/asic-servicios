package com.fedevela.dms.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.core.workflow.pojos.Workflow;
import com.fedevela.core.workflow.pojos.WorkflowConfig;
import com.fedevela.core.workflow.pojos.WorkflowState;
import java.util.List;

public interface WorkflowService {

    /**
     *
     * @param idWorkflow
     * @param idWorkflowState
     * @param idWorkflowEvent
     * @param doNothingExclude, No mostrar el evento doNothing.
     * @return
     */
    public WorkflowConfig getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String idWorkflowEvent);

    /**
     *
     * @param idWorkflow
     * @param idWorkflowState
     * @param idWorkflowEvent
     * @param authorities
     * @param doNothingExclude, No mostrar el evento doNothing.
     * @return
     */
    public WorkflowConfig getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String idWorkflowEvent,
            final String[] authorities,
            final boolean doNothingExclude);

    /**
     *
     * @param idWorkflow
     * @param idWorkflowState
     * @param idWorkflowEvent
     * @param authorities
     * @param doNothingExclude, No mostrar el evento doNothing.
     * @param allowState, Filtrar el permiso de estatus
     * @param allowEvent, Filtrar el permiso de evento
     * @return
     */
    public WorkflowConfig getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String idWorkflowEvent,
            final String[] authorities,
            final boolean doNothingExclude,
            final boolean allowState,
            final boolean allowEvent);

    /**
     *
     * @param idWorkflow
     * @param idWorkflowState
     * @param doNothingExclude, No mostrar el evento doNothing.
     * @return
     */
    public List<WorkflowConfig> getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final boolean doNothingExclude);

    /**
     *
     * @param idWorkflow
     * @param idWorkflowState
     * @param authorities
     * @param doNothingExclude, No mostrar el evento doNothing.
     * @return
     */
    public List<WorkflowConfig> getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String[] authorities,
            final boolean doNothingExclude);

    public List<WorkflowConfig> getWorkflowConfig(
            final Long idWorkflow,
            final String idWorkflowState,
            final String[] authorities,
            final boolean doNothingExclude,
            final boolean allowState,
            final boolean allowEvent);

    /**
     * Lista toda la configuración de uno o mas workflows.
     *
     * @param idWorkflow
     * @return
     */
    public List<WorkflowConfig> getWorkflowConfig(
            final Long... idWorkflow);

    public List<WorkflowState> getWorkflowState(
            final Long idWorkflow,
            final String[] authorities,
            final boolean allowState);

    /**
     *
     * @param idWorkflowState
     * @return
     */
    public WorkflowState getWorkflowState(final String idWorkflowState);

    public Workflow getWorkflow(final Long idWorkflow);

    public Workflow saveWorkflow(Workflow workflow);

    public void deleteWorkflow(final Long idWorkflow);

    public WorkflowConfig saveWorkflowConfig(WorkflowConfig workflowConfig);

    public void deleteWorkflowConfig(final Long idWorkflowConfig);

    public WorkflowState saveWorkflowState(WorkflowState workflowState);

    public void deleteWorkflowState(String idWorkflowState);

    public List<WorkflowState> getState();

    /**
     * Lista todos los estatus configurados de uno o mas workflows.
     *
     * @param idWorkflow
     * @return
     */
    public List<WorkflowState> getState(final Long... idWorkflow);
}
