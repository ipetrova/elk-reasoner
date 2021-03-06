/**
 * 
 */
package org.semanticweb.elk.proofs.inferences;
/*
 * #%L
 * ELK Proofs Package
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

import java.util.Collection;
import java.util.Iterator;

import org.semanticweb.elk.proofs.expressions.Expression;
import org.semanticweb.elk.util.hashing.HashGenerator;

/**
 * TODO
 * 
 * @author	Pavel Klinov
 * 			pavel.klinov@uni-ulm.de
 *
 */
public class InferenceEntry<I extends Inference> {

	private final I key_;
	
	private final int hash_;
	
	public InferenceEntry(I inf) {
		this.key_ = inf;
		this.hash_ = computeHashCode(inf);
	}

	public int computeHashCode(I key) {
		return HashGenerator.combineListHash(key.getRule().hashCode(), HashGenerator.combinedHashCode(key.getPremises()), key.getConclusion().hashCode());
	}
	
	@Override
	public int hashCode() {
		return this.hash_;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (object == null || !(object instanceof InferenceEntry)) {
			return false;
		}
		
		Inference otherInf = ((InferenceEntry<?>) object).key_;
		
		return key_.getRule().equals(otherInf.getRule()) && key_.getConclusion().equals(otherInf.getConclusion()) && equal(key_.getPremises(), otherInf.getPremises());
	}

	private boolean equal(Collection<? extends Expression> premises, Collection<? extends Expression> other) {
		Iterator<? extends Expression> premiseIter = premises.iterator();
		Iterator<? extends Expression> otherIter = other.iterator();
		
		while (premiseIter.hasNext()) {
			if (!otherIter.hasNext()) {
				return false;
			}
			
			Expression premise = premiseIter.next();
			Expression otherPremise = otherIter.next();
			
			if (!premise.equals(otherPremise)) {
				return false;
			}
		}
		
		return !otherIter.hasNext();
	}

}
