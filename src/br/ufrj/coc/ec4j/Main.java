package br.ufrj.coc.ec4j;

import br.ufrj.coc.ec4j.algorithm.Algorithm;
import br.ufrj.coc.ec4j.util.Properties;

public class Main {

	public static Algorithm newInstanceAlgorithm(String className) throws Exception {
		return (Algorithm) Class.forName(className).newInstance();
	}

	public static void main(String[] args) throws Exception {
		for (String name : Properties.ALGORITHMS) { // loop algorithms
			for (int individualSize : Properties.INDIVIDUAL_SIZES) { // loop dimensions
				String className = Algorithm.class.getPackage().getName() + '.' + name.toLowerCase() + '.' + name;
				Algorithm algorithm = newInstanceAlgorithm(className);
				for (String variant : algorithm.getVariants()) {  // loop variants
					algorithm.main(name, individualSize, variant);
				}
			}
		}
	}
}