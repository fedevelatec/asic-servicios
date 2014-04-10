package com.fedevela.dms.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Deprecated
public interface DmsService {

    /**
     *
     * @param idOperatoria
     * @param idDocument
     * @param idCategory
     * @return
     */
    public List<Object> getIncidents(final BigInteger idOperatoria, final BigInteger idDocument, final BigDecimal idCategory);

    /**
     *
     * @param idOperatoria
     * @param idDocument
     * @param idCategory
     * @param idRule
     * @return
     */
    public List<Object> getIncidents(final BigInteger idOperatoria, final BigInteger idDocument, final BigDecimal idCategory, final String idRule);

    /**
     *
     * @param idOperatoria
     * @param idCategory
     * @return
     */
    public List<Object> getCheckList(final BigInteger idOperatoria, final BigDecimal idCategory);

    /**
     *
     * @param idOperation
     * @param idProduct
     * @param idGroup
     * @return
     */
    public List<Object> getChecklist(BigInteger idOperation, BigInteger idProduct, BigInteger idGroup);

    /**
     *
     * @param idOperation
     * @param idProduct
     * @param idGroup
     * @return
     */
    public List<Object> getChecklistRules(BigInteger idOperation, BigInteger idProduct, BigInteger idGroup);

    /**
     *
     * @param idOperation
     * @param idProduct
     * @param idDocument
     * @param idGroup
     * @return
     */
    public List<Object> getIncidents(BigInteger idOperation, BigInteger idProduct, BigInteger idDocument, BigInteger idGroup);

    /**
     *
     * @param idOperation
     * @param idProduct
     * @param idDocument
     * @return
     */
    public List<Object> getIncidentRules(BigInteger idOperation, BigInteger idProduct, BigInteger idDocument);

    /**
     *
     * @param idOperation
     * @return
     */
    public List<Object> getOperationProductRules(BigInteger idOperation);
}
