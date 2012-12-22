package net.alienx.ihm_stab.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import net.alienx.ihm_stab.IHMPanel;
import net.alienx.ihm_stab.SerialListener;

public class UISerial extends IHMPanel implements SerialListener, ActionListener {
	private JTextArea serialRow;
	private JTextField sendTxt;
	private JButton sendButton;
	
	private JTextField convertTxt;
	private JComboBox convertType;
	private JButton convertButton;
	
	@Override
	protected void drawUI() {
		this.setLayout(new BorderLayout());
		
		serialRow = new JTextArea();
		serialRow.setFont(new Font("Helvetica", 0, 14));
		serialRow.setForeground(Color.red);
		serialRow.setLineWrap(true);
		serialRow.setWrapStyleWord(true);
		//serialRow.getSelectedText();
        JScrollPane areaScrollPane = new JScrollPane(serialRow);
        areaScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //areaScrollPane.setPreferredSize(new Dimension(250, 250));
        areaScrollPane.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("Consol"),
                                BorderFactory.createEmptyBorder(5,5,5,5)),
                areaScrollPane.getBorder()));
        add(areaScrollPane,BorderLayout.CENTER);
        
        JPanel pActions = new JPanel();
        addBorder(pActions,"Actions");
        pActions.setLayout(new GridBagLayout());
        GridBagConstraints gdb1 = new GridBagConstraints();
        gdb1.insets = new Insets(2,2,2,2);
        gdb1.gridx = 0;
        gdb1.gridy = 0;
        gdb1.fill = GridBagConstraints.BOTH;

        
        convertTxt = new JTextField();
        convertTxt.setEditable(false);
        convertTxt.setColumns(30);
        pActions.add(convertTxt,gdb1);
        
        String[] convertTypeNames = {"toDec","toInt","toBin"};
        convertType = new JComboBox(convertTypeNames);
        gdb1.gridx++;
        pActions.add(convertType,gdb1);
        
        convertButton = new JButton("Convert Selection");
        convertButton.addActionListener(this);
        gdb1.gridx++;
        pActions.add(convertButton,gdb1);
        
        gdb1.weightx = 1;
        gdb1.gridx++;
        pActions.add(new JPanel(),gdb1);
        
        sendTxt = new JTextField();
        gdb1.weightx = 1;
        gdb1.gridx = 0;
        gdb1.gridy = 1;
        gdb1.gridwidth = 6;
        pActions.add(sendTxt,gdb1);
        
        gdb1.weightx = 0.1;
        gdb1.gridx = 6;      
        sendButton = new JButton("Send");
        pActions.add(sendButton,gdb1);
        
        add(pActions,BorderLayout.SOUTH);

	}

	@Override
	protected void init() {
		getProtocol().getSerial().addSerialListener(this);

	}

	private void convertToDec(String txt){
		int res = 0;
		try{
			res = Integer.parseInt(txt.trim(), 16);
			convertTxt.setText(Integer.toString(res));
		}catch(Exception e) {
			convertTxt.setText("ERROR");
		}
		
	}
	
	private void convertToBin(String txt){
		int res = 0;
		String binary = "0";
		try{
			res = Integer.parseInt(txt.trim(), 16);
			binary = Integer.toBinaryString(res);
			convertTxt.setText(binary);
		}catch(Exception e) {
			convertTxt.setText("ERROR");
		}
		
	}
	
	private void convertToInt(String txt){
		String[] val = txt.split(" ",2);
		if(val.length < 2){
			convertTxt.setText("ERROR");
			return;
		}
		
		int res = 0;
		try{
			res = (Integer.parseInt(val[0].trim(), 16)<< 8);
			res += (Integer.parseInt(val[1].trim(), 16)& 0xFF);
			convertTxt.setText(Integer.toString(res));
		}catch(Exception e) {
			convertTxt.setText("ERROR");
		}
		
	}
	
	@Override
	public void SerialCmdRecv(byte[] datas) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SerialRowRecv(byte data) {
		serialRow.insert(Integer.toHexString(data & 0xFF).toUpperCase()+" ", serialRow.getText().length());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if(s == convertButton){
			if(convertType.getSelectedItem().equals("toDec")){
				convertToDec(serialRow.getSelectedText());
			}else if(convertType.getSelectedItem().equals("toInt")){
				convertToInt(serialRow.getSelectedText());
			}else if(convertType.getSelectedItem().equals("toBin")){
				convertToBin(serialRow.getSelectedText());
			}
		}
		
	}

}
