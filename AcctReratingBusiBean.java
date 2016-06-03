package com.ailk.ims.business.acctrerating.busi;

import java.util.ArrayList;
import java.util.List;
import com.ailk.easyframe.web.common.exception.BaseException;
import com.ailk.ims.common.BaseCancelableBusiBean;
import com.ailk.ims.component.AccountComponent;
import com.ailk.ims.define.EnumCodeDefine;
import com.ailk.ims.define.ErrorCodeDefine;
import com.ailk.ims.exception.IMSException;
import com.ailk.ims.ims3h.Acct3hBean;
import com.ailk.ims.ims3h.IMS3hBean;
import com.ailk.ims.util.CommonUtil;
import com.ailk.ims.util.DateUtil;
import com.ailk.ims.util.IMSUtil;
import com.ailk.openbilling.persistence.imsinner.entity.Do_acctReratingResponse;
import com.ailk.openbilling.persistence.imsinner.entity.SAcctReratingReg;
import com.ailk.openbilling.persistence.imsintf.entity.BaseResponse;
import com.ailk.openbilling.persistence.imsintf.entity.CancelOrderInfo;
import com.ailk.openbilling.persistence.imsintf.entity.ModifyAcctReq;

/**
 * @Description: 设置账户的rerating_flag
 * @Company: Asiainfo-Linkage Technologies(China),Inc. Hangzhou
 * @Author tangjl5
 * @Date 2011-11-17
 * @Date 2012-4-11 tangjl5 根据flag账户status设置为rerating或者或者回复为active
 */
public class AcctReratingBusiBean extends BaseCancelableBusiBean
{
    private SAcctReratingReg req = null;

    @Override
    public void cancel(CancelOrderInfo cancelInfo)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(Object... input) throws IMSException
    {
        req = (SAcctReratingReg) input[0];

    }

    @Override
    public void validate(Object... input) throws IMSException
    {
        SAcctReratingReg req = (SAcctReratingReg) input[0];
        if (!CommonUtil.isValid(req.getAcct_id()))
        {
            IMSUtil.throwBusiException(ErrorCodeDefine.COMMON_PARAM_ISNULL, "acct_id");
        }

        if (null == req.getRerating_flag())
        {
            IMSUtil.throwBusiException(ErrorCodeDefine.COMMON_PARAM_ISNULL, "rerating_flag");
        }
    }

    @Override
    public Object[] doBusiness(Object... input) throws IMSException
    {
        SAcctReratingReg req = (SAcctReratingReg) input[0];

        // @Date 2012-4-11 tangjl5 根据flag账户status设置为rerating或者或者回复为active
        ModifyAcctReq modifyAcctReq = new ModifyAcctReq();
        modifyAcctReq.setAcct_id(req.getAcct_id());
        if (req.getRerating_flag() == EnumCodeDefine.ACCOUNT_RERATING_FLAG_FALSE)
        {
            modifyAcctReq.setNew_state((short)EnumCodeDefine.ACCOUNT_ACTIVE);
        }
        else
        {
            modifyAcctReq.setNew_state((short)EnumCodeDefine.ACCOUNT_RERATING);
        }
        modifyAcctReq.setAction_date(DateUtil.formatDate(context.getRequestDate(), DateUtil.DATE_FORMAT_EN_B_YYYYMMDDHHMMSS));
        context.getComponent(AccountComponent.class).modifyAcctStatus(modifyAcctReq);
        return null;
    }

    @Override
    public BaseResponse createResponse(Object[] result)
    {
        return new Do_acctReratingResponse();
    }

    @Override
    public void destroy()
    {
        // TODO Auto-generated method stub

    }

    public List<IMS3hBean> createMain3hBeans(Object... input) throws BaseException
    {
        List<IMS3hBean> beans = new ArrayList<IMS3hBean>();
        Acct3hBean acctBean = context.get3hTree().loadAcct3hBean(req.getAcct_id());
        beans.add(acctBean);
        return beans;
    }
}
