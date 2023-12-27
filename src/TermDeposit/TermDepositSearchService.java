package TermDeposit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Utilities.ComboItem;
import Utilities.utility;

public class TermDepositSearchService {

	public Object[][] GetUnauthorizedApplication() throws SQLException
	{
//		List<Object[]> result = new ArrayList<>();
		Connection lcl_conn_dt = utility.db_conn();
		ResultSet TDRapplications=null;
		
		String tdrAppQuery = "SELECT tdr.Account_no,p.Desc,tdr.Application_id,tdr.Amount,tdr.Maturity_date,tdr.Application_date,tdr.Principal_fund_crd_acc, tdr.prof_nom_acc, tdr.Brn_cd FROM TDR_Application tdr inner join TDR_Product p on tdr.product_id = p.ID WHERE tdr.Brn_cd = '"+ 
		Session.GetBranchCode() +"'";
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			 TDRapplications = lcl_stmt.executeQuery(tdrAppQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		TDRapplications.last();
         int rowCount = TDRapplications.getRow();
         TDRapplications.beforeFirst();

         // Assuming two columns: name and age
         int columnCount = 8;

         // Declare and initialize a 2D array
         Object[][] data = new Object[rowCount][columnCount];

         // Populate the array with data from the ResultSet
         int rowIndex = 0;
         while (TDRapplications.next()) {   
        	 data[rowIndex][0] = TDRapplications.getString("Account_no");
             data[rowIndex][1] = TDRapplications.getString("Application_id");
             data[rowIndex][2] = TDRapplications.getString("Desc");
             data[rowIndex][3] = TDRapplications.getFloat("Amount");
             data[rowIndex][4] = TDRapplications.getString("Maturity_date");
             data[rowIndex][5] = TDRapplications.getString("Application_date");
             data[rowIndex][6] = TDRapplications.getString("Principal_fund_crd_acc");
             data[rowIndex][7] = TDRapplications.getString("prof_nom_acc");
          
             rowIndex++;
         }
		return data;
	}
	public TermDepositApplicationDTO GetTDRAppDetails(int TDRNo)
	{
		Connection lcl_conn_dt = utility.db_conn();
		ResultSet TDRAppDetail=null;
		
		String tdrAppQuery = "SELECT tdr.*,acc.*,brn.brn_name,curr.Currency_name from TDR_Application tdr inner join Account_tl acc on tdr.account_no=acc.account_no inner join Branch_tl brn on brn.brn_cd=acc.brn_cd inner join Currency curr on acc.curr_cd_id=curr.curr_code where tdr.application_id = '"+TDRNo+"'";
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			 TDRAppDetail = lcl_stmt.executeQuery(tdrAppQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AccountDTO tdrFundAcc= new AccountDTO();
        TermDepositApplicationDTO tdrAppDto= new TermDepositApplicationDTO();
        try{
         if(TDRAppDetail.next()) {   
        	 tdrFundAcc.SetAccountNo(TDRAppDetail.getString("Account_no"));
        	 tdrFundAcc.SetAccountTitle(TDRAppDetail.getString("Holder_name"));
        	 tdrFundAcc.SetBranchCode(TDRAppDetail.getString("Brn_CD"));
        	 tdrFundAcc.SetBranchName(TDRAppDetail.getString("Brn_name"));
        	 tdrFundAcc.SetCurrency(TDRAppDetail.getString("Currency_name"));
        	 tdrFundAcc.SetBalance(TDRAppDetail.getFloat("Balance"));
        	 tdrAppDto.SetAccountNo(TDRAppDetail.getString("Account_no"));
        	 tdrAppDto.SetAccountTitle(TDRAppDetail.getString("Holder_name"));
        	 tdrAppDto.SetApplicationDate (TDRAppDetail.getString("Application_date"));
        	 tdrAppDto.SetAccountTitle(TDRAppDetail.getString("Holder_name"));
        	 tdrAppDto.SetTDRAmount(TDRAppDetail.getFloat("Amount"));
        	 tdrAppDto.SetApplicationDate(TDRAppDetail.getString("Application_date"));
        	 tdrAppDto.SetFiledata(TDRAppDetail.getBytes("TDR_Request_Doc"),TDRAppDetail.getString("TDR_Request_Doc_Name") );
        	 tdrAppDto.SetPricipalFundCrAccount(TDRAppDetail.getString("Principal_fund_crd_acc"));
        	 tdrAppDto.SetProfitNomAccount(TDRAppDetail.getString("Prof_Nom_Acc"));
        	 tdrAppDto.SetSelectedMaturityAction(new ComboItem(TDRAppDetail.getInt("Maturity_Action"),"test"));
        	 tdrAppDto.SetSelectedMOF(new ComboItem(TDRAppDetail.getInt("Mode_of_Fund"),"test"));
        	 tdrAppDto.SetSelectedTenure(new ComboItem(TDRAppDetail.getInt("product_id"),"test"));
        	 tdrAppDto.SetTdrAccount(tdrFundAcc);
         }
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
		return tdrAppDto;
	} 
	
}

