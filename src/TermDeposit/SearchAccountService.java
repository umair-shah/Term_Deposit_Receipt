package TermDeposit;
import Utilities.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class SearchAccountService {
	
	public AccountDTO ValidateAccount(String accountNo)
	{
		AccountDTO accountDTO = new AccountDTO();
		//SELECT A.Account_No, A.Title, A.Brn_Cd, B.BRN_NAME, B.TODAY_DATE, C.CURRENCY_NAME FROM BRANCH_TL B INNER JOIN ACCOUNT_TL A ON A.Brn_Cd = B.Brn_Cd INNER JOIN Currency C ON A.Curr_Cd_Id = C.Curr_Code WHERE A.Account_No = '123456789';
		String accountSearchQuery = "Select A.Account_No, A.Title, A.Brn_Cd, A.Balance, B.Brn_Name, B.Today_Date, C.Currency_Name, S.Desc" +
				" FROM BRANCH_TL B INNER JOIN ACCOUNT_TL A ON A.BRN_CD = B.BRN_CD INNER JOIN Currency C ON A.Curr_Cd_Id = C.CURR_Code INNER JOIN Account_Status S ON S.ID = A.STATUS_ID" +
				" WHERE A.ACCOUNT_NO = '" + accountNo + "' AND A.Brn_cd = '"+ Session.GetBranchCode() +"'";
		Connection lcl_conn_dt = utility.db_conn();
		ResultSet accountRs =null;
		java.sql.Statement lcl_stmt;
		try {
			 lcl_stmt= lcl_conn_dt.createStatement();
			 accountRs = lcl_stmt.executeQuery(accountSearchQuery);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		try 
		{
			if(accountRs.next())
			{
					accountDTO.SetResult(true);
					accountDTO.SetAccountNo(accountNo);
					accountDTO.SetAccountTitle(accountRs.getString("TITLE"));
					accountDTO.SetBranchCode(accountRs.getString("Brn_cd"));
					accountDTO.SetBranchName(accountRs.getString("Brn_Name"));
					accountDTO.SetBranchDate(accountRs.getString("Today_Date"));
					accountDTO.SetCurrency(accountRs.getString("Currency_Name"));
					accountDTO.SetBalance(accountRs.getFloat("Balance"));
					accountDTO.SetAccountStatus(accountRs.getString("Desc"));
			}
				
			else
			{
				accountDTO.SetResult(false);
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accountDTO;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
