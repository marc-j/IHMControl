package net.alienx.ihm_stab.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import net.alienx.ihm_stab.IHMPanel;
import net.alienx.ihm_stab.protocol.ProtocolListener;
import net.alienx.ihm_stab.protocol.ProtocolMessage;
import net.alienx.ihm_stab.protocol.msg.ProtocolMsgCapteurs;

public class UITelemetry extends IHMPanel implements ProtocolListener, ItemListener {
	
	private TimeSeries plotSeries[];
	private TimeSeriesCollection timeseriescollection;
	public static final short ACC_X=0,
							  ACC_Y=1,
							  ACC_Z=2,
							  GYRO_X=3,
							  GYRO_Y=4,
							  GYRO_Z=5,
							  ROLL=6,
							  PITCH=7,
							  YAW=8;
	private static final String[] _ValuesNames = {"Acc X","Acc Y","Acc Z","Gyro X","Gyro Y","Gyro Z","Roll","Pitch","Yaw"};
	private JLabel ValuesLabel[];
	private JCheckBox ValuesEnabled[];
	
	@Override
	protected void drawUI() {
		setLayout(new BorderLayout());
		
		JPanel pValues = new JPanel();
		pValues.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),BorderFactory.createEmptyBorder(5,5,5,10)));
		pValues.setLayout(new GridBagLayout());
		
		GridBagConstraints gdb1 = new GridBagConstraints();
		gdb1.insets = new Insets(2,2,2,2);
		gdb1.gridx = 0;
		gdb1.gridy = 0;
		gdb1.fill = GridBagConstraints.BOTH;
		
		for(int i=0;i<_ValuesNames.length;i++){
			gdb1.gridx=0;
			gdb1.weightx = 0;	
			ValuesEnabled[i] = new JCheckBox(_ValuesNames[i]);
			ValuesEnabled[i].addItemListener(this);
			pValues.add(ValuesEnabled[i],gdb1);
			
			gdb1.gridx=2;
			gdb1.weightx = 0.5;
			pValues.add(new JPanel(),gdb1);
			
			ValuesLabel[i] = new JLabel("0.000000",JLabel.RIGHT);
			gdb1.gridx=3;
			gdb1.weightx = 1;
			pValues.add(ValuesLabel[i],gdb1);
			gdb1.gridy++;
		}
		
		gdb1.gridy++;
		gdb1.weighty = 1;
		pValues.add(new JPanel(),gdb1);
		
		add(pValues,BorderLayout.WEST);
		plotSeries[0] = new TimeSeries("Acc_X");
		//plotSeries[1] = new TimeSeries("Acc_Y");
		//plotSeries[2] = new TimeSeries("Acc_Z");
		
		timeseriescollection = new TimeSeriesCollection();
		//timeseriescollection.addSeries(plotSeries[1]);
		//timeseriescollection.addSeries(plotSeries[2]);
		
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("IMU", "Time", "Value", timeseriescollection, true, true, false);
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        ValueAxis valueaxis = xyplot.getDomainAxis();
        /*xyplot.setDataset(1, timeseriescollection1);
        NumberAxis numberaxis = new NumberAxis("Range Axis 2");
        numberaxis.setAutoRangeIncludesZero(false);
        xyplot.setRenderer(1, new DefaultXYItemRenderer());
        xyplot.setRangeAxis(1, numberaxis);
        xyplot.mapDatasetToRangeAxis(1, 1);*/
        valueaxis.setAutoRange(true);
        valueaxis.setFixedAutoRange(60000D);
        valueaxis = xyplot.getRangeAxis();
        //valueaxis.setRange(0.0D, 180.D);

		ChartPanel chartpanel = new ChartPanel(jfreechart);
		add(chartpanel,BorderLayout.CENTER);
		
	}

	@Override
	protected void init() {
		plotSeries = new TimeSeries[_ValuesNames.length];
		ValuesLabel = new JLabel[_ValuesNames.length];
		ValuesEnabled = new JCheckBox[_ValuesNames.length];
		
		for(int i=0;i<plotSeries.length;i++)
			plotSeries[i] = new TimeSeries(_ValuesNames[i]);
		
		getProtocol().addProtocolListener(this);
	}
	
	@Override
	public void protocolDataReceive(int cmd, ProtocolMessage msg) {
		
		if(msg instanceof ProtocolMsgCapteurs){
			if(plotSeries[ACC_X].getItemCount() > 1000){
				for(int i=0;i<9;i++)
					plotSeries[i].delete(0, 500);
			}
			plotSeries[ACC_X].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getAccX());
			plotSeries[ACC_Y].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getAccY());
			plotSeries[ACC_Z].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getAccZ());
			plotSeries[GYRO_X].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getGyroX());
			plotSeries[GYRO_Y].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getGyroY());
			plotSeries[GYRO_Z].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getGyroZ());
			plotSeries[ROLL].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getRoll());
			plotSeries[PITCH].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getPitch());
			plotSeries[YAW].add(new Millisecond(),((ProtocolMsgCapteurs) msg).getYaw());
			
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ev) {
		Object s = ev.getItemSelectable();
		for(int i=0;i<ValuesEnabled.length;i++){
			if(s == ValuesEnabled[i]){
				if(ev.getStateChange() == ItemEvent.DESELECTED){
					removePlot(i);
				}else{
					showPlot(i);
				}
			}
		}
	}
	
	public void showPlot(int i){
		if(plotSeries[i] == null)
			plotSeries[i] = new TimeSeries(_ValuesNames[i]);
		timeseriescollection.addSeries(plotSeries[i]);
	}
	
	public void removePlot(int i){
		if(plotSeries[i] == null)
			return;
		timeseriescollection.removeSeries(plotSeries[i]);
	}

}
