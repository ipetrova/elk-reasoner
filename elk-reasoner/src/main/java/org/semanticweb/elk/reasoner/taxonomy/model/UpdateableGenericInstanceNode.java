package org.semanticweb.elk.reasoner.taxonomy.model;

/*
 * #%L
 * ELK Reasoner
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

import org.semanticweb.elk.owl.interfaces.ElkEntity;

/**
 * Generic instance node that can be modified.
 * 
 * @author Peter Skocovsky
 *
 * @param <T>
 *            The type of members of the related type nodes.
 * @param <I>
 *            The type of members of this node.
 * @param <TN>
 *            The type of type nodes with which this node may be associated.
 * @param <IN>
 *            The type of instance nodes with which this node may be associated.
 */
public interface UpdateableGenericInstanceNode<T extends ElkEntity, I extends ElkEntity, TN extends UpdateableGenericTypeNode<T, I, TN, IN>, IN extends UpdateableGenericInstanceNode<T, I, TN, IN>>
		extends UpdateableNode<I>, InstanceNode<T, I> {

	/**
	 * Associates this node with its direct type node.
	 * 
	 * @param typeNode
	 *            The type node with which this node should be associated.
	 */
	void addDirectTypeNode(TN typeNode);

	/**
	 * Deletes the association between this node and the specified type node.
	 * 
	 * @param typeNode
	 *            The type node with which this node should not be associated.
	 */
	void removeDirectTypeNode(TN typeNode);

}
