package org.semanticweb.elk.alc.saturation.conclusions.implementation;

/*
 * #%L
 * ALC Reasoner
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

import org.semanticweb.elk.alc.indexing.hierarchy.IndexedClassExpression;
import org.semanticweb.elk.alc.indexing.hierarchy.IndexedObjectUnionOf;
import org.semanticweb.elk.alc.saturation.conclusions.interfaces.Disjunction;
import org.semanticweb.elk.alc.saturation.conclusions.visitors.LocalDeterministicConclusionVisitor;

public class DisjunctionImpl extends AbstractLocalDeterministicConclusion
		implements Disjunction {

	private final IndexedObjectUnionOf disjunction_;

	public DisjunctionImpl(IndexedObjectUnionOf disjunction) {
		this.disjunction_ = disjunction;
	}

	@Override
	public IndexedClassExpression getWatchedDisjunct() {
		return disjunction_.getSecondDisjunct();
	}

	@Override
	public IndexedClassExpression getPropagatedDisjunct() {
		return disjunction_.getFirstDisjunct();
	}

	@Override
	public <I, O> O accept(LocalDeterministicConclusionVisitor<I, O> visitor,
			I input) {
		return visitor.visit(this, input);
	}

	@Override
	public String toString() {
		return Disjunction.NAME + "(" + getWatchedDisjunct() + " "
				+ getPropagatedDisjunct() + ")";
	}

}