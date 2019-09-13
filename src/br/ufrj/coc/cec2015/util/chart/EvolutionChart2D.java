package br.ufrj.coc.cec2015.util.chart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class EvolutionChart2D extends JFrame {
	private static final long serialVersionUID = 9036503967468200772L;
	private static final Color[] COLORS = new Color[] { Color.BLUE, Color.RED, Color.DARK_GRAY, Color.ORANGE, Color.CYAN };

	private static String DEFAULT_CHART_TITLE = "Gr�fico de Evolu��o dos Erros";

	XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
	private final ChartPanel chartPanel;

	public EvolutionChart2D(List<Double> serieX, List<Double> serieY, String description) {
		super("Evolution Chart2D");
		
		this.addSerie(serieX, serieY, description);
		
		chartPanel = createEvolutionPanel();
		super.add(chartPanel, BorderLayout.CENTER);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void setTitle(String title, String... subTitles) {
		chartPanel.getChart().setTitle(title + "\n\r" + subTitles[0]);
		chartPanel.getChart().clearSubtitles();
		/*for (String subTitle : subTitles)
			chartPanel.getChart().addSubtitle(new TextTitle(subTitle));*/
	}

	public void addSerie(List<Double> serieX, List<Double> serieY, String description) {
		XYSeries xySeries = createXYSeries(serieX, serieY, description);
		xySeriesCollection.addSeries(xySeries);
	}

	private XYSeries createXYSeries(List<Double> serieX, List<Double> serieY, String description) {
		XYSeries xySeries = new XYSeries(description);
		for (int idx = 0; idx < serieX.size(); idx++) {
			XYDataItem dataItem = new XYDataItem(serieX.get(idx), serieY.get(idx));
			xySeries.add(dataItem);
		}
		return xySeries;
	}

	public void toFile(String filePng) {
		try {
			OutputStream out = new FileOutputStream(filePng);
			ChartUtilities.writeChartAsPNG(out, chartPanel.getChart(), chartPanel.getWidth(), chartPanel.getHeight());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("serial")
	private ChartPanel createEvolutionPanel() {
		JFreeChart jfreechart = ChartFactory.createXYLineChart(DEFAULT_CHART_TITLE, "Percentual de Avalia��o (% MaxFES = Dim.10000)", "M�dia dos Erros", this.xySeriesCollection, PlotOrientation.VERTICAL, true, true, false);
		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		
		LogAxis yAxis = new LogAxis("M�dia dos Erros");
		NumberFormat numberFormat = new DecimalFormat("0.##E00");
		yAxis.setNumberFormatOverride(numberFormat);
		xyPlot.setRangeAxis(yAxis);
		
		XYItemRenderer renderer = xyPlot.getRenderer();
		for (int idx = 0; idx < this.xySeriesCollection.getSeriesCount(); idx++) {
			renderer.setSeriesPaint(idx, COLORS[idx]);
		}

		xyPlot.setBackgroundPaint(Color.WHITE);
		xyPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
		xyPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);

		return new ChartPanel(jfreechart) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(800, 650);
			}
		};
	}
}