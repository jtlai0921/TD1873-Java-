package org.neuroph.core.transfer;

import org.neuroph.util.Properties;

public class ArcCos extends TransferFunction {

	public ArcCos(Properties pro){
		
	}
	
	@Override
	public double getOutput(double net) {
		return Math.acos(net);
	}

}
