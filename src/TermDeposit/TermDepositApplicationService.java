package TermDeposit;
import Utilities.utility;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Utilities.ComboItem;
import Utilities.utility;

public class TermDepositApplicationService {
	TermDepositSearchService TDRsearcservice = null;
	public void TermDepositApplicationService()
	{
		TDRsearcservice= new TermDepositSearchService();
	}
	
	public ResultSet GetModeofFunds()
	{
		Connection lcl_conn_dt = utility.db_conn();
		ResultSet modeOfFundRs=null;
		
		String modeOfFundQuery = "SELECT * FROM MODE_OF_FUND";
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement();
			 modeOfFundRs = lcl_stmt.executeQuery(modeOfFundQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return modeOfFundRs;
	}
	
	public ResultSet GetTenure()
	{
		Connection lcl_conn_dt = utility.db_conn();
		String tdrProductQuery = "SELECT * FROM tdr_Product";
		ResultSet tdrProductRs=null;
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement();
			 tdrProductRs = lcl_stmt.executeQuery(tdrProductQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return tdrProductRs;
	}
	
	public ResultSet GetActionatMaturity()
	{
		Connection lcl_conn_dt = utility.db_conn();
		String actionAtMaturityQuery = "SELECT * FROM Maturity_Action";
		ResultSet actionAtMaturityRs=null;
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement();
			 actionAtMaturityRs = lcl_stmt.executeQuery(actionAtMaturityQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return actionAtMaturityRs;
	}
	
	public boolean isAccountExist(String accountNo)
	{
		String query="Select Account_no from Account_tl where Account_no='"+accountNo+"'";
		Connection lcl_conn_dt = utility.db_conn();
		ResultSet accountRs =null;
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement();
			 accountRs = lcl_stmt.executeQuery(query);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try{
			if(accountRs.next())
			{
				return true;
			}
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
		return false;
	}

	public Date getMaturityDate(Date date, ComboItem selectedTenure)
	{
		String maturityDateQuery = "SELECT Add_Months('"+date+"',p.tenure) maturity_date FROM tdr_Product p where p.ID = '"+selectedTenure.getId()+"'";
		Connection lcl_conn_dt = utility.db_conn();
		ResultSet maturityDate =null;
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement();
			 maturityDate = lcl_stmt.executeQuery(maturityDateQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Date mdate=null;
		try {
			while(maturityDate.next())
			{
				 mdate= maturityDate.getDate("maturity_date");
			}
			return mdate;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int insertTDRApplication(TermDepositApplicationDTO TDADTO)
	{
		String todayDate = TDADTO.GetApplicationDate();
		java.sql.Date startDate=null;
		try {
			startDate= utility.toDate("yyyy-MM-dd", todayDate);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		ComboItem selectedTenure = TDADTO.GetSelectedTenure();
		Date mdate = getMaturityDate(startDate, selectedTenure);
		ComboItem selectedMOF = TDADTO.GetSelectedMOF();
		ComboItem selectedMaturityAction= TDADTO.GetSelectedMaturityAction();

		
		int applicationId=-1;
		Connection lcl_conn_dt = utility.db_conn();

        String query = "SELECT APPLICATION_ID FROM FINAL TABLE (INSERT INTO TDR_Application (Holder_name,Amount,Input_by,Maturity_date,Application_date,TDR_Request_DOC,TDR_App_status,Product_Id,Maturity_Action,Mode_of_fund,Principal_Fund_Crd_Acc,Prof_Nom_Acc,TDR_Request_Doc_Name,Account_no, Brn_Cd ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?))";
        try {
        	
        	PreparedStatement preparedStatement = lcl_conn_dt.prepareStatement(query);
        	preparedStatement.setString(1, TDADTO.GetAccountTitle());
            preparedStatement.setFloat(2, TDADTO.GetTDRAmount());
            preparedStatement.setString(3,Session.GetUserName().toString());
            preparedStatement.setDate(4, mdate);
            preparedStatement.setDate(5, startDate);
            preparedStatement.setBytes(6, TDADTO.GetFileData());
            preparedStatement.setInt(7,1);
            preparedStatement.setInt(8,selectedTenure.getId());
            preparedStatement.setInt(9,selectedMaturityAction.getId());
            preparedStatement.setInt(10,selectedMOF.getId());
            preparedStatement.setString(11,TDADTO.GetPrincipalFundCrAccount());
            preparedStatement.setString(12, TDADTO.GetProfitNomAccount());
            preparedStatement.setString(13,TDADTO.GetFileName());
            preparedStatement.setString(14,TDADTO.GetAccountNo());
            preparedStatement.setString(15,Session.GetBranchCode().toString());
            ResultSet rs = preparedStatement.executeQuery();
     		//ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
     		if(rs.next())
     		{
     			applicationId =rs.getInt(1); 
     		}
     		
   
           
        }catch(Exception exp)
        {
        	System.out.println(exp.getMessage());
        	exp.printStackTrace();
        }
        return applicationId;
	}
	

	public int updateTDRApplication(TermDepositApplicationDTO TDADTO)
	{
		String todayDate = TDADTO.GetApplicationDate();
		java.sql.Date startDate=null;
		try {
			startDate= utility.toDate("yyyy-MM-dd", todayDate);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		int rs=-1;
		ComboItem selectedTenure = TDADTO.GetSelectedTenure();
		Date mdate = getMaturityDate(startDate, selectedTenure);
		ComboItem selectedMOF = TDADTO.GetSelectedMOF();
		ComboItem selectedMaturityAction= TDADTO.GetSelectedMaturityAction();
		Connection lcl_conn_dt = utility.db_conn();

        String query = "update TDR_Application set Amount= ? ,Input_by= ?,Maturity_date= ?,Application_date= ?,TDR_Request_DOC= ? ,Product_Id =? ,Maturity_Action =? ,Mode_of_fund =? ,Principal_Fund_Crd_Acc =? ,Prof_Nom_Acc =? ,TDR_Request_Doc_Name =? where application_id= ?";
        try {
        	
        	PreparedStatement preparedStatement = lcl_conn_dt.prepareStatement(query);
            preparedStatement.setFloat(1, TDADTO.GetTDRAmount());
            preparedStatement.setString(2,Session.GetUserName().toString());
            preparedStatement.setDate(3, mdate);
            preparedStatement.setDate(4, startDate);
            preparedStatement.setBytes(5, TDADTO.GetFileData());
            preparedStatement.setInt(6,selectedTenure.getId());
            preparedStatement.setInt(7,selectedMaturityAction.getId());
            preparedStatement.setInt(8,selectedMOF.getId());
            preparedStatement.setString(9,TDADTO.GetPrincipalFundCrAccount());
            preparedStatement.setString(10, TDADTO.GetProfitNomAccount());
            preparedStatement.setString(11,TDADTO.GetFileName());
            preparedStatement.setString(12,TDADTO.GetApplicationNo());
            rs = preparedStatement.executeUpdate();
     		//ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        }catch(Exception exp)
        {
        	System.out.println(exp.getMessage());
        	exp.printStackTrace();
        }
        return rs;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
