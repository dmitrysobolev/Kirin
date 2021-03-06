package com.futureplatforms.kirin.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.futureplatforms.kirin.KirinModule;
import com.futureplatforms.kirin.android.app.IKirinModuleHost;

public class KirinActivityUtils {

	public static String loadBackgroundModule(FragmentManager fragmentManager,
			IKirinModuleHost<?> fragment) {
		String tag = fragment.getClass().getSimpleName();
		if(fragmentManager.findFragmentByTag(tag) == null)
		fragmentManager.beginTransaction().add((Fragment) fragment, tag)
				.commit();
		return tag;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <Module extends KirinModule<?>> Module getBackgroundModule(
			FragmentManager fragmentManager, String tag)
			throws KirinFragmentException {
		Fragment f = fragmentManager.findFragmentByTag(tag);
		if (f instanceof IKirinModuleHost<?>) {
			return (Module) ((IKirinModuleHost) f).getModule();
		} else
			throw new KirinFragmentException("Fragment is not a Kirin Fragment");

	}

	public static class KirinFragmentException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public KirinFragmentException(String message) {
			super(message);
		}

	}
}
