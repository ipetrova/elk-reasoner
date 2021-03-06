package org.semanticweb.elk.owl.inferences;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;

/*
 * #%L
 * ELK Proofs Package
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
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

import org.semanticweb.elk.owl.interfaces.ElkClassExpression;
import org.semanticweb.elk.owl.interfaces.ElkObject;
import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkSubClassOfAxiom;

/**
 * Represents the inference:
 * 
 * <pre>
 *   (1)       (2)
 *  C ⊑ ∃R.D  D ⊑ E
 * ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯
 *     C ⊑ ∃R.E
 * </pre>
 * 
 * @author Yevgeny Kazakov
 *
 */
public class ElkClassInclusionExistentialFillerExpansion
		extends AbstractElkInference {
	
	private final static String NAME_ = "Existential Filler Expansion";

	private final ElkClassExpression subClass_, subFiller_, superFiller_;

	private final ElkObjectPropertyExpression property_;

	ElkClassInclusionExistentialFillerExpansion(ElkClassExpression subClass,
			ElkObjectPropertyExpression property, ElkClassExpression subFiller,
			ElkClassExpression superFiller) {
		this.subClass_ = subClass;
		this.property_ = property;
		this.subFiller_ = subFiller;
		this.superFiller_ = superFiller;
	}

	public ElkClassExpression getSubClass() {
		return subClass_;
	}

	public ElkObjectPropertyExpression getProperty() {
		return property_;
	}

	public ElkClassExpression getSubFiller() {
		return subFiller_;
	}

	public ElkClassExpression getSuperFiller() {
		return superFiller_;
	}
	
	@Override
	public String getName() {
		return NAME_;
	}

	@Override
	public int getPremiseCount() {
		return 2;
	}

	@Override
	public ElkAxiom getPremise(int index, ElkObject.Factory factory) {
		switch (index) {
		case 0:
			return getFirstPremise(factory);
		case 1:
			return getSecondPremise(factory);
		default:
			return failGetPremise(index);
		}
	}

	public ElkSubClassOfAxiom getFirstPremise(ElkObject.Factory factory) {
		return factory.getSubClassOfAxiom(subClass_,
				factory.getObjectSomeValuesFrom(property_, subFiller_));
	}

	public ElkSubClassOfAxiom getSecondPremise(ElkObject.Factory factory) {
		return factory.getSubClassOfAxiom(subFiller_, superFiller_);
	}

	@Override
	public ElkSubClassOfAxiom getConclusion(ElkObject.Factory factory) {
		return factory.getSubClassOfAxiom(subClass_,
				factory.getObjectSomeValuesFrom(property_, superFiller_));

	}

	@Override
	public <O> O accept(ElkInference.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	public interface Factory {

		ElkClassInclusionExistentialFillerExpansion getElkClassInclusionExistentialFillerUnfolding(
				ElkClassExpression subClass,
				ElkObjectPropertyExpression property,
				ElkClassExpression subFiller, ElkClassExpression superFiller);

	}

	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	interface Visitor<O> {

		O visit(ElkClassInclusionExistentialFillerExpansion inference);

	}

}
