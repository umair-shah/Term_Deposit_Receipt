package TermDeposit;
import Utilities.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
public class TermDepositApplication {
	private JFrame frame;
	private JPanel panel;
	private JTextField accountTitleField;
	private JTextField branchNameField;
	private JTextField accountNoField;
	private JTextField currencyField;
	private JTextField branchCodeField;
	private JTextField dateField;
	private MaxLengthAmountField totalAmountField;
	private JTextField profitNomAccountField;
	private JTextField principalFundCrField;
	private JLabel lblAccountTitle;
	private JLabel lblBranchName;
	private JLabel lblAccountNo;
	private JLabel lblCurrency;
	private JLabel lblBranchCode;
	private JLabel lblDate;
	private JLabel lblModeOfFund;
	private JComboBox<ComboItem> modeOfFundComboBox;
	private JLabel lblTotalAmount;
	private JLabel lblTenure;
	private JComboBox<ComboItem> tenureComboBox;
	private JLabel lblActionAtMaturity;
	private JComboBox<ComboItem> actionAtMaturityComboBox;
	private JLabel lblProfitNomAccount;
	private JLabel lblPrincipalFundCr;
	private JButton saveButton;
	private JLabel lblFileName;
	private UploadFile filehandler;
	TermDepositApplicationService tdrService;
	private JButton updatebtn;
	
	public TermDepositApplication(AccountDTO accountDetails)
	{
		filehandler=new UploadFile();
		tdrService = new TermDepositApplicationService();
		CreateWindow();
		NewTDA(accountDetails);
		
	}
	public TermDepositApplication(TermDepositApplicationDTO TDRApplication)
	{
		filehandler=new UploadFile();
		tdrService = new TermDepositApplicationService();
		CreateWindow();
		UpdateTDA(TDRApplication);
	}
	
