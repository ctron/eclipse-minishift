/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package de.dentrassi.eclipse.minishift;

import static de.dentrassi.eclipse.minishift.Activator.PLUGIN_ID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariableResolver;

public abstract class AbstractMinishiftResolver implements IDynamicVariableResolver {

	protected void errorResult(final String message) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, message));
	}

}