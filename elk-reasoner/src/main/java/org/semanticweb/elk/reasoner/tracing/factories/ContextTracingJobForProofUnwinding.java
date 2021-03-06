package org.semanticweb.elk.reasoner.tracing.factories;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2015 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.indexing.model.IndexedContextRoot;
import org.semanticweb.elk.reasoner.saturation.conclusions.model.ClassConclusion;

class ContextTracingJobForProofUnwinding<I extends ClassConclusion, J extends ProofUnwindingJob<I>>
		extends
			ContextTracingJob<IndexedContextRoot> {

	final ClassConclusion tracedConclusion;

	final ProofUnwindingState<I, J> unwindingState;

	ContextTracingJobForProofUnwinding(ClassConclusion tracedConclusion,
			ProofUnwindingState<I, J> unwindingState) {
		super(tracedConclusion.getTraceRoot());
		this.tracedConclusion = tracedConclusion;
		this.unwindingState = unwindingState;
	}

}
