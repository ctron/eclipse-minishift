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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import de.dentrassi.eclipse.minishift.preferences.PreferenceConstants;

public class IpAddressResolver implements IDynamicVariableResolver {

	@Override
	public String resolveValue(final IDynamicVariable variable, final String argument) throws CoreException {

		try {
			final String ip = executeMinishift("ip");

			if ( ip == null ) {
				errorResult();
			}

			if ( ip.trim().isEmpty() ) {
				errorResult();
			}

			return ip;

		} catch (final IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, "Failed to launch 'minishift' binary. You may need to configure the location of the minishift executable in the preferences.", e));
		}
	}

	private void errorResult () throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID, "Minishift seems not to be running"));
	}

	private String executeMinishift(final String... arguments) throws IOException, CoreException {
		final DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(new ExecuteWatchdog(3_000));

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ByteArrayOutputStream err = new ByteArrayOutputStream();
		final PumpStreamHandler handler = new PumpStreamHandler(out, err);

		executor.setStreamHandler(handler);

		@SuppressWarnings("unchecked")
		final Map<String, String> env = new HashMap<>(EnvironmentUtils.getProcEnvironment());
		env.put("LANG", "en_US.UTF-8");

		final CommandLine cmd = findMinishift();
		Arrays.stream(arguments).forEach(cmd::addArgument);

		final int rc = executor.execute(cmd, env);

		if (rc != 0) {
			throw new CoreException(
					new Status(IStatus.ERROR, PLUGIN_ID, String.format("Call to minishift returned with rc = %s", rc)));
		}

		return new String(out.toByteArray(), StandardCharsets.UTF_8);
	}

	private CommandLine findMinishift() {

		final String binary = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_MINISHIFT_PATH);
		if ( binary != null && !binary.isEmpty()) {
			return new CommandLine(binary);
		}

		return new CommandLine("minishift");
	}

}
