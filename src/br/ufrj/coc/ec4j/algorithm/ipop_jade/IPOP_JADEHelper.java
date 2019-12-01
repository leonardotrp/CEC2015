package br.ufrj.coc.ec4j.algorithm.ipop_jade;

import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import br.ufrj.coc.ec4j.algorithm.Population;
import br.ufrj.coc.ec4j.algorithm.de.DEProperties;
import br.ufrj.coc.ec4j.algorithm.jade.JADEHelper;
import br.ufrj.coc.ec4j.util.Helper;
import br.ufrj.coc.ec4j.util.Properties;

public class IPOP_JADEHelper extends JADEHelper {
	private Queue<Double> funcValueDiffs;
	private Queue<Double> maxDistances;
	private int evalPerc;
	
	public IPOP_JADEHelper() {
		super();
		this.initialize();
	}

	protected void initialize() {
		super.initialize();
		int populationSize = Properties.ARGUMENTS.get().getPopulationSize();
		this.funcValueDiffs = new CircularFifoQueue<Double>(populationSize);
		this.maxDistances = new CircularFifoQueue<Double>(populationSize);
		this.evalPerc = 0;
	}

	public void initializeGeneration(Population population) {
		super.initializeGeneration(population);
		this.funcValueDiffs.add(Helper.getFunctionValueDifference(population));
		this.maxDistances.add(Helper.getMaxDistance(population));
		int currentEvalPerc = (int) (Properties.ARGUMENTS.get().getEvolutionPercentage() * 100);
		int interv = (int) (DEProperties.IPOP_MAXFES_INTERVAL * 100);
		if (currentEvalPerc > this.evalPerc && (currentEvalPerc % interv) == 0) {
			this.evalPerc = currentEvalPerc;

			// Diff_Interv: Módulo de { MedDiff(T1) - MedDiff(T2) }
			double funcValueDiffLast = population.getFuncValDiff();
			double funcValueDiffCurr = Helper.calculateMean(this.funcValueDiffs);
			boolean funcValDiffBetter = funcValueDiffCurr <= funcValueDiffLast;
			double funcValueDiffInterval = Math.abs(funcValueDiffLast - funcValueDiffCurr);

			// Diff_MaxDist: Módulo de { MedMaxDist(T1) - MedMaxDist(T2) }
			double maxDistLast = population.getMaxDistance();
			double maxDistCurr = Helper.calculateMean(this.maxDistances);
			double maxDistInterval = Math.abs(maxDistLast - maxDistCurr);
			System.err.println(String.format("TEST STAGNATION %.2f: funcValueDiffInterval = %e / maxDistInterval = %e", Properties.ARGUMENTS.get().getEvolutionPercentage(), funcValueDiffInterval, maxDistInterval));

			boolean criteria1 = (funcValueDiffInterval == 0.0 || maxDistInterval == 0.0) && funcValueDiffCurr != Properties.MIN_ERROR_VALUE;
			boolean criteria2 = !funcValDiffBetter && funcValueDiffInterval < DEProperties.IPOP_MAXDIFF_FUNCVAL;
			boolean criteria3 = maxDistInterval < DEProperties.IPOP_MAXDIST;
			boolean stagnation = (criteria1 || criteria2) && criteria3;

			if (stagnation) {
				super.ipop(population);
				this.funcValueDiffs.clear();
				this.maxDistances.clear();
				population.setFuncValDiff(0);
				population.setMaxDistance(0);
				population.incCountRestart();
				System.err.println(String.format("RESTART POPULATION (%d)! ( CRIT_1(%s) || CRIT_2(%s) ) && CRIT_3(%s)", population.getCountRestart(), criteria1, criteria2, criteria3));
				this.initializeGeneration(population);
			}
			population.setFuncValDiff(funcValueDiffCurr);
			population.setMaxDistance(maxDistCurr);
		}
	}
}