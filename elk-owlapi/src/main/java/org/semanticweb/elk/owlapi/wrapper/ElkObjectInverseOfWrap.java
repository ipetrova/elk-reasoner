/*
 * #%L
 * ELK OWL API Binding
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.owlapi.wrapper;

import org.semanticweb.elk.owl.interfaces.ElkObjectInverseOf;
import org.semanticweb.elk.owl.interfaces.ElkObjectProperty;
import org.semanticweb.elk.owl.visitors.ElkObjectPropertyExpressionVisitor;
import org.semanticweb.elk.owlapi.converter.ElkObjectPropertyExpressionConverterVisitor;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;

/**
 * Implements the {@link ElkObjectInverseOf} interface by wrapping instances of
 * {@link OWLObjectInverseOf}
 * 
 * @author Yevgeny Kazakov
 * 
 */
public class ElkObjectInverseOfWrap<T extends OWLObjectInverseOf> extends
		ElkObjectPropertyExpressionWrap<T> implements ElkObjectInverseOf {

	public ElkObjectInverseOfWrap(T owlObjectInverseOf) {
		super(owlObjectInverseOf);
	}

	public ElkObjectProperty getObjectProperty() {
		ElkObjectPropertyExpressionConverterVisitor converter = ElkObjectPropertyExpressionConverterVisitor
				.getInstance();
		return this.owlObject.getInverse().accept(converter);
	}

	@Override
	public <O> O accept(ElkObjectPropertyExpressionVisitor<O> visitor) {
		return visitor.visit(this);
	}

}