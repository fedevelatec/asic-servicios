package com.fedevela.dms.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.dms.services.DmsService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class DmsServiceImpl implements DmsService {

    @Resource
    private DmsDao dao;

    @Override
    public List<Object> getIncidents(BigInteger idOperatoria, BigInteger idDocument, BigDecimal idCategory) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsIncidents");
        sb.append(" WHERE    dmsIncidentsPK.idOperatoria=").append(idOperatoria);
        sb.append("      and dmsIncidentsPK.idDocument=").append(idDocument);
        sb.append("      and dmsIncidentsPK.idCategory=").append(idCategory);
        sb.append(" ORDER BY showIndex");
        return dao.find(sb.toString());
    }

    @Override
    public List<Object> getIncidents(BigInteger idOperatoria, BigInteger idDocument, BigDecimal idCategory, String idRule) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsIncidents");
        sb.append(" WHERE    dmsIncidentsPK.idOperatoria=").append(idOperatoria);
        sb.append("      and dmsIncidentsPK.idDocument=").append(idDocument);
        sb.append("      and dmsIncidentsPK.idCategory=").append(idCategory);
        sb.append("      and dmsIncidentsPK.idRule='").append(idRule).append("'");
        sb.append(" ORDER BY showIndex");
        return dao.find(sb.toString());
    }

    @Override
    public List<Object> getCheckList(BigInteger idOperatoria, BigDecimal idCategory) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsCheckList");
        sb.append(" WHERE    dmsCheckListPK.idOperatoria=").append(idOperatoria);
        sb.append("      and dmsCheckListPK.idCategory=").append(idCategory);
        sb.append(" ORDER BY showIndex");
        return dao.find(sb.toString());
    }








    @Override
    public List<Object> getChecklist(BigInteger idOperation, BigInteger idProduct, BigInteger idGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsChecklist cl");
        sb.append(" WHERE    cl.dmsChecklistPK.idOperation=").append(idOperation);
        sb.append("      and cl.dmsChecklistPK.idProduct=").append(idProduct);
        sb.append("      and cl.dmsChecklistPK.idGroup=").append(idGroup);
        sb.append(" ORDER BY cl.eIndex ASC");
        return dao.find(sb.toString());
    }

    @Override
    public List<Object> getChecklistRules(BigInteger idOperation, BigInteger idProduct, BigInteger idGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsChecklistRules");
        sb.append(" WHERE    dmsChecklistRulesPK.idOperation=").append(idOperation);
        sb.append("      and dmsChecklistRulesPK.idProduct=").append(idProduct);
        sb.append("      and dmsChecklistRulesPK.idGroup=").append(idGroup);
        sb.append(" ORDER BY eIndex");
        return dao.find(sb.toString());
    }

    @Override
    public List<Object> getIncidents(BigInteger idOperation, BigInteger idProduct, BigInteger idDocument, BigInteger idGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsIncidents");
        sb.append(" WHERE    dmsIncidentsPK.idOperation=").append(idOperation);
        sb.append("      and dmsIncidentsPK.idProduct=").append(idProduct);
        sb.append("      and dmsIncidentsPK.idDocument=").append(idDocument);
        sb.append("      and dmsIncidentsPK.idGroup=").append(idGroup);
        return dao.find(sb.toString());
    }

    @Override
    public List<Object> getIncidentRules(BigInteger idOperation, BigInteger idProduct, BigInteger idDocument) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsIncidentRules");
        sb.append(" WHERE    dmsIncidentRulesPK.idOperation=").append(idOperation);
        sb.append("      and dmsIncidentRulesPK.idProduct=").append(idProduct);
        sb.append("      and dmsIncidentRulesPK.idDocument=").append(idDocument);
        sb.append(" ORDER BY eIndex");
        return dao.find(sb.toString());
    }

    @Override
    public List<Object> getOperationProductRules(BigInteger idOperation) {
        StringBuilder sb = new StringBuilder();
        sb.append(" FROM DmsOperationProductRules");
        sb.append(" WHERE    dmsOperationProductRulesPK.idOperation=").append(idOperation);
        return dao.find(sb.toString());
    }

}
