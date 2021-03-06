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
import static de.dentrassi.eclipse.minishift.Minishift.execute;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;

public class IpAddressResolver extends AbstractMinishiftResolver {

	@Override
	public String resolveValue(final IDynamicVariable variable, final String argument) throws CoreException {

		try {
			final String ip = execute("ip");

			if (ip == null || ip.trim().isEmpty()) {
				errorResult("Minishift seems not to be running");
			}

			return ip.trim();

		} catch (final IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID,
					"Failed to launch 'minishift' binary. You may need to configure the location of the minishift executable in the preferences.",
					e));
		}
	}

}
