/**
 * 
 */
package org.semanticweb.elk.proofs.inferences;

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

import java.util.Collection;

import org.semanticweb.elk.proofs.expressions.Expression;

/**
 * Represents an elementary component of proofs reported to the user. Each
 * inference has a set of premises and a conclusion, which are
 * {@link Expression}s.
 * 
 * @author Pavel Klinov
 * 
 *         pavel.klinov@uni-ulm.de
 */
public interface Inference {

	public Collection<? extends Expression> getPremises();

	public Expression getConclusion();
	
	public InferenceRule getRule();

	public <I, O> O accept(InferenceVisitor<I, O> visitor, I input);
}
