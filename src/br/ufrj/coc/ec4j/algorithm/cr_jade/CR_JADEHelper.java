package br.ufrj.coc.ec4j.algorithm.cr_jade;

import br.ufrj.coc.ec4j.algorithm.Population;
import br.ufrj.coc.ec4j.algorithm.de.DEProperties;
import br.ufrj.coc.ec4j.algorithm.jade.JADEHelper;
import br.ufrj.coc.ec4j.util.Helper;
import br.ufrj.coc.ec4j.util.Properties;

public class CR_JADEHelper extends JADEHelper {
	private int evalPerc;

	protected void initialize() {
		super.initialize();
		this.evalPerc = 0;
	}

	public void initializeGeneration(Population population) {
		super.initializeGeneration(population);
		
		double funcValDifference = Helper.getFunctionValueDifference(population);
		double maxDistance = Helper.getMaxDistance(population);

		if (funcValDifference < Properties.MIN_ERROR_VALUE && maxDistance < 1) {
			this.controlledRestart(population);
		}
		else if (DEProperties.CR_MAXFES_INTERVAL > 0) {
			int currentEvalPerc = (int) (Properties.ARGUMENTS.get().getEvolutionPercentage() * 100);
			int interv = (int) (DEProperties.CR_MAXFES_INTERVAL * 100);
			if (currentEvalPerc > this.evalPerc && (currentEvalPerc % interv) == 0) {
				this.evalPerc = currentEvalPerc;
				double funcValueDiffInterval = Math.abs(population.getFuncValDiff() - funcValDifference);
				double maxDistInterval = Math.abs(population.getMaxDistance() - maxDistance);
				System.err.println(String.format("Test stagnation %.2f: funcValueDiffInterval = %e / maxDistInterval = %e", Properties.ARGUMENTS.get().getEvolutionPercentage(), funcValueDiffInterval, maxDistInterval));
				boolean stagnation = (funcValueDiffInterval == 0.0 || maxDistInterval == 0.0);
				if (stagnation) {
					this.controlledRestart(population);
				}
				population.setFuncValDiff(funcValDifference);
				population.setMaxDistance(maxDistance);
			}
		}
	}
	
	@Override
	protected void controlledRestart(Population population) {
		System.err.println(String.format("RESTART (%d)!", population.getCountRestart()));
		population.setFuncValDiff(0);
		population.setMaxDistance(0);
		super.controlledRestart(population);
	}
}