package de.dentrassi.eclipse.minishift;

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
import static de.dentrassi.eclipse.minishift.Activator.PLUGIN_ID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

import de.dentrassi.eclipse.minishift.preferences.PreferenceConstants;

public final class Minishift {

	private Minishift() {
	}

	public static String execute(final List<String> arguments) throws IOException, CoreException {
		return execute(arguments.toArray(new String[arguments.size()]));
	}

	public static String execute(final String... arguments) throws IOException, CoreException {
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

		final int rc;
		try {
			rc = executor.execute(cmd, env);
		} catch (final ExecuteException e) {
			final String outString = new String(err.toByteArray(), StandardCharsets.UTF_8);
			final String errString = new String(err.toByteArray(), StandardCharsets.UTF_8);

			StatusManager.getManager()
			.handle(new Status(IStatus.INFO, PLUGIN_ID,
					String.format("Failed to execute: %s%nOutput: %s%nError: %s%n", cmd, outString, errString)),
					StatusManager.LOG);
			throw e;
		}

		final String outString = new String(out.toByteArray(), StandardCharsets.UTF_8);

		if (rc != 0) {
			throw new CoreException(new Status(IStatus.ERROR, PLUGIN_ID,
					String.format("Call to minishift returned with rc = %s: %s", rc, outString)));
		}

		return outString;
	}

	public static CommandLine findMinishift() {

		final String binary = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.P_MINISHIFT_PATH);
		if (binary != null && !binary.isEmpty()) {
			return new CommandLine(binary);
		}

		return new CommandLine("minishift");
	}

}
