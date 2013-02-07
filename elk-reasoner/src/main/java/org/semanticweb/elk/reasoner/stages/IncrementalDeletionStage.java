/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 - 2012 Department of Computer Science, University of Oxford
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.semanticweb.elk.reasoner.stages;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.semanticweb.elk.reasoner.incremental.IncrementalStages;
import org.semanticweb.elk.reasoner.saturation.ClassExpressionNoInputSaturation;
import org.semanticweb.elk.reasoner.saturation.ContextModificationListener;
import org.semanticweb.elk.reasoner.saturation.rules.RuleDeapplicationFactory;

/**
 * Reverts inferences
 * 
 * @author Pavel Klinov
 * 
 */
class IncrementalDeletionStage extends AbstractReasonerStage {

	// logger for this class
	private static final Logger LOGGER_ = Logger
			.getLogger(IncrementalDeletionStage.class);

	private ClassExpressionNoInputSaturation desaturation_ = null;

	public IncrementalDeletionStage(ReasonerStageManager manager) {
		super(manager);
	}

	@Override
	public String getName() {
		return IncrementalStages.DELETION.toString();
	}

	@Override
	public boolean done() {
		return reasoner.incrementalState
				.getStageStatus(IncrementalStages.DELETION);
	}

	@Override
	public List<ReasonerStage> getDependencies() {
		return Arrays.asList(manager.incrementalDeletionInitializationStage);
	}

	@Override
	boolean preExecute() {
		if (!super.preExecute())
			return false;
		desaturation_ = new ClassExpressionNoInputSaturation(
				reasoner.getProcessExecutor(), workerNo,
				reasoner.getProgressMonitor(), new RuleDeapplicationFactory(
						reasoner.saturationState, true),
				ContextModificationListener.DUMMY);
		return true;
	}

	@Override
	public void executeStage() throws ElkInterruptedException {
		for (;;) {
			desaturation_.process();
			if (!spuriousInterrupt())
				break;
		}
	}

	@Override
	boolean postExecute() {
		if (!super.postExecute())
			return false;
		reasoner.incrementalState.setStageStatus(IncrementalStages.DELETION,
				true);
		reasoner.ruleAndConclusionStats.add(desaturation_
				.getRuleAndConclusionStatistics());
		if (LOGGER_.isTraceEnabled()) {
			LOGGER_.trace("Number of modified contexts "
					+ reasoner.saturationState.getNotSaturatedContexts().size());
		}
		desaturation_ = null;
		return true;
	}

	@Override
	public void printInfo() {
		if (desaturation_ != null)
			desaturation_.printStatistics();
	}
}