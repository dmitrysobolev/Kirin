package com.futureplatforms.kirin.android.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.futureplatforms.kirin.IKirinNativeObject;
import com.futureplatforms.kirin.KirinModule;

abstract public class KirinFragment<Module extends KirinModule<KirinNativeObj>, KirinNativeObj extends IKirinNativeObject>
		extends Fragment implements IKirinNativeObject, IKirinFragment<Module> {
	private Module module;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KirinFragmentMethods.onCreate(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		KirinFragmentMethods.onUnload(this);
	}

	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public void setModule(Module module) {
		this.module = module;
	}

}
