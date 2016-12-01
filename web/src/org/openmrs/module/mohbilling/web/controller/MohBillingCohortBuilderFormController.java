package org.openmrs.module.mohbilling.web.controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohbilling.businesslogic.ConsommationUtil;
import org.openmrs.module.mohbilling.businesslogic.DepartementUtil;
import org.openmrs.module.mohbilling.businesslogic.InsurancePolicyUtil;
import org.openmrs.module.mohbilling.businesslogic.InsuranceUtil;
import org.openmrs.module.mohbilling.businesslogic.ReportsUtil;
import org.openmrs.module.mohbilling.businesslogic.ThirdPartyBillUtil;
import org.openmrs.module.mohbilling.model.AllServicesRevenue;
import org.openmrs.module.mohbilling.model.Consommation;
import org.openmrs.module.mohbilling.model.Department;
import org.openmrs.module.mohbilling.model.GlobalBill;
import org.openmrs.module.mohbilling.model.Insurance;
import org.openmrs.module.mohbilling.model.ServiceRevenue;
import org.openmrs.module.mohbilling.model.ThirdParty;
import org.openmrs.module.mohbilling.service.BillingService;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;


public class MohBillingCohortBuilderFormController extends
		ParameterizableViewController {
	protected final Log log = LogFactory.getLog(getClass());
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		ModelAndView mav = new ModelAndView();
			mav.setViewName(getViewName());


			if (request.getParameter("formStatus") != null
					&& !request.getParameter("formStatus").equals("")) {
				String startDateStr = request.getParameter("startDate");
				String startHourStr = request.getParameter("startHour");
				String startMinStr = request.getParameter("startMinute");
				
				String endDateStr = request.getParameter("endDate");
				String endHourStr = request.getParameter("endHour");
				String endMinuteStr = request.getParameter("endMinute");
				
				String collectorStr = null;
				String insuranceStr = null;
				String thirdPartyStr = null;
				String billStatus = null;
				String departmentStr = null;
				
				
				 User creator = null;
				 Department department = null;
				
				String billCreatorStr = null;
				if(request.getParameter("billCreator")!=null &&!request.getParameter("billCreator").equals("")){
					billCreatorStr=request.getParameter("billCreator");
					creator = Context.getUserService().getUser(Integer.valueOf(billCreatorStr));
				}
				if(request.getParameter("billStatus")!=null&&!request.getParameter("billStatus").equals(""))
				 billStatus = request.getParameter("billStatus");
				
				if(request.getParameter("departmentId")!=null&&!request.getParameter("departmentId").equals("")){
				 departmentStr = request.getParameter("departmentId");
				 department = DepartementUtil.getDepartement(Integer.valueOf(departmentStr));
				}
				
				 // parameters
				 Object[] params = ReportsUtil.getReportParameters(request, startDateStr, startHourStr, startMinStr, endDateStr, endHourStr, endMinuteStr, collectorStr, insuranceStr, thirdPartyStr);
				
				 Date startDate = (Date) params[0];
				 Date endDate = (Date) params[1];
				 
				 Insurance insurance = null;
				 ThirdParty thirdParty =null;
				 if(request.getParameter("insuranceId")!=null &&!request.getParameter("insuranceId").equals("") ){
					 insurance = InsuranceUtil.getInsurance(Integer.valueOf(request.getParameter("insuranceId")));
				 }
				 if(request.getParameter("thirdPartyId")!=null && !request.getParameter("thirdPartyId").equals(""))
				 thirdParty = Context.getService(BillingService.class).getThirdParty(Integer.valueOf(request.getParameter("thirdPartyId")));
				
				 List<Consommation> cons = ConsommationUtil.getConsommations(startDate, endDate, insurance, thirdParty, creator, department);
				 mav.addObject("consommations", cons);
							
		}
			mav.addObject("insurances", InsuranceUtil.getAllInsurances());
			mav.addObject("thirdParties", InsurancePolicyUtil.getAllThirdParties());
			mav.addObject("departments", DepartementUtil.getAllHospitalDepartements());
			return mav;
	}

}
