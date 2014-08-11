/**
 * 
 */
package org.semanticweb.elk.reasoner.saturation.tracing.inferences;
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
import org.semanticweb.elk.reasoner.indexing.hierarchy.IndexedDisjointnessAxiom;
import org.semanticweb.elk.reasoner.saturation.conclusions.interfaces.Contradiction;
import org.semanticweb.elk.reasoner.saturation.conclusions.visitors.ConclusionVisitor;
import org.semanticweb.elk.reasoner.saturation.tracing.inferences.visitors.ClassInferenceVisitor;

/**
 * Represents a {@link Contradiction} as the result of a class occurrsing
 * multiple times in one {@link IndexedDisjointnessAxiom}.
 * 
 * TODO store the inconsistent disjointness axiom explicitly as the side
 * condition or get it from the rule registered for the premise?
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 */
public class ContradictionFromInconsistentDisjointnessAxiom extends ContradictionFromSubsumer<IndexedClassExpression> implements Contradiction, ClassInference {

	public ContradictionFromInconsistentDisjointnessAxiom(IndexedClassExpression p) {
		super(p);
	}
	
	@Override
	public <I, O> O accept(ConclusionVisitor<I, O> visitor, I input) {
		return visitor.visit(this, input);
	}

	@Override
	public String toString() {
		return "Contradiction from " + premise;
	}

	@Override
	public <I, O> O acceptTraced(ClassInferenceVisitor<I, O> visitor, I parameter) {
		return visitor.visit(this, parameter);
	}

}
