/**
 * 
 */
package org.semanticweb.elk.proofs.transformations;
/*
 * #%L
 * OWL API Proofs Model
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

import java.util.Collection;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.proofs.expressions.AxiomExpression;
import org.semanticweb.elk.proofs.expressions.Expression;
import org.semanticweb.elk.proofs.expressions.ExpressionVisitor;
import org.semanticweb.elk.proofs.expressions.LemmaExpression;
import org.semanticweb.elk.proofs.inferences.Inference;
import org.semanticweb.elk.proofs.inferences.InferenceRule;
import org.semanticweb.elk.proofs.inferences.InferenceVisitor;
import org.semanticweb.elk.util.collections.Operations;

/**
 * TODO
 * 
 * @author Pavel Klinov
 *
 * pavel.klinov@uni-ulm.de
 */
public class TransformedInference<T extends InferenceTransformation> implements Inference {

	protected final Inference inference;
	
	protected final T transformation;
	
	public TransformedInference(Inference inf, T f) {
		inference = inf;
		transformation = f;
	}
	
	@Override
	public Expression getConclusion() {
		return transform(inference.getConclusion(), transformation);
	}

	protected TransformedExpression<?, T> propagateTransformation(Expression expr) {
		return transform(expr, transformation);
	}
	
	private TransformedExpression<?, T> transform(Expression expr, final T transfrm) {
		return expr.accept(new ExpressionVisitor<Void, TransformedExpression<?, T>>() {

			@Override
			public TransformedExpression<?, T> visit(AxiomExpression<? extends ElkAxiom> axiom, Void input) {
				return new TransformedAxiomExpression<T, ElkAxiom>((AxiomExpression<ElkAxiom>) axiom, transfrm);
			}

			@Override
			public TransformedExpression<?, T> visit(LemmaExpression<?> lemma, Void input) {
				return new TransformedLemmaExpression<T>(lemma, transfrm);
			}
			
		}, null);
	}

	@Override
	public Collection<? extends TransformedExpression<?, T>> getPremises() {
		return Operations.map(inference.getPremises(), new Operations.Transformation<Expression, TransformedExpression<?, T>>() {

			@Override
			public TransformedExpression<?, T> transform(Expression premise) {
				return propagateTransformation(premise);
			}
			
		});
	}

	@Override
	public String toString() {
		return inference.toString();
	}

	@Override
	public InferenceRule getRule() {
		return inference.getRule();
	}

	@Override
	public <I, O> O accept(InferenceVisitor<I, O> visitor, I input) {
		return inference.accept(visitor, input);
	}
	
}
