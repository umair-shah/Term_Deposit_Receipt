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
	
	public String insertTDRApplication(TermDepositApplicationDTO TDADTO)
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

		
		String applicationSno;
		String year;
		String applicationNo=null;
		Connection lcl_conn_dt = utility.db_conn();

        String insertQuery = "SELECT lpad(APPLICATION_ID,5,'0'),Year(Application_date) FROM FINAL TABLE (INSERT INTO TDR_Application (Holder_name,Amount,Input_by,Maturity_date,Application_date,TDR_Request_DOC,TDR_App_status,Product_Id,Maturity_Action,Mode_of_fund,Principal_Fund_Crd_Acc_ID,Prof_Nom_Acc_ID,TDR_Request_Doc_Name,Account_ID ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?))";
        String updateBlockAmnt="Update Account_tl set block_Amnt=block_Amnt+ ? where account_id= ? and balance >= ? ";
        try {
    		lcl_conn_dt.setAutoCommit(false);
    		lcl_conn_dt.setTransactionIsolation(lcl_conn_dt.TRANSACTION_READ_COMMITTED);
        	PreparedStatement preparedStatement1 = lcl_conn_dt.prepareStatement(insertQuery);
        	PreparedStatement preparedStatement2 = lcl_conn_dt.prepareStatement(updateBlockAmnt);
        	preparedStatement1.setString(1, TDADTO.GetAccountTitle());
        	preparedStatement1.setFloat(2, TDADTO.GetTDRAmount());
        	preparedStatement1.setString(3,Session.GetUserName().toString());
        	preparedStatement1.setDate(4, mdate);
        	preparedStatement1.setDate(5, startDate);
        	preparedStatement1.setBytes(6, TDADTO.GetFileData());
        	preparedStatement1.setInt(7,1);
        	preparedStatement1.setInt(8,selectedTenure.getId());
        	preparedStatement1.setInt(9,selectedMaturityAction.getId());
        	preparedStatement1.setInt(10,selectedMOF.getId());
        	preparedStatement1.setLong(11, Long.parseLong(TDADTO.GetAccountID()));
        	preparedStatement1.setLong(12, Long.parseLong(TDADTO.GetAccountID()));
        	preparedStatement1.setString(13,TDADTO.GetFileName());
        	preparedStatement1.setString(14,TDADTO.GetAccountID());
            
        	preparedStatement2.setFloat(1, TDADTO.GetTDRAmount());
        	preparedStatement2.setInt(2, Integer.parseInt(TDADTO.GetAccountID()));
        	preparedStatement2.setFloat(3, TDADTO.GetTDRAmount());

            ResultSet rs = preparedStatement1.executeQuery();
            int updatecheck=preparedStatement2.executeUpdate();
            
     		//ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
     		if(rs.next() && updatecheck >=1 )
     		{
     			lcl_conn_dt.commit();
     			applicationSno =rs.getString(1);
     			year = rs.getString(2);
     			applicationNo=applicationSno + "/" +  TDADTO.GetAccountNo().substring(8, 14)+"/"+year;
     			
     		}
     		else{
     			lcl_conn_dt.rollback();
     		}
           
        }catch(Exception exp)
        {
        	
        	System.out.println(exp.getMessage());
        	exp.printStackTrace();
        }
        return applicationNo;
	}
	

	public int updateTDRApplication(TermDepositApplicationDTO TDADTO,float prevAmnt)
	{
		String todayDate = TDADTO.GetApplicationDate();
		java.sql.Date startDate=null;
		try {
			startDate= utility.toDate("yyyy-MM-dd", todayDate);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		int rs=-1;
		float diff= TDADTO.GetTDRAmount() - prevAmnt;
		ComboItem selectedTenure = TDADTO.GetSelectedTenure();
		Date mdate = getMaturityDate(startDate, selectedTenure);
		ComboItem selectedMOF = TDADTO.GetSelectedMOF();
		ComboItem selectedMaturityAction= TDADTO.GetSelectedMaturityAction();
		Connection lcl_conn_dt = utility.db_conn();

        String query = "update TDR_Application set Amount= ? ,Input_by= ?,Maturity_date= ?,Application_date= ?,TDR_Request_DOC= ? ,Product_Id =? ,Maturity_Action =? ,Mode_of_fund =? ,Principal_Fund_Crd_Acc_Id =? ,Prof_Nom_Acc_Id =? ,TDR_Request_Doc_Name =? where application_id= ? ";
        String updateBlockAmnt="Update Account_tl set block_Amnt=block_Amnt+ ? where account_id= ? and balance >= ? ";

        try {
    		lcl_conn_dt.setAutoCommit(false);
    		lcl_conn_dt.setTransactionIsolation(lcl_conn_dt.TRANSACTION_READ_COMMITTED);
    		
        	PreparedStatement preparedStatement = lcl_conn_dt.prepareStatement(query);
        	PreparedStatement preparedStatement1 = lcl_conn_dt.prepareStatement(updateBlockAmnt);

            preparedStatement.setFloat(1, TDADTO.GetTDRAmount());
            preparedStatement.setString(2,Session.GetUserName().toString());
            preparedStatement.setDate(3, mdate);
            preparedStatement.setDate(4, startDate);
            preparedStatement.setBytes(5, TDADTO.GetFileData());
            preparedStatement.setInt(6,selectedTenure.getId());
            preparedStatement.setInt(7,selectedMaturityAction.getId());
            preparedStatement.setInt(8,selectedMOF.getId());
            preparedStatement.setLong(9,Long.parseLong(TDADTO.GetAccountID()));
            preparedStatement.setLong(10,Long.parseLong(TDADTO.GetAccountID()));
            preparedStatement.setString(11,TDADTO.GetFileName());
            preparedStatement.setInt(12,Integer.parseInt(TDADTO.GetApplicationNo().substring(0,5)));
            
        	preparedStatement1.setFloat(1, diff);
        	preparedStatement1.setInt(2, Integer.parseInt(TDADTO.GetAccountID()));
        	preparedStatement1.setFloat(3, TDADTO.GetTDRAmount());
            
        	
            rs = preparedStatement.executeUpdate();
            int updatecheck=preparedStatement1.executeUpdate();
            
     		if(rs >=1  && updatecheck >=1 )
     		{
     			lcl_conn_dt.commit();
     			return 1;
     		}
     		else{
     			lcl_conn_dt.rollback();
     		}
     		//ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        }catch(Exception exp)
        {
        	System.out.println(exp.getMessage());
        	exp.printStackTrace();
        }
        return -1;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
