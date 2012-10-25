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

import org.semanticweb.elk.reasoner.incremental.ChangesInitializationListener;
import org.semanticweb.elk.reasoner.incremental.IncrementalChangesInitialization;
import org.semanticweb.elk.reasoner.incremental.IncrementalStages;
import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedClassExpression;

/**
 * Reverts inferences
 * 
 * @author Pavel Klinov
 * 
 */
class IncrementalChangesInitializationStage extends AbstractReasonerStage {

	// logger for this class
	//private static final Logger LOGGER_ = Logger.getLogger(IncrementalDeSaturationStage.class);

	private IncrementalChangesInitialization initialization_ = null;
	private final ChangesInitializationListener<IndexedClassExpression> listener_ = new ChangesInitializationListener<IndexedClassExpression>();
	private final boolean deletions_;

	public IncrementalChangesInitializationStage(AbstractReasonerState reasoner, boolean deletions) {
		super(reasoner);
		deletions_ = deletions;
	}

	@Override
	public String getName() {
		return IncrementalStages.CHANGES_INIT.toString() + " (" + ( deletions_ ? "deletions" : "additions" ) + ")";
	}

	@Override
	public boolean done() {
		return reasoner.incrementalState.getStageStatus(IncrementalStages.CHANGES_INIT);
	}

	@Override
	public List<ReasonerStage> getDependencies() {
		return Arrays.asList((ReasonerStage) new ChangesLoadingStage(reasoner));
	}

	@Override
	public void execute() throws ElkInterruptedException {
		if (initialization_ == null) {
			initComputation();
		}
		
		progressMonitor.start(getName());

		try {
			for (;;) {
				initialization_.process();
				if (!interrupted())
					break;
			}
		} finally {
			progressMonitor.finish();
		}

		reasoner.incrementalState.setStageStatus(IncrementalStages.CHANGES_INIT, true);
	}
	
	

	@Override
	void initComputation() {
		super.initComputation();
		
		initialization_ = new IncrementalChangesInitialization(
				reasoner.ontologyIndex.getIndexedClassExpressions(),
				deletions_ ? reasoner.incrementalState.diffIndex.getIndexDeletions() : reasoner.incrementalState.diffIndex.getIndexAdditions(),
				reasoner.saturationState,
				reasoner.getProcessExecutor(),
				workerNo,
				reasoner.getProgressMonitor(),
				listener_);
	}

	@Override
	public void printInfo() {
		// TODO Auto-generated method stub
		
	}
}