	public void CreateWindow()
	{
		frame = new JFrame("Term Deposit Application");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(662,587);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		panel = new JPanel();
		panel.setBackground(new Color(0, 128, 128));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		accountTitleField = new JTextField();
		accountTitleField.setEditable(false);
		accountTitleField.setBounds(113, 30, 202, 20);
		panel.add(accountTitleField);
		accountTitleField.setColumns(10);
		
		branchNameField = new JTextField();
		branchNameField.setEditable(false);
		branchNameField.setBounds(436, 30, 210, 20);
		panel.add(branchNameField);
		branchNameField.setColumns(10);
		
		lblAccountTitle = new JLabel("Account Title");
		lblAccountTitle.setBounds(28, 33, 96, 14);
		panel.add(lblAccountTitle);
		
		lblBranchName = new JLabel("Branch Name");
		lblBranchName.setBounds(340, 33, 100, 14);
		panel.add(lblBranchName);
		
		lblAccountNo = new JLabel("Account No");
		lblAccountNo.setBounds(28, 78, 96, 14);
		panel.add(lblAccountNo);
		
		accountNoField = new JTextField();
		accountNoField.setEditable(false);
		accountNoField.setColumns(10);
		accountNoField.setBounds(113, 75, 202, 20);
		panel.add(accountNoField);
		
		lblCurrency = new JLabel("Currency");
		lblCurrency.setBounds(340, 78, 100, 14);
		panel.add(lblCurrency);
		
		currencyField = new JTextField();
		currencyField.setEditable(false);
		currencyField.setColumns(10);
		currencyField.setBounds(436, 75, 210, 20);
		panel.add(currencyField);
		
		lblBranchCode = new JLabel("Branch Code");
		lblBranchCode.setBounds(28, 128, 96, 14);
		panel.add(lblBranchCode);
		
		branchCodeField = new JTextField();
		branchCodeField.setEditable(false);
		branchCodeField.setColumns(10);
		branchCodeField.setBounds(113, 125, 130, 20);
		panel.add(branchCodeField);
		
		lblDate = new JLabel("Date");
		lblDate.setBounds(340, 128, 100, 14);
		panel.add(lblDate);
		
		dateField = new JTextField();
		dateField.setEditable(false);
		dateField.setColumns(10);
		dateField.setBounds(436, 125, 153, 20);
		panel.add(dateField);
		
		lblModeOfFund = new JLabel("Mode of Fund");
		lblModeOfFund.setBounds(28, 175, 96, 14);
		panel.add(lblModeOfFund);
		
		modeOfFundComboBox = new JComboBox<ComboItem>();
		modeOfFundComboBox.setBounds(194, 172, 191, 20);
		panel.add(modeOfFundComboBox);
		
		
		lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setBounds(28, 221, 96, 14);
		panel.add(lblTotalAmount);
		
		totalAmountField = new MaxLengthAmountField(16);
		totalAmountField.setColumns(10);
		totalAmountField.setBounds(194, 218, 191, 20);
		panel.add(totalAmountField);
		
		lblTenure = new JLabel("Tenure");
		lblTenure.setBounds(28, 273, 96, 14);
		panel.add(lblTenure);
		
		tenureComboBox = new JComboBox<ComboItem>();
		tenureComboBox.setBounds(194, 270, 191, 20);
		panel.add(tenureComboBox);
		
		lblActionAtMaturity = new JLabel("Action at Maturity");
		lblActionAtMaturity.setBounds(28, 330, 137, 14);
		panel.add(lblActionAtMaturity);
		
		actionAtMaturityComboBox = new JComboBox<ComboItem>();
		actionAtMaturityComboBox.setBounds(194, 327, 304, 20);
		panel.add(actionAtMaturityComboBox);
		
		lblProfitNomAccount = new JLabel("Profit Nom Account");
		lblProfitNomAccount.setBounds(28, 387, 137, 14);
		panel.add(lblProfitNomAccount);
		
		profitNomAccountField = new JTextField();
		profitNomAccountField.setColumns(10);
		profitNomAccountField.setBounds(194, 384, 304, 20);
		profitNomAccountField.setEditable(false);
		panel.add(profitNomAccountField);
		
		lblPrincipalFundCr = new JLabel("Principal Fund Cr Account");
		lblPrincipalFundCr.setBounds(28, 447, 156, 14);
		panel.add(lblPrincipalFundCr);
		
		principalFundCrField = new JTextField();
		principalFundCrField.setColumns(10);
		principalFundCrField.setBounds(194, 447, 304, 20);
		principalFundCrField.setEditable(false);
		panel.add(principalFundCrField);
		
		
		lblFileName = new JLabel("File Name");
		lblFileName.setBounds(138, 505, 164, 14);
		
		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					String Filename=filehandler.selectFile();
					lblFileName.setText(Filename);
				}
				catch(Exception e){
					//System.out.println(e.getMessage());
					e.printStackTrace();
				}

			}
		});
		btnSelectFile.setBounds(28, 501, 89, 23);
		panel.add(btnSelectFile);
		
		JLabel lblMaxMbjpgjpegpngpdf = new JLabel("Max(5 MB) *JPG,*JPEG,*PNG,*PDF");
		lblMaxMbjpgjpegpngpdf.setLabelFor(btnSelectFile);
		lblMaxMbjpgjpegpngpdf.setBounds(28, 535, 177, 14);
		panel.add(lblMaxMbjpgjpegpngpdf);
		

		panel.add(lblFileName);
		
		
		
		
		ResultSet modeOfFundRs = tdrService.GetModeofFunds();
		try 
		{
			while(modeOfFundRs.next())
			{
				modeOfFundComboBox.addItem(new ComboItem(modeOfFundRs.getInt("ID"),modeOfFundRs.getString("Desc")));
			}
		} 
		catch (SQLException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ResultSet tdrProductRs = tdrService.GetTenure();
		try 
		{
			while(tdrProductRs.next())
			{
				tenureComboBox.addItem(new ComboItem(tdrProductRs.getInt("ID"),tdrProductRs.getString("Desc")));
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResultSet actionAtMaturityRs = tdrService.GetActionatMaturity();
		try 
		{
			while(actionAtMaturityRs.next())
			{
				actionAtMaturityComboBox.addItem(new ComboItem(actionAtMaturityRs.getInt("ID"),actionAtMaturityRs.getString("Desc")));
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		panel.repaint();
	}

	public void UpdateTDA(final TermDepositApplicationDTO TDRAppDto)
	{
		
		updatebtn = new JButton("Update");
		updatebtn.setBounds(471, 501, 117, 23);
		panel.add(updatebtn);
		panel.repaint();
		final AccountDTO accdto=TDRAppDto.GetAccountDTO();
		
		accountNoField.setText(TDRAppDto.GetAccountNo());
		accountTitleField.setText(TDRAppDto.GetAccountTitle());
		branchCodeField.setText(accdto.GetBranchCode());
		branchNameField.setText(accdto.GetBranchName());
		dateField.setText(TDRAppDto.GetApplicationDate());
		currencyField.setText(accdto.GetCurrency());
		int i=0;
		while(i < modeOfFundComboBox.getItemCount())
		{
			if(modeOfFundComboBox.getItemAt(i).getId()== TDRAppDto.GetSelectedMOF().getId())
			{
				modeOfFundComboBox.setSelectedItem(modeOfFundComboBox.getItemAt(i));
			}
			i++;
		}
		totalAmountField.setText(String.valueOf(TDRAppDto.GetTDRAmount()));
		i=0;
		while(i < tenureComboBox.getItemCount())
		{
			if(tenureComboBox.getItemAt(i).getId()== TDRAppDto.GetSelectedTenure().getId())
			{
				tenureComboBox.setSelectedItem(tenureComboBox.getItemAt(i));
			}
			i++;
		}
		i=0;
		while(i < actionAtMaturityComboBox.getItemCount())
		{
			if(actionAtMaturityComboBox.getItemAt(i).getId()== TDRAppDto.GetSelectedMaturityAction().getId())
			{
				actionAtMaturityComboBox.setSelectedItem(actionAtMaturityComboBox.getItemAt(i));
			}
			i++;
		}
		profitNomAccountField.setText(TDRAppDto.GetProfitNomAccount());
		principalFundCrField.setText(TDRAppDto.GetPrincipalFundCrAccount());
		lblFileName.setText(TDRAppDto.GetFileName());
		
		updatebtn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent updatebtnClicked) {
			if(totalAmountField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Amount is required!","Enter Amount",JOptionPane.ERROR_MESSAGE);
			}
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)") ||
			   actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal"))
			   && (profitNomAccountField.getText().isEmpty()))
			{
					JOptionPane.showMessageDialog(frame, "Profit Nomination Account is reuired!","Enter Profit Nomination Account",JOptionPane.ERROR_MESSAGE);
			}
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)")) && principalFundCrField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Credit Principal Account is reuired!","Enter Credit Principal Account",JOptionPane.ERROR_MESSAGE);
			}
			else if(Float.parseFloat(totalAmountField.getText()) > accdto.GetBalance() )
			{
				JOptionPane.showMessageDialog(frame, "Insufficient amount in account","Insufficient Amount",JOptionPane.ERROR_MESSAGE);
			}
			
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)") ||
					   actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal") || actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal & Profit")  && !(profitNomAccountField.getText().isEmpty())))
					   {
							if(!tdrService.isAccountExist(profitNomAccountField.getText().toString()))
							{
								JOptionPane.showMessageDialog(frame, "Profit Nomination Account is Invalid!","Enter Valid Profit Nomination Account ",JOptionPane.ERROR_MESSAGE);
							}
							
					   }
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)")) || actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal & Profit") && !principalFundCrField.getText().isEmpty())
			{
				if(!tdrService.isAccountExist(principalFundCrField.getText().toString()))
				{
					JOptionPane.showMessageDialog(frame, "Credit Principal Account is Invalid!","Enter Valid Credit Principal Account",JOptionPane.ERROR_MESSAGE);
				}
			}
			else{

					
			
			TermDepositApplicationDTO TDADTO = new TermDepositApplicationDTO();
			TDADTO.SetApplicationDate(dateField.getText());
			TDADTO.SetSelectedTenure((ComboItem) tenureComboBox.getSelectedItem());
			TDADTO.SetSelectedMOF((ComboItem) modeOfFundComboBox.getSelectedItem());
			TDADTO.SetSelectedMaturityAction((ComboItem) actionAtMaturityComboBox.getSelectedItem());
			TDADTO.SetAccountTitle(accountTitleField.getText());
			TDADTO.SetTDRAmount(Float.parseFloat(totalAmountField.getText()));
			TDADTO.SetPricipalFundCrAccount(principalFundCrField.getText());
			TDADTO.SetProfitNomAccount(profitNomAccountField.getText());
			TDADTO.SetAccountNo(accdto.GetAccountNo());
			TDADTO.SetApplicationNo(TDRAppDto.GetApplicationNo());
			if(filehandler.path != null)
			{
				File file = new File(filehandler.path);	
				try {
					TDADTO.SetFiledata(filehandler.readFileData(file), file.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				try {
					TDADTO.SetFiledata(TDRAppDto.GetFileData(), TDRAppDto.GetFileName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			int status= tdrService.updateTDRApplication(TDADTO);
			if(status==-1)
			{
				JOptionPane.showMessageDialog(frame, "Updated unsuccessful","Unsuccessful",JOptionPane.ERROR_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(frame, "Application Updated Successfully \n Application ID = "+TDRAppDto,"Successful",JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
			}
			}
		}
		});
	}

	public void NewTDA(final AccountDTO accountDTO)
	{
		saveButton = new JButton("Save");
		saveButton.setBounds(471, 501, 118, 23);
		panel.add(saveButton);
		panel.repaint();
		
		accountNoField.setText(accountDTO.GetAccountNo());
		accountTitleField.setText(accountDTO.GetAccountTitle());
		branchCodeField.setText(accountDTO.GetBranchCode());
		branchNameField.setText(accountDTO.GetBranchName());
		dateField.setText(accountDTO.GetBranchDate());
		currencyField.setText(accountDTO.GetCurrency());
		profitNomAccountField.setText(accountDTO.GetAccountNo());
		principalFundCrField.setText(accountDTO.GetAccountNo());
		saveButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent saveButtonClicked) {
			if(totalAmountField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Amount is required!","Enter Amount",JOptionPane.ERROR_MESSAGE);
			}
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)") ||
			   actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal"))
			   && (profitNomAccountField.getText().isEmpty()))
			{
					JOptionPane.showMessageDialog(frame, "Profit Nomination Account is reuired!","Enter Profit Nomination Account",JOptionPane.ERROR_MESSAGE);
			}
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)")) && principalFundCrField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Credit Principal Account is reuired!","Enter Credit Principal Account",JOptionPane.ERROR_MESSAGE);
			}
			else if(Float.parseFloat(totalAmountField.getText()) > accountDTO.GetBalance())
			{
				JOptionPane.showMessageDialog(frame, "Insufficient amount in account","Insufficient Amount",JOptionPane.ERROR_MESSAGE);
			}
			else if (filehandler.path == null || filehandler.path.isEmpty()) {
				
	            JOptionPane.showMessageDialog(frame, "Please select a file.");
	        }
			
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)") ||
					   actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal") || actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal & Profit")  && !(profitNomAccountField.getText().isEmpty())))
					   {
							if(!tdrService.isAccountExist(profitNomAccountField.getText().toString()))
							{
								JOptionPane.showMessageDialog(frame, "Profit Nomination Account is Invalid!","Enter Valid Profit Nomination Account ",JOptionPane.ERROR_MESSAGE);
							}
							
					   }
			else if((actionAtMaturityComboBox.getSelectedItem().equals("Credit Principal & Profit to Account (No Rollover)")) || actionAtMaturityComboBox.getSelectedItem().equals("Rollover Principal & Profit") && !principalFundCrField.getText().isEmpty())
			{
				if(!tdrService.isAccountExist(principalFundCrField.getText().toString()))
				{
					JOptionPane.showMessageDialog(frame, "Credit Principal Account is Invalid!","Enter Valid Credit Principal Account",JOptionPane.ERROR_MESSAGE);
				}
			}
			else{
				
			
			File file = new File(filehandler.path);
			
			TermDepositApplicationDTO TDADTO = new TermDepositApplicationDTO();
			TDADTO.SetApplicationDate(dateField.getText());
			TDADTO.SetSelectedTenure((ComboItem) tenureComboBox.getSelectedItem());
			TDADTO.SetSelectedMOF((ComboItem) modeOfFundComboBox.getSelectedItem());
			TDADTO.SetSelectedMaturityAction((ComboItem) actionAtMaturityComboBox.getSelectedItem());
			TDADTO.SetAccountTitle(accountTitleField.getText());
			TDADTO.SetTDRAmount(Float.parseFloat(totalAmountField.getText()));
			TDADTO.SetPricipalFundCrAccount(principalFundCrField.getText());
			TDADTO.SetProfitNomAccount(profitNomAccountField.getText());
			TDADTO.SetAccountNo(accountDTO.GetAccountNo());
			
			try {
				TDADTO.SetFiledata(filehandler.readFileData(file), file.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int applicationId= tdrService.insertTDRApplication(TDADTO);
			if(applicationId==-1)
			{
				JOptionPane.showMessageDialog(frame, "Insert unsuccessful","Unsuccessful",JOptionPane.ERROR_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(frame, "Application Created Successfully \n Application ID = "+applicationId,"Successful",JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
			}
			}
		}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AccountDTO accountDTO = new AccountDTO();
		TermDepositApplication tda = new TermDepositApplication(accountDTO);

	}
}
