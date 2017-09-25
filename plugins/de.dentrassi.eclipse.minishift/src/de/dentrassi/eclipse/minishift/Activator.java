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

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.dentrassi.eclipse.minishift";

	private static Activator INSTANCE;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.INSTANCE = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		Activator.INSTANCE = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return INSTANCE;
	}

}
