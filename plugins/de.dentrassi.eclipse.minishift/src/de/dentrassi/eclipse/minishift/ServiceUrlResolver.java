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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;

public class ServiceUrlResolver extends AbstractMinishiftResolver {

	private static final String PREFIX_HTTP = "http://";
	private static final String PREFIX_HTTPS = "https://";

	@Override
	public String resolveValue(final IDynamicVariable variable, final String argument) throws CoreException {

		try {
			if (argument == null || argument.isEmpty()) {
				errorResult("Missing argument. You must pass the namespace and service name as argument to the variable: e.g. http://<ns>.<service>");
			}

			boolean https = false;
			String service;

			if (argument.startsWith(PREFIX_HTTPS)) {
				https = true;
				service = argument.substring(PREFIX_HTTPS.length());
			} else if (argument.startsWith(PREFIX_HTTP)) {
				service = argument.substring(PREFIX_HTTP.length());
			} else {
				service = argument;
			}

			final String [] toks = service.split("\\.");
			if ( toks.length != 2 ) {
				errorResult("Wrong argument. You must pass the namespace and service name as argument to the variable: e.g. http://<ns>.<service>");
			}

			final List<String> arguments = new ArrayList<>();
			arguments.add("openshift");
			arguments.add("service");
			if (https) {
				arguments.add("--https");
			}
			arguments.add("-n");
			arguments.add(toks[0]);
			arguments.add("-u");
			arguments.add(toks[1]);

			final String result = execute(arguments);

			if (result == null || result.trim().isEmpty()) {
				errorResult(String.format("Minishift seems not to be running or service '%s' unknown.", service));
			}

			return result.trim();

		} catch (final IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID,
					"Failed to launch 'minishift' binary. You may need to configure the location of the minishift executable in the preferences.",
					e));
		}
	}

}
