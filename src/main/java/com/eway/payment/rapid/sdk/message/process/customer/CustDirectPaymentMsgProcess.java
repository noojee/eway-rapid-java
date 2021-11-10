package com.eway.payment.rapid.sdk.message.process.customer;

import javax.ws.rs.client.WebTarget;

import com.eway.payment.rapid.sdk.beans.external.Customer;
import com.eway.payment.rapid.sdk.beans.external.TransactionType;
import com.eway.payment.rapid.sdk.entities.CreateCustomerResponse;
import com.eway.payment.rapid.sdk.entities.DirectPaymentRequest;
import com.eway.payment.rapid.sdk.entities.DirectPaymentResponse;
import com.eway.payment.rapid.sdk.entities.Request;
import com.eway.payment.rapid.sdk.entities.Response;
import com.eway.payment.rapid.sdk.exception.RapidSdkException;
import com.eway.payment.rapid.sdk.message.convert.BeanConverter;
import com.eway.payment.rapid.sdk.message.convert.CustomerToInternalCustomerConverter;
import com.eway.payment.rapid.sdk.message.convert.response.DirectPaymentToCreateCustConverter;
import com.eway.payment.rapid.sdk.message.process.AbstractMakeRequestMessageProcess;
import com.eway.payment.rapid.sdk.util.Constant;

/**
 * Create customer with Direct Payment method
 */
public class CustDirectPaymentMsgProcess extends AbstractMakeRequestMessageProcess<Customer, CreateCustomerResponse>
{

    /**
     * @param resource
     *            The web resource to call Rapid API
     * @param requestPath
     *            Path of request URL. Used to make full web service URL
     */
    public CustDirectPaymentMsgProcess(WebTarget resource, String... requestPath)
    {
        super(resource, requestPath);
    }

    @Override
    protected Request createRequest(Customer input) throws RapidSdkException
    {
        DirectPaymentRequest request = new DirectPaymentRequest();
        BeanConverter<Customer, com.eway.payment.rapid.sdk.beans.internal.Customer> interCustConvert = new CustomerToInternalCustomerConverter(
                false);
        request.setCustomer(interCustConvert.doConvert(input));
        request.setCustomerIP(input.getCustomerDeviceIP());
        request.setMethod(Constant.CREATE_TOKEN_CUSTOMER_METHOD);
        request.setTransactionType(TransactionType.MOTO.name());
        return request;
    }

    @Override
    protected Response sendRequest(Request req) throws RapidSdkException
    {
        return doPost(req, DirectPaymentResponse.class);
    }

    @Override
    protected CreateCustomerResponse makeResult(Response res) throws RapidSdkException
    {
        DirectPaymentResponse response = (DirectPaymentResponse) res;
        DirectPaymentToCreateCustConverter converter = new DirectPaymentToCreateCustConverter();
        return converter.doConvert(response);
    }
}
