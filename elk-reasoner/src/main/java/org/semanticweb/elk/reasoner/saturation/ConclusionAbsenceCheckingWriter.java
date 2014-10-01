package org.semanticweb.elk.reasoner.saturation;

/*
 * #%L
 * ELK Reasoner
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedClassExpression;
import org.semanticweb.elk.reasoner.saturation.conclusions.interfaces.Conclusion;
import org.semanticweb.elk.reasoner.saturation.context.Context;

/**
 * A {@link SaturationStateWriter} that produces conclusions only if they are
 * absent in the respective context.
 * 
 * @author "Yevgeny Kazakov"
 */
public class ConclusionAbsenceCheckingWriter<C extends Context> extends
		SaturationStateWriterWrap<C> {

	private final SaturationState<? extends C> state_;

	public ConclusionAbsenceCheckingWriter(
			SaturationStateWriter<? extends C> writer,
			SaturationState<? extends C> state) {
		super(writer);
		this.state_ = state;
	}

	@Override
	public void produce(IndexedClassExpression root, Conclusion conclusion) {
		Context context = state_.getContext(root);

		if (context != null && context.containsConclusion(conclusion)) {
			// context already contains the conclusion
			return;
		}
		// else
		super.produce(root, conclusion);
	}

}