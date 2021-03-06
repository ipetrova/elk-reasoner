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

import org.semanticweb.elk.exceptions.ElkException;
import org.semanticweb.elk.loading.AxiomLoader;
import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObject;
import org.semanticweb.elk.owl.visitors.ElkAxiomProcessor;
import org.semanticweb.elk.reasoner.incremental.NonIncrementalChangeListener;
import org.semanticweb.elk.reasoner.indexing.classes.ChangeIndexingProcessor;
import org.semanticweb.elk.reasoner.indexing.classes.NonIncrementalElkAxiomVisitor;
import org.semanticweb.elk.reasoner.indexing.conversion.ElkAxiomConverter;
import org.semanticweb.elk.reasoner.indexing.conversion.ElkAxiomConverterImpl;
import org.semanticweb.elk.reasoner.indexing.model.ModifiableOntologyIndex;
import org.semanticweb.elk.reasoner.tracing.TraceState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link ReasonerStage} during which the input ontology is loaded into the
 * reasoner.
 * 
 * @author "Yevgeny Kazakov"
 * 
 */
public class AxiomLoadingStage extends AbstractReasonerStage {

	// logger for this class
	private static final Logger LOGGER_ = LoggerFactory
			.getLogger(AxiomLoadingStage.class);

	/**
	 * the {@link AxiomLoader} using which the axioms are loaded
	 */
	private volatile AxiomLoader loader_;
	
	/**
	 * {@code true} if this stage was not yet successfully completed before
	 */
	private boolean firstLoad_ = true;

	/**
	 * the {@link ElkAxiomProcessor}s using which the axioms are inserted and
	 * deleted
	 */
	private ElkAxiomProcessor axiomInsertionProcessor_,
			axiomDeletionProcessor_;

	public AxiomLoadingStage(AbstractReasonerState reasoner,
			AbstractReasonerStage... preStages) {
		super(reasoner, preStages);
	}

	@Override
	public String getName() {
		return "Loading of Axioms";
	}

	@Override
	public boolean preExecute() {
		if (!super.preExecute())
			return false;
		if (!firstLoad_) {
			reasoner.trySetIncrementalMode();			
		}
		loader_ = reasoner.getAxiomLoader();
		if (loader_ == null || loader_.isLoadingFinished()) {
			return true;
		}

		ModifiableOntologyIndex ontologyIndex = reasoner
				.getModifiableOntologyIndex();
		ElkObject.Factory elkFactory = reasoner.getElkFactory();

		ElkAxiomConverter axiomInserter = new ElkAxiomConverterImpl(elkFactory,
				ontologyIndex, 1);
		ElkAxiomConverter axiomDeleter = new ElkAxiomConverterImpl(elkFactory,
				ontologyIndex, -1);

		/*
		 * wrapping both the inserter and the deleter to receive notifications
		 * if some axiom change can't be incorporated incrementally
		 */
		/**
		 * The listener used to count axioms and detect if the axiom cannot be
		 * loaded incrementally
		 */
		NonIncrementalChangeListener<ElkAxiom> listener = new NonIncrementalChangeListener<ElkAxiom>() {

			boolean resetDone = false;

			@Override
			public void notify(ElkAxiom axiom) {
				if (resetDone)
					return;
				LOGGER_.debug("{}: axiom not supported in incremental mode", axiom);
				reasoner.resetPropertySaturation();
				reasoner.setNonIncrementalMode();
				resetDone = true;
			}
		};

		axiomInserter = new NonIncrementalElkAxiomVisitor(axiomInserter,
				listener);
		axiomDeleter = new NonIncrementalElkAxiomVisitor(axiomDeleter, listener);

		this.axiomInsertionProcessor_ = new ChangeIndexingProcessor(
				axiomInserter, ChangeIndexingProcessor.ADDITION);
		this.axiomDeletionProcessor_ = new ChangeIndexingProcessor(
				axiomDeleter, ChangeIndexingProcessor.REMOVAL);
		return true;
	}

	@Override
	public void executeStage() throws ElkException {
		loader_.load(axiomInsertionProcessor_, axiomDeletionProcessor_);
	}

	@Override
	public boolean postExecute() {
		if (!super.postExecute())
			return false;	
		this.firstLoad_ = false;
		this.loader_ = null;
		this.axiomInsertionProcessor_ = null;
		this.axiomDeletionProcessor_ = null;
		return true;
	}

	@Override
	public void printInfo() {
		// TODO
	}
	
	@Override
	boolean invalidate() {
		boolean invalidated = super.invalidate();
		if (invalidated) {
			TraceState traceState = reasoner.getTraceState();
			traceState.clearIndexedAxiomInferences();
			traceState.clearClassInferences();
		}
		return invalidated;
	}

	@Override
	public void setInterrupt(boolean flag) {
		super.setInterrupt(flag);
		setInterrupt(loader_, flag);
	}

}